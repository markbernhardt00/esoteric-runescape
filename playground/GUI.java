package playground;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUI {
    private final JDialog mainDialog;
    private final JComboBox<String> enemySelector;

    private boolean started;

    public GUI() {

        String[] enemies = { "Cow", "Chicken", "Goblin", "Man" };

        mainDialog = new JDialog();
        mainDialog.setTitle("EsotericRS Playground");
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));
        mainDialog.getContentPane().add(mainPanel);

        JPanel enemySelectionPanel = new JPanel();
        enemySelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel enemySelectionLabel = new JLabel("Select an enemy: ");
        enemySelectionPanel.add(enemySelectionLabel);

        enemySelector = new JComboBox<>(enemies);
        enemySelectionPanel.add(enemySelector);

        mainPanel.add(enemySelectionPanel);

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

    public String getSelectedEnemy(){
        return enemySelector.getSelectedItem().toString();
    }

    public void open(){
        mainDialog.setVisible(true);
    }

    public void close(){
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

}
