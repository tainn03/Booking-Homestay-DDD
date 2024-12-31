package nnt.com.domain.image.service;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.image.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageDomainService extends BaseBehaviors<Image, String> {
    String uploadFile(MultipartFile file);

    List<String> uploadFiles(List<MultipartFile> files);

    void deleteFiles(List<String> urls);
}
