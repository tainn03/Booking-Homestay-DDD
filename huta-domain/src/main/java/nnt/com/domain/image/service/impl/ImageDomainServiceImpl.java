package nnt.com.domain.image.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.utils.CloudinaryUtil;
import nnt.com.domain.image.model.entity.Image;
import nnt.com.domain.image.repository.ImageDomainRepository;
import nnt.com.domain.image.service.ImageDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageDomainServiceImpl implements ImageDomainService {
    ImageDomainRepository imageDomainRepository;
    CloudinaryUtil cloudinaryUtil;

    @Override
    public String uploadFile(MultipartFile file) {
        String url = cloudinaryUtil.uploadFile(file);
        return save(Image.builder()
                .url(url)
                .build())
                .getUrl();
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> urls = cloudinaryUtil.uploadFiles(files);
        return urls.stream()
                .map(url -> save(Image.builder()
                        .url(url)
                        .build())
                        .getUrl())
                .toList();
    }

    @Override
    public void deleteFiles(List<String> urls) {
        cloudinaryUtil.deleteFiles(urls);
        urls.forEach(this::delete);
    }

    @Override
    public Image save(Image image) {
        return imageDomainRepository.save(image);
    }

    @Override
    public Image update(Image image) {
        return imageDomainRepository.save(image);
    }

    @Override
    public Image getById(String url) {
        return imageDomainRepository.getById(url);
    }

    @Override
    public Page<Image> getAll(int page, int size, String sort, String direction) {
        return imageDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(String id) {
        imageDomainRepository.delete(id);
    }
}
