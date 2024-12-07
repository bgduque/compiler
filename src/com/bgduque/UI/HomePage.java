package com.bgduque.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HomePage extends JFrame {
    public HomePage() {
        super("Java Compiler");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setVisible(true);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(20, 20, 20, 20);

        JLabel hLabel = new JLabel("Welcome to Java Compiler", JLabel.CENTER);
        hLabel.setFont(new Font("Open Sans", Font.BOLD, 25));
        add(hLabel, c);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton startButton = createButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(startButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        c.gridy = 1;
        add(panel, c);

        // startButton.addActionListener(e -> openCompiler(false));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMinimumSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setFont(new Font("Open Sans", Font.PLAIN, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        return button;
    }

    private void openCompiler(boolean uploadFile) {
        this.dispose();

        SwingUtilities.invokeLater(() -> { 
        });
    }
}
