package nnt.com.domain.shared.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryUtil {
    static Cloudinary cloudinary;
    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static List<String> uploadFiles(List<MultipartFile> files) {
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> uploadFile(file), executorService)
                        .handle((result, ex) -> {
                            if (ex != null) {
                                log.error("ERROR UPLOAD FILE: {}", file.getOriginalFilename(), ex);
                                return null; // Hoặc có thể trả về một giá trị khác nếu cần
                            }
                            return result;
                        }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull) // Lọc bỏ các URL null do lỗi
                .collect(Collectors.toList());
    }

    public static String uploadFile(MultipartFile file) {
        try {
            File convFile = FileUtil.convertMultiPartToFile(file);
            Map uploadResult = cloudinary.uploader().upload(convFile, ObjectUtils.emptyMap());
            if (!convFile.delete()) {
                log.warn("COULD NOT DELETE TEMPORARY FILE: {}", convFile.getAbsolutePath());
            }
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            log.error("COULD NOT UPLOAD FILE: {}", file.getOriginalFilename(), e);
            return null;
        }
    }

    public static void deleteFiles(List<String> urls) {
        List<CompletableFuture<Void>> futures = urls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    String publicId = extractPublicId(url);
                    try {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    } catch (Exception e) {
                        log.error("ERROR DELETE FILE: {}", publicId, e);
                    }
                }, executorService))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private static String extractPublicId(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }
}
