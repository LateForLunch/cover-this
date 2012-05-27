/*
 * CoverPrintable.java
 * Created on May 27, 2012, 3:06:16 PM
 */
package com.googlecode.coverthis.view;

import com.googlecode.coverthis.model.CoverType;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JPanel;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class CoverPrintable extends JPanel implements Printable {

    private CoverType coverType;
    private BufferedImage originalImage;
    private BufferedImage coverTypeImage;
    private BufferedImage scaledImage;

    public CoverPrintable() {
    }

    public CoverType getCoverType() {
        return coverType;
    }

    public void setCoverType(CoverType coverType) {
        if (originalImage != null) {
            this.coverTypeImage = coverType.transform(originalImage);
            this.scaledImage = scaleImageToPanel(coverTypeImage);
        }
        this.coverType = coverType;
        repaint();
    }

    public BufferedImage getImage() {
        return coverTypeImage;
    }

    public void setImage(BufferedImage image) {
        if (coverType != null) {
            this.coverTypeImage = coverType.transform(image);
        } else {
            this.coverTypeImage = image;
        }
        this.originalImage = image;
        this.scaledImage = scaleImageToPanel(coverTypeImage);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scaledImage != null) {
            int pw = getWidth();
            int ph = getHeight();
            int iw = scaledImage.getWidth();
            int ih = scaledImage.getHeight();
            if ((pw != iw) || (ph != ih)) {
                this.scaledImage = scaleImageToPanel(coverTypeImage);
            }
            int offsetx = (pw - iw) / 2;
            int offsety = (ph - ih) / 2;
            g.clearRect(0, 0, pw, ph);
            g.drawImage(scaledImage, offsetx, offsety, null);
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page)
            throws PrinterException {

        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        if (coverType == null) {
            throw new PrinterException("Cover type not set.");
        }
        if (coverTypeImage == null) {
            throw new PrinterException("Image not set.");
        }

        BufferedImage cover = coverTypeImage;
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

//        double coverW = PrintUnitTools.millimetersToUnits(coverType.getWidth());
//        double coverH = PrintUnitTools.millimetersToUnits(coverType.getHeight());
        int coverW = cover.getWidth();
        int coverH = cover.getHeight();

//        if (coverW > coverH) { // Landscape/portrait fix
//            double tmp = coverW;
//            coverW = coverH;
//            coverH = tmp;
//        }

        boolean rotate90degrees = false;
        int pfo = pf.getOrientation();
        if (pfo == PageFormat.PORTRAIT) {
            if (coverW > coverH) {
                rotate90degrees = true;
            }
        } else { // landscape or reverse landscape
            if (coverH > coverW) {
                rotate90degrees = true;
            }
        }

        if (rotate90degrees) {
            int tmp = coverW;
            coverW = coverH;
            coverH = tmp;
            cover = new BufferedImage(coverW, coverH, coverTypeImage.getType());
            AffineTransform at = new AffineTransform();
            at.quadrantRotate(1, 0.5 * coverW, 0.5 * coverW);
            Graphics2D coverG2d = cover.createGraphics();
            coverG2d.drawImage(coverTypeImage, at, null);
            coverG2d.dispose();
        }

        double offsetX = (pf.getImageableWidth() - coverW) / 2;
        double offsetY = (pf.getImageableHeight() - coverH) / 2;

        g2d.drawImage(cover, (int) offsetX, (int) offsetY, null);

//        g2d.drawRect(
//                (int) Math.round(offsetX),
//                (int) Math.round(offsetY),
//                (int) Math.round(coverW),
//                (int) Math.round(coverH));

        return PAGE_EXISTS;
    }

    private BufferedImage scaleImageToPanel(BufferedImage input) {
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        double scaleX = (panelWidth + 0.0) / inputWidth;
        double scaleY = (panelHeight + 0.0) / inputHeight;
        double scale = Math.min(scaleX, scaleY);

        int outputWidth = (int) (scale * inputWidth);
        int outputHeight = (int) (scale * inputHeight);

//        int tmp = outputWidth;
//        outputWidth = outputHeight;
//        outputHeight = tmp;
//        BufferedImage output =
//                new BufferedImage(outputWidth, outputHeight, input.getType());
//        AffineTransform at =
//                AffineTransform.getQuadrantRotateInstance(
//                1, 0.5 * outputWidth, 0.5 * outputWidth);
//        Graphics2D coverG2d = output.createGraphics();
//        coverG2d.drawImage(input, at, null);
//        coverG2d.dispose();

        BufferedImage output = new BufferedImage(
                outputWidth, outputHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = output.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(input, 0, 0, outputWidth, outputHeight, null);
        g2d.dispose();

        return output;
    }
}
