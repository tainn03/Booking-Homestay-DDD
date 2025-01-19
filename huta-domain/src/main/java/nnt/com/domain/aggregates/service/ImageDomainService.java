package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.Image;
import nnt.com.domain.shared.behaviors.BaseBehaviors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageDomainService extends BaseBehaviors<Image, String> {
    String uploadFile(MultipartFile file);

    List<String> uploadFiles(List<MultipartFile> files);

    void deleteFiles(List<String> urls);
}
