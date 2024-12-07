import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

public class MainPage extends JFrame {
    private JTextArea codeTextArea;
    private final JTextArea resultTextArea = new JTextArea();
    private final JButton lexicalAnalysisButton = createButton("Lexical Analysis");
    private final JButton syntaxAnalysisButton = createButton("Syntax Analysis");
    private final JButton semanticAnalysisButton = createButton("Semantic Analysis");
    private final JButton runButton = createButton("Run");
    private UndoManager undoManager = new UndoManager();

    public MainPage() {
        setTitle("Mini Java Compiler");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());
        add(createHeaderLabel(), BorderLayout.NORTH);
        add(createSplitPane(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        initializeComponents();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(createMenuItem("Open File...", e -> openFile()));
        fileMenu.add(createMenuItem("New Window", e -> openNewWindow()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", e -> System.exit(0)));

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        editMenu.add(createMenuItem("Undo", e -> undoAction(), KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(createMenuItem("Redo", e -> redoAction(), KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        editMenu.addSeparator();
        editMenu.add(createMenuItem("Cut", e -> codeTextArea.cut()));
        editMenu.add(createMenuItem("Copy", e -> codeTextArea.copy()));
        editMenu.add(createMenuItem("Paste", e -> codeTextArea.paste()));
        editMenu.add(createMenuItem("Delete", e -> codeTextArea.replaceSelection("")));
        editMenu.addSeparator();
        editMenu.add(createMenuItem("Select All", e -> codeTextArea.selectAll()));

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        JMenu zoomMenu = new JMenu("Zoom");
        viewMenu.add(zoomMenu);
        zoomMenu.add(createMenuItem("Zoom In", e -> zoomIn()));
        zoomMenu.add(createMenuItem("Zoom Out", e -> zoomOut()));
        zoomMenu.addSeparator();
        zoomMenu.add(createMenuItem("Restore Default Zoom", e -> restoreDefaultZoom()));

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        helpMenu.add(createMenuItem("About", e -> showAboutDialog()));

        return menuBar;
    }

    private JLabel createHeaderLabel() {
        JLabel headerLabel = new JLabel("Mini Java Compiler", JLabel.CENTER);
        headerLabel.setFont(new Font("Open Sans", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return headerLabel;
    }

    private JSplitPane createSplitPane() {
        codeTextArea = new JTextArea();
        codeTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        codeScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code Editor"));
        codeTextArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        resultTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
        resultTextArea.setEditable(false);
        resultTextArea.setEnabled(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Analysis Results"));

        return new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScrollPane, resultScrollPane);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // lexicalAnalysisButton = createButton("Lexical Analysis");
        // syntaxAnalysisButton = createButton("Syntax Analysis");
        // semanticAnalysisButton = createButton("Semantic Analysis");
        // runButton = createButton("Run");

        buttonPanel.add(lexicalAnalysisButton);
        buttonPanel.add(syntaxAnalysisButton);
        buttonPanel.add(semanticAnalysisButton);
        buttonPanel.add(runButton);

        return buttonPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Open Sans", Font.PLAIN, 16));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener, int keyCode, int modifiers) {
        JMenuItem menuItem = createMenuItem(text, actionListener);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        return menuItem;
    }

    private void openFile() {
        // Implementation for opening a file
    }

    private void openNewWindow() {
        SwingUtilities.invokeLater(() -> {
            MainPage newMainPage = new MainPage();
            newMainPage.setVisible(true);
        });
    }

    private void undoAction() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void redoAction() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    private void zoomIn() {
        // Implementation for zooming in
    }

    private void zoomOut() {
        // Implementation for zooming out
    }

    private void restoreDefaultZoom() {
        // Implementation for restoring default zoom
    }

    private void showAboutDialog() {
        // Implementation for showing about dialog
    }

    private void initializeComponents() {
        // Additional initialization if needed
    }
}