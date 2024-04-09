package com.anroypaul.imagecomparator;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {

    private MultipartFile firstImage;
    private MultipartFile secondImage;

    public MultipartFile getFirstImage() {
        return firstImage;
    }

    public MultipartFile getSecondImage() {
        return secondImage;
    }
}
