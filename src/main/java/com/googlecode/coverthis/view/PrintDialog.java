/*
 * PrintDialog.java
 * Created on May 27, 2012, 3:07:25 PM
 */
package com.googlecode.coverthis.view;

import com.googlecode.coverthis.model.PrinterModel;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class PrintDialog extends JDialog {

    private PrinterModel printerModel;
    private JComboBox cmb_printers;
    private JButton btn_ok;

    public PrintDialog(PrinterModel printerModel, Frame owner) {
        super(owner, "Select printer", true);
        this.printerModel = printerModel;
        init();
    }

    private void init() {
        setLayout(new FlowLayout());

        this.cmb_printers = new JComboBox(printerModel.getPrinterNames());
        this.btn_ok = new JButton("Ok");
        btn_ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                PrintDialog.this.setVisible(false);
                printerModel.setPrinter(
                        cmb_printers.getSelectedItem().toString());
                try {
                    printerModel.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(cmb_printers);
        add(btn_ok);

        pack();
    }
}
