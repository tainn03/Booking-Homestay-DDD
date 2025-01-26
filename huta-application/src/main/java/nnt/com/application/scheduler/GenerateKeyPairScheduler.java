package nnt.com.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GenerateKeyPairScheduler {

    private static final String GIT_BASH_PATH = "C:\\Program Files\\Git\\bin\\bash.exe";

    @Scheduled(cron = "0 0 0 1 * ?") // chạy vào lúc 0h ngày 1 hàng tháng
    public void generateKeyPair() {
        try {
            Path certificatesDir = Path.of("D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates");
            if (!Files.exists(certificatesDir)) {
                Files.createDirectories(certificatesDir);
            }

            String command = "openssl genrsa -out D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/privatekey.pem 2048 && " +
                    "openssl rsa -in D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/privatekey.pem -pubout -out D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/publickey.pem";

            ProcessBuilder processBuilder = new ProcessBuilder(GIT_BASH_PATH, "-c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Đọc kết quả từ command
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Key pair generated and saved to huta-start/src/main/resources/certificates");
            } else {
                log.error("Key pair generation failed with exit code: {}", exitCode);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error generating key pair", e);
            Thread.currentThread().interrupt(); // Phục hồi trạng thái interrupt
        }
    }
}
