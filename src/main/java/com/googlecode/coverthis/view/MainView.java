/*
 * MainView.java
 * Created on May 27, 2012, 3:07:00 PM
 */
package com.googlecode.coverthis.view;

import com.googlecode.coverthis.controller.MainController;
import com.googlecode.coverthis.model.CoverTypeModel;
import com.googlecode.coverthis.model.PrinterModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class MainView {

    private MainController controller;
    private PrinterModel printerModel;
    private CoverTypeModel coverTypeModel;
    private JFrame frame;
    private CoverPrintable coverPrintable;
    private JDialog printDialog;
    private JComboBox cmb_coverTypes;

    public MainView(
            MainController controller,
            PrinterModel printerModel,
            CoverTypeModel coverTypeModel) {

        this.controller = controller;
        this.printerModel = printerModel;
        this.coverTypeModel = coverTypeModel;
        init();
    }

    private void init() {
        this.frame = new JFrame("Cover Printer");
        this.printDialog = new PrintDialog(printerModel, frame);

        JPanel pnl_main = new JPanel(new BorderLayout());

        this.coverPrintable = controller.getCoverPrintable();
        coverPrintable.setPreferredSize(new Dimension(500, 300));
        coverPrintable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File img = fileChooser.getSelectedFile();
                    try {
                        coverPrintable.setImage(ImageIO.read(img));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        pnl_main.add(coverPrintable, BorderLayout.CENTER);

        JPanel pnl_controls = new JPanel(new FlowLayout());
        pnl_controls.add(new JLabel("Select Cover Type: "));
        this.cmb_coverTypes = new JComboBox(coverTypeModel.getCoverNames());
        cmb_coverTypes.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                coverPrintable.setCoverType(
                        coverTypeModel.getCoverType(
                        cmb_coverTypes.getSelectedIndex()));
            }
        });
        pnl_controls.add(cmb_coverTypes);
        JButton btn_print = new JButton("Print...");
        btn_print.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                coverPrintable.setCoverType(
                        coverTypeModel.getCoverType(
                        cmb_coverTypes.getSelectedIndex()));
                printDialog.setLocationRelativeTo(frame);
                printDialog.setVisible(true);
            }
        });
        pnl_controls.add(btn_print);
        pnl_main.add(pnl_controls, BorderLayout.PAGE_END);

        frame.getContentPane().add(pnl_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
