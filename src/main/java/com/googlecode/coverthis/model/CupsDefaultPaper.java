/*
 * CupsDefaultPaper.java
 * Created on May 27, 2012, 3:03:43 PM
 */
package com.googlecode.coverthis.model;

import java.awt.print.Paper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class CupsDefaultPaper extends Paper {

    private String printerName;
    private Map<String, String> ppd;

    public CupsDefaultPaper(String printerName) {
        super();
        this.printerName = printerName;
        this.ppd = new HashMap<String, String>();
        init();
    }

    private void init() {
        File ppdFile = new File("/etc/cups/ppd/" + printerName + ".ppd");
        if (!ppdFile.isFile()) {
            throw new IllegalArgumentException(
                    "/etc/cups/ppd/" + printerName + ".ppd not found.");
        }

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(ppdFile);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException ex) {
            // This should not happen
            ex.printStackTrace();
        }

        String line;
        String[] split;
        try {
            while ((line = br.readLine()) != null) {
                split = line.split(":", 2);
                if (split.length == 2) {
                    String s1 = split[0].trim();
                    String s2 = split[1].trim();
                    ppd.put(s1, s2);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            br.close();
            fr.close();
        } catch (IOException ex) {
            // Ignore
        }

        String defaultPageSize = ppd.get("*DefaultPageSize");
        String paperDim = null;
        String imageableArea = null;
        for (String key : ppd.keySet()) {
            if (key.startsWith("*PaperDimension " + defaultPageSize + "/")) {
                paperDim = ppd.get(key);
            } else if (key.startsWith("*ImageableArea " + defaultPageSize + "/")) {
                imageableArea = ppd.get(key);
            }
        }

        String[] paperDimSplit = paperDim.split(" ");
        double w = Double.parseDouble(paperDimSplit[0].substring(1));
        double h = Double.parseDouble(paperDimSplit[1].substring(0, paperDimSplit[1].length() - 1));
        super.setSize(w, h);

        String[] imageableAreaSplit = imageableArea.split(" ");
        double x = Double.parseDouble(imageableAreaSplit[0].substring(1));
        double y = Double.parseDouble(imageableAreaSplit[1]);
        w = Double.parseDouble(imageableAreaSplit[2]);
        h = Double.parseDouble(imageableAreaSplit[3].substring(0, imageableAreaSplit[3].length() - 1));
        super.setImageableArea(x, y, w, h);
    }
}
