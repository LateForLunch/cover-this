package com.googlecode.coverthis;

import com.googlecode.coverthis.controller.MainController;
import com.googlecode.coverthis.model.CoverTypeModel;
import com.googlecode.coverthis.model.PrinterModel;
import javax.swing.UIManager;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            String cn = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(cn); // Use the native L&F
        } catch (Exception ex) {
        }

        PrinterModel printerModel = new PrinterModel();
        CoverTypeModel coverTypeModel = new CoverTypeModel();
        MainController controller = new MainController(printerModel, coverTypeModel);
    }
}
