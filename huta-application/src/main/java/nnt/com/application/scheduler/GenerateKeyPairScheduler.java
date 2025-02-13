//package nnt.com.application.scheduler;
//
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//import static lombok.AccessLevel.PRIVATE;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//@FieldDefaults(level = PRIVATE, makeFinal = true)
//public class GenerateKeyPairScheduler {
//
//    private static final String GIT_BASH_PATH = "C:\\Program Files\\Git\\bin\\bash.exe";
//
//    @Scheduled(cron = "0 0 0 1 * ?") // chạy vào lúc 0h ngày 1 hàng tháng
//    public void generateKeyPair() {
//        try {
//            generateCertificatesDir();
//
//            Process process = getProcess();
//
//            // Đọc kết quả từ command line
//            log(process);
//
//        } catch (IOException | InterruptedException e) {
//            log.error("Error generating key pair", e);
//            Thread.currentThread().interrupt(); // Phục hồi trạng thái interrupt
//        }
//    }
//
//    private void generateCertificatesDir() throws IOException {
//        Path certificatesDir = Path.of("D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates");
//        if (!Files.exists(certificatesDir)) {
//            Files.createDirectories(certificatesDir);
//        }
//    }
//
//    private static Process getProcess() throws IOException {
//        String command = "openssl genrsa -out D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/privatekey.pem 2048 && " +
//                "openssl rsa -in D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/privatekey.pem -pubout -out D:/LuanVan/huta/booking/huta-start/src/main/resources/certificates/publickey.pem";
//
//        ProcessBuilder processBuilder = new ProcessBuilder(GIT_BASH_PATH, "-c", command);
//        processBuilder.redirectErrorStream(true);
//        return processBuilder.start();
//    }
//
//    private void log(Process process) throws IOException, InterruptedException {
//        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                log.info(line);
//            }
//        }
//
//        if (process.waitFor() == 0) {
//            log.info("KEY PAIR GENERATED AND SAVED TO huta-start/src/main/resources/certificates");
//        }
//    }
//}
