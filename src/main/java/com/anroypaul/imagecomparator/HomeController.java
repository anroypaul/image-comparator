package com.anroypaul.imagecomparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Controller
public class HomeController {

    private final FileService fileService;

    private final ImageCompareService imageCompareService;

    @Autowired
    HomeController(FileService fileService, ImageCompareService imageCompareService) {
        this.fileService = fileService;
        this.imageCompareService = imageCompareService;
    }


    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("image1") MultipartFile image1, @RequestParam("image2") MultipartFile image2, RedirectAttributes attributes) throws IOException {
        // check if file is empty
        if (image1.isEmpty() || image2.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select images to upload. Both images are required");
            return "redirect:/";
        }

        try {
            // upload file to server
            File file1 = fileService.uploadFile(image1);
            File file2 = fileService.uploadFile(image2);

            BufferedImage diffImage = imageCompareService.getComparedImage(file1, file2);

            attributes.addFlashAttribute("base64Image", fileService.convertImageToBase64(diffImage));
            return "redirect:/";
        } catch (ImageComparisonException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // TODO remove files from the server
            fileService.removeFilesFromUploadsDirectory();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded an image!");

        return "redirect:/";
    }

    @PostMapping("/reset")
    public String reset() {
        // remove all files from FS
        fileService.removeFilesFromUploadsDirectory();
        return "index";
    }
}
