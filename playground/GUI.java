package playground;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class GUI {
    private final JDialog mainDialog;
    private final JCheckBox bankCB;
    private final JCheckBox bonesCB;

    private boolean started;

    public GUI() {
        mainDialog = new JDialog();
        mainDialog.setTitle("EsotericRS Playground");
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));
        mainDialog.getContentPane().add(mainPanel);

        JPanel bankChoosePanel = new JPanel();
        bankChoosePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel bonesChoosePanel = new JPanel();
        bonesChoosePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel bankChooseLabel = new JLabel("Bank items?");
        bankChoosePanel.add(bankChooseLabel);

        bankCB = new JCheckBox();
        bankChoosePanel.add(bankCB);

        bonesCB = new JCheckBox();
        bankChoosePanel.add(bonesCB);
        JLabel bonesChooseLabel = new JLabel("Bury Bones? (Warning: Lots of bones)");
        bonesChoosePanel.add(bonesChooseLabel);

        mainPanel.add(bankChoosePanel);
        mainPanel.add(bonesChoosePanel);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            started = true;
            close();
        });
        mainPanel.add(startButton);

        mainDialog.pack();
    }

    public boolean isStarted() {
        return started;
    }

    public Boolean getIsBankChecked(){
        return bankCB.isSelected();
    }

    public Boolean getIsBonesChecked(){
        return bonesCB.isSelected();
    }

    public void open(){
        mainDialog.setVisible(true);
    }

    public void close(){
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

}