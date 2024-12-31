package nnt.com.domain.base.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileUtil {
    File convertMultiPartToFile(MultipartFile file) throws IOException;
}
