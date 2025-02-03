package nnt.com.domain.aggregates.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Image;
import nnt.com.domain.aggregates.repository.ImageDomainRepository;
import nnt.com.domain.aggregates.service.ImageDomainService;
import nnt.com.domain.shared.utils.CloudinaryUtil;
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
        return cloudinaryUtil.uploadFile(file);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        return cloudinaryUtil.uploadFiles(files);
    }

    @Override
    public void deleteFiles(List<String> urls) {
        cloudinaryUtil.deleteFiles(urls);
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
