import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Mini Java Compiler");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // Header label
        JLabel headerLabel = new JLabel("Welcome to Mini Java Compiler!", JLabel.CENTER);
        headerLabel.setFont(new Font("Open Sans", Font.BOLD, 30));
        add(headerLabel, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton startButton = createButton("Start");

        // Center alignment
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Adjust space between buttons

        gbc.gridy = 1;
        add(buttonPanel, gbc);

        // Button actions
        startButton.addActionListener(e -> openCompiler(false));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50)); // Button size
        button.setMinimumSize(new Dimension(200, 50)); // Set minimum size
        button.setMaximumSize(new Dimension(200, 50)); // Set maximum size
        button.setFont(new Font("Open Sans", Font.PLAIN, 16));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        return button;
    }

    private void openCompiler(boolean uploadFile) {
        // Close current frame
        this.dispose();

        // Open MiniCompiler
        SwingUtilities.invokeLater(() -> {
            MainPage compiler = new MainPage();
            compiler.setVisible(true);

            if (uploadFile) {
                compiler.openFile();
            }
        });
    }
}