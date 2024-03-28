package com.anroypaul.imagecomparator;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
//import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
public class ImageCompareService {

    public BufferedImage getComparedImage(File fileImage1, File fileImage2) throws ImageComparisonException {
        try {
            ArrayList<int[]> diffs = new ArrayList<>();
            BufferedImage image1 = ImageIO.read(fileImage1);
            BufferedImage image2 = ImageIO.read(fileImage2);

            if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
                System.err.println("Images have different dimensions.");
                throw new ImageComparisonException("Images have different dimensions.");
            }

            BufferedImage diffImage = new BufferedImage(image2.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = diffImage.createGraphics();
            g.drawImage(image2, 0, 0, null);

            for (int y = 0; y < image1.getHeight(); y++) {
                for (int x = 0; x < image1.getWidth(); x++) {
                    int rgb1 = image1.getRGB(x, y);
                    int rgb2 = image2.getRGB(x, y);
                    if (rgb1 != rgb2) {
                        diffs.add(new int[]{x, y});

                    }
                }
            }

            diffs.sort(Comparator.comparingInt(a -> a[0]));
            ArrayList<LinkedList<int[]>> groupedPairs = findConsecutiveNumbers(diffs);

            // Print the grouped pairs
            for (LinkedList<int[]> group : groupedPairs) {
                int startX = group.getFirst()[0];
                int startY = group.getFirst()[1];
                int endX = group.getLast()[0];
                int endY = group.getLast()[1];
                drawRectangle(g, startX - 5, startY - 5, endX - startX + 10, endY - startY + 10);

            }

            // displayImage(diffImage, "Compared Image");

            return diffImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void drawRectangle(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(Color.RED);
        g.drawRect(x, y, width, height);
    }

    /*private static void displayImage(BufferedImage image, String title) {
        JFrame frame = new JFrame(title);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/

    private ArrayList<LinkedList<int[]>> findConsecutiveNumbers(ArrayList<int[]> pairs) {
        ArrayList<LinkedList<int[]>> consecutiveNumbers = new ArrayList<>();
        if (pairs.isEmpty()) {
            return consecutiveNumbers;
        }

        List<int[]> currentSequence = new ArrayList<>();
        int[] firstPair = pairs.get(0);
        currentSequence.add(firstPair);

        for (int i = 1; i < pairs.size(); i++) {
            if (pairs.get(i)[0] - pairs.get(i - 1)[0] <= 10) {
                currentSequence.add(pairs.get(i));
            } else {
                consecutiveNumbers.add(new LinkedList<>(currentSequence));
                currentSequence.clear();
                currentSequence.add(pairs.get(i));
            }
        }

        // Add the last sequence if it exists
        if (!currentSequence.isEmpty()) {
            consecutiveNumbers.add(new LinkedList<>(currentSequence));
        }

        return consecutiveNumbers;
    }
}
