/*
 * DvdAsDvdSlim.java
 * Created on May 27, 2012, 3:05:31 PM
 */
package com.googlecode.coverthis.model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class DvdAsDvdSlim extends CoverType {

    private static final String NAME = "DVD Printed As Slim DVD";
    private static final double DVD_THICKNESS = 14.0;
    private static final double DVD_SLIM_THICKNESS = 7.0;
    private static final double WIDTH = DVD.getWidth() - (DVD_THICKNESS - DVD_SLIM_THICKNESS);
    private static final double HEIGHT = DVD.getHeight();

    public DvdAsDvdSlim() {
        super(NAME, WIDTH, HEIGHT);
    }

    @Override
    public BufferedImage transform(BufferedImage input) {
        BufferedImage dvdImg = DVD.transform(input);

        int outputW = (int) Math.round(
                PrintUnitTools.millimetersToUnits(getWidth()));
        int outputH = (int) Math.round(
                PrintUnitTools.millimetersToUnits(getHeight()));

        int partW = (int) Math.round(
                PrintUnitTools.millimetersToUnits(
                (DVD.getWidth() - DVD_THICKNESS) / 2));
        int dvdCenterW = (int) Math.round(
                PrintUnitTools.millimetersToUnits(DVD_THICKNESS));
        int dvdSlimCenterW = (int) Math.round(
                PrintUnitTools.millimetersToUnits(DVD_SLIM_THICKNESS));

        BufferedImage leftImg = dvdImg.getSubimage(
                0, 0, partW, dvdImg.getHeight());
        BufferedImage centerImg = dvdImg.getSubimage(
                partW, 0, dvdCenterW, dvdImg.getHeight());
        BufferedImage rightImg = dvdImg.getSubimage(
                partW + dvdCenterW, 0, dvdImg.getWidth() - partW - dvdCenterW, dvdImg.getHeight());

        AffineTransform at = new AffineTransform();
        at.scale((dvdSlimCenterW + 0.0) / centerImg.getWidth(), 1.0);
        AffineTransformOp op =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
        BufferedImage newCenterImg = op.filter(centerImg, null);

        BufferedImage output = new BufferedImage(outputW, outputH, input.getType());
        Graphics2D g2d = output.createGraphics();
        g2d.drawImage(leftImg, 0, 0, null);
        g2d.drawImage(rightImg, partW + dvdSlimCenterW, 0, null);
        g2d.drawImage(newCenterImg, partW, 0, null);
        g2d.dispose();

        return output;
    }
}
