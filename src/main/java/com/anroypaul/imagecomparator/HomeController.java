package com.anroypaul.imagecomparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final FileService fileService;
    private final FileValidator fileValidator;
    private final ImageCompareService imageCompareService;


    @Autowired
    public HomeController(FileService fileService, FileValidator fileValidator, ImageCompareService imageCompareService) {
        this.fileService = fileService;
        this.fileValidator = fileValidator;
        this.imageCompareService = imageCompareService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("imageForm", new ImageForm());
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute ImageForm imageForm, BindingResult result, RedirectAttributes attributes) {
        fileValidator.validate(imageForm, result);

        if (result.hasErrors()) {
            // Handle validation errors
            List<String> errors = new ArrayList<>();

            // Get field errors
            for (FieldError error : result.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }

            // Get global errors
            for (ObjectError error : result.getGlobalErrors()) {
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            }
            attributes.addFlashAttribute("errors", errors);
            return  "redirect:/";
        }

        MultipartFile image1 = imageForm.getFirstImage();
        MultipartFile image2 = imageForm.getSecondImage();

        // check if file is empty
        if (image1.isEmpty() || image2.isEmpty()) {
            attributes.addFlashAttribute("errors", "Please select images to upload. Both images are required");
            return "redirect:/";
        }

        try {
            // upload file to server
            File file1 = fileService.uploadFile(image1);
            File file2 = fileService.uploadFile(image2);

            BufferedImage diffImage = imageCompareService.getComparedImage(file1, file2);

            attributes.addFlashAttribute("base64Image", fileService.convertImageToBase64(diffImage));
            return "redirect:/";
        } catch (ImageComparisonException | IOException e) {
            attributes.addFlashAttribute("errors", e.getMessage());
            return "redirect:/";
        } finally {
            // TODO remove files from the server
            fileService.removeFilesFromUploadsDirectory();
        }
    }

    @PostMapping("/reset")
    public String reset() {
        // remove all files from FS
        fileService.removeFilesFromUploadsDirectory();
        return "index";
    }
}
