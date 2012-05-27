/*
 * CoverType.java
 * Created on May 27, 2012, 3:02:35 PM
 */
package com.googlecode.coverthis.model;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class CoverType {

    public static final CoverType DVD = new CoverType("DVD", 272, 184); // Or 278 x 190
    public static final CoverType DVD_SLIM = new CoverType("DVD (Slim)", 68, 46);

    private String name;
    private double width; // in mm
    private double height; // in mm

    public CoverType(String name, double width, double height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getRatio() {
        return width / height;
    }

    public BufferedImage transform(BufferedImage input) {
        int inputW = input.getWidth();
        int inputH = input.getHeight();

        int outputW = (int) Math.round(
                PrintUnitTools.millimetersToUnits(getWidth()));
        int outputH = (int) Math.round(
                PrintUnitTools.millimetersToUnits(getHeight()));

//        BufferedImage output =
//                new BufferedImage(outputW, outputH, input.getType());
        AffineTransform at = new AffineTransform();
        if ((inputW > inputH && outputH > outputW) ||
                (inputH > inputW && outputW > outputH)) {
            at.quadrantRotate(1);
        }
        at.scale((outputW + 0.0) / inputW, (outputH + 0.0) / inputH);
//        Graphics2D g2d = output.createGraphics();
//        g2d.drawImage(input, at, null);
//        g2d.dispose();

        AffineTransformOp op =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        BufferedImage output = op.filter(input, null);

        return output;
    }
}
