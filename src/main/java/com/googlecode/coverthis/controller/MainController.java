/*
 * MainController.java
 * Created on May 27, 2012, 3:00:53 PM
 */
package com.googlecode.coverthis.controller;

import com.googlecode.coverthis.model.CoverTypeModel;
import com.googlecode.coverthis.model.PrinterModel;
import com.googlecode.coverthis.view.CoverPrintable;
import com.googlecode.coverthis.view.MainView;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class MainController {

    private PrinterModel printerModel;
    private CoverPrintable coverPrintable;
    private CoverTypeModel coverTypeModel;
    private MainView mainView;

    public MainController(PrinterModel printerModel, CoverTypeModel coverTypeModel) {
        this.printerModel = printerModel;
        this.coverTypeModel = coverTypeModel;
        this.coverPrintable = new CoverPrintable();
        printerModel.setPrintable(coverPrintable);
        this.mainView = new MainView(this, printerModel, coverTypeModel);
    }

    public CoverPrintable getCoverPrintable() {
        return coverPrintable;
    }
}
