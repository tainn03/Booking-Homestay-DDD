package nnt.com.domain.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryUtil {
    List<String> uploadFiles(List<MultipartFile> files);

    String uploadFile(MultipartFile file);

    void deleteFiles(List<String> urls);
}
