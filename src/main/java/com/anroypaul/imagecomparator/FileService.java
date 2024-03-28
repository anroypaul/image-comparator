package com.anroypaul.imagecomparator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Comparator;

@Service
public class FileService {

    public String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    public String encodeImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

//    public File convertMultipartfileToFile(MultipartFile multipartFile) throws IOException {
//        File file = new File(StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())));
//
//        multipartFile.transferTo(file);
//        return file;
//    }

    public String convertImageToBase64(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public File uploadFile(MultipartFile file) throws IOException {
        if (Files.notExists(Paths.get(UPLOAD_DIRECTORY))) {
            Files.createDirectory(Paths.get(UPLOAD_DIRECTORY));
        }
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        return fileNameAndPath.toFile();
    }

    public void removeFilesFromUploadsDirectory() {
        try {
            Files.walk(Path.of(UPLOAD_DIRECTORY))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            System.out.println("Files were removed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
