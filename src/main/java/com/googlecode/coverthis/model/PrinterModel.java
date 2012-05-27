/*
 * PrinterModel.java
 * Created on May 27, 2012, 3:04:19 PM
 */
package com.googlecode.coverthis.model;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class PrinterModel {

    private PrintService[] printServices;
    private PrintService printService;
    private Printable painter;

    public PrinterModel() {
        this.printServices = PrinterJob.lookupPrintServices();
    }

    public String[] getPrinterNames() {
        String[] printerNames = new String[printServices.length];
        for (int i = 0; i < printServices.length; i++) {
            printerNames[i] = printServices[i].getName();
        }
        return printerNames;
    }

    public boolean setPrinter(String printerName) {
        for (int i = 0; i < printServices.length; i++) {
            if (printServices[i].getName().equals(printerName)) {
                this.printService = printServices[i];
                return true;
            }
        }
        return false;
    }

    public void setPrintable(Printable painter) {
        this.painter = painter;
    }

    public void print() throws PrinterException {
        if (printService == null) {
            throw new NullPointerException("No printer set.");
        }
        if (painter == null) {
            throw new NullPointerException("No painter set.");
        }

        PageFormat pf = new PageFormat();
        pf.setPaper(new CupsDefaultPaper(printService.getName()));
//        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintService(printService);
        pj.setPrintable(painter, pf);
        pj.print();
    }
}
