package com.anroypaul;

import java.io.File;

public class Main {


    public static void main(String[] args) {
        new ImageCompareService().getComparedImage(new File("image1.png"), new File("image2.png"));
    }

}
