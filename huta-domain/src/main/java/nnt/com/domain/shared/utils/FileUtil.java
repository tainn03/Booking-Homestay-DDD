package nnt.com.domain.shared.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileUtil {
    File convertMultiPartToFile(MultipartFile file) throws IOException;
}
