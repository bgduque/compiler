import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import javax.swing.undo.UndoManager;

public class JavaMiniCompiler {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}

class HomePage extends JFrame {

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

class MainPage extends JFrame {
    private JTextArea codeTextArea;
    private JTextArea resultTextArea;
    private JButton lexicalAnalysisButton, syntaxAnalysisButton, semanticAnalysisButton, runButton; // Add Run button
    private UndoManager undoManager = new UndoManager(); // Add UndoManager

    public MainPage() {
        // Set up the frame
        setTitle("Mini Java Compiler");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a "File" menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Add "Open File" menu item
        JMenuItem openFileMenuItem = new JMenuItem("Open File...");
        fileMenu.add(openFileMenuItem);
        openFileMenuItem.addActionListener(e -> openFile());

        // Add "Create New File" menu item
        JMenuItem createNewFileMenuItem = new JMenuItem("New Window");
        fileMenu.add(createNewFileMenuItem);
        createNewFileMenuItem.addActionListener(e -> {
            // Create a new instance of MainPage to simulate opening a new window
            SwingUtilities.invokeLater(() -> {
                MainPage newMainPage = new MainPage();
                newMainPage.setVisible(true);  // Show the new main page window
            });
        });

        // Add a separator line after the "New Window" menu item
        fileMenu.addSeparator();

        // Add "Exit" menu item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(e -> System.exit(0)); // Exit the application

        // Create an "Edit" menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        // Add "Undo" menu item
        JMenuItem undoMenuItem = new JMenuItem("Undo");
        editMenu.add(undoMenuItem);
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undoMenuItem.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });

        // Add "Redo" menu item
        JMenuItem redoMenuItem = new JMenuItem("Redo");
        editMenu.add(redoMenuItem);
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        redoMenuItem.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });

        // Add a separator line
        editMenu.addSeparator();

        // Add "Cut" menu item
        JMenuItem cutMenuItem = new JMenuItem("Cut");
        editMenu.add(cutMenuItem);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener(e -> codeTextArea.cut());

        // Add "Copy" menu item
        JMenuItem copyMenuItem = new JMenuItem("Copy");
        editMenu.add(copyMenuItem);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener(e -> codeTextArea.copy());

        // Add "Paste" menu item
        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        editMenu.add(pasteMenuItem);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener(e -> codeTextArea.paste());

        // Add "Delete" menu item
        JMenuItem deleteMenuItem = new JMenuItem("Delete");
        editMenu.add(deleteMenuItem);
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteMenuItem.addActionListener(e -> codeTextArea.replaceSelection(""));
        
        // Add a separator line
        editMenu.addSeparator();

        JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        editMenu.add(selectAllMenuItem);
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        selectAllMenuItem.addActionListener(e -> codeTextArea.selectAll());

        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);

        // Zoom Menu
        JMenu zoomMenu = new JMenu("Zoom");
        viewMenu.add(zoomMenu);

        // Zoom In
        JMenuItem zoomInMenuItem = new JMenuItem("Zoom In");
        zoomMenu.add(zoomInMenuItem);
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
        zoomInMenuItem.addActionListener(e -> zoomIn());

        // Zoom Out
        JMenuItem zoomOutMenuItem = new JMenuItem("Zoom Out");
        zoomMenu.add(zoomOutMenuItem);
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
        zoomOutMenuItem.addActionListener(e -> zoomOut());

        // Add separator between Zoom In/Out and Restore Default Zoom
        zoomMenu.addSeparator();

        // Restore Default Zoom
        JMenuItem restoreDefaultZoomMenuItem = new JMenuItem("Restore Default Zoom");
        zoomMenu.add(restoreDefaultZoomMenuItem);
        restoreDefaultZoomMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK));
        restoreDefaultZoomMenuItem.addActionListener(e -> restoreDefaultZoom());

        // Create a "Help" menu
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // Add "About" menu item
        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener(e -> showAboutDialog());

        
        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        // Header Label
        JLabel headerLabel = new JLabel("Mini Java Compiler", JLabel.CENTER);
        headerLabel.setFont(new Font("Open Sans", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(headerLabel, BorderLayout.NORTH);

        // Code Text Area with Buttons
        codeTextArea = new JTextArea();
        codeTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        codeScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code Editor"));
        codeTextArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit())); // Add UndoableEditListener

        JPanel codeEditorPanel = new JPanel(new BorderLayout());
        codeEditorPanel.add(codeScrollPane, BorderLayout.CENTER);

        JPanel codeEditorButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Changed to RIGHT alignment
        runButton = createButton("Run");  // Initialize Run button
        runButton.setPreferredSize(new Dimension(120, 35));
        runButton.setFont(new Font("Open Sans", Font.PLAIN, 14));

        codeEditorButtonPanel.add(runButton);  // Add Run button to the panel
        codeEditorPanel.add(codeEditorButtonPanel, BorderLayout.NORTH);
        // Create a context menu (right-click menu)
        JPopupMenu contextMenu = new JPopupMenu();

        // Create menu items for the context menu
        JMenuItem undoContextMenuItem = new JMenuItem("Undo");
        JMenuItem redoContextMenuItem = new JMenuItem("Redo");
        JMenuItem cutContextMenuItem = new JMenuItem("Cut");
        JMenuItem copyContextMenuItem = new JMenuItem("Copy");
        JMenuItem pasteContextMenuItem = new JMenuItem("Paste");
        JMenuItem deleteContextMenuItem = new JMenuItem("Delete");
        JMenuItem selectAllContextMenuItem = new JMenuItem("Select All");

        // Add actions for each menu item
        undoContextMenuItem.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        });
        redoContextMenuItem.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        });

        cutContextMenuItem.addActionListener(e -> codeTextArea.cut());
        copyContextMenuItem.addActionListener(e -> codeTextArea.copy());
        pasteContextMenuItem.addActionListener(e -> codeTextArea.paste());
        deleteContextMenuItem.addActionListener(e -> codeTextArea.replaceSelection(""));
        selectAllContextMenuItem.addActionListener(e -> codeTextArea.selectAll());

        // Add menu items to the context menu
        // Add Undo and Redo to the context menu
        contextMenu.add(undoContextMenuItem);
        contextMenu.add(redoContextMenuItem);
        contextMenu.addSeparator(); // Add a separator line before other items
        contextMenu.add(cutContextMenuItem);
        contextMenu.add(copyContextMenuItem);
        contextMenu.add(pasteContextMenuItem);
        contextMenu.add(deleteContextMenuItem);
        contextMenu.addSeparator(); // Add a separator line
        contextMenu.add(selectAllContextMenuItem);

        // Add mouse listener to show the context menu
        codeTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Result Text Area
        resultTextArea = new JTextArea();
        resultTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Analysis Results"));

        // Split Pane for Result and Code Text Areas (Vertical Split)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeEditorPanel, resultScrollPane);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lexicalAnalysisButton = createButton("Lexical Analysis");
        syntaxAnalysisButton = createButton("Syntax Analysis");
        semanticAnalysisButton = createButton("Semantic Analysis");

        buttonPanel.add(lexicalAnalysisButton);
        buttonPanel.add(syntaxAnalysisButton);
        buttonPanel.add(semanticAnalysisButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Disable buttons initially
        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);
        runButton.setEnabled(true);  // Enable Run button by default

        // Add Mouse Wheel Listener for zooming
        codeTextArea.addMouseWheelListener(e -> {
            if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                if (e.getWheelRotation() < 0) {
                    zoomIn();  // Zoom in when scrolling up
                } else {
                    zoomOut();  // Zoom out when scrolling down
                }
            }
        });

        // Button actions
        lexicalAnalysisButton.addActionListener(e -> performLexicalAnalysis());
        syntaxAnalysisButton.addActionListener(e -> performSyntaxAnalysis());
        semanticAnalysisButton.addActionListener(e -> performSemanticAnalysis());
        runButton.addActionListener(e -> runCode());  // Add action for Run button
    }

    // Zoom In functionality
    private void zoomIn() {
        Font currentFont = codeTextArea.getFont();
        int newSize = currentFont.getSize() + 2;
        codeTextArea.setFont(currentFont.deriveFont((float) newSize));
    }

    // Zoom Out functionality
    private void zoomOut() {
        Font currentFont = codeTextArea.getFont();
        int newSize = currentFont.getSize() - 2;
        if (newSize > 4) {  // Ensure the font size doesn't get too small
            codeTextArea.setFont(currentFont.deriveFont((float) newSize));
        }
    }

    private void restoreDefaultZoom() {
        codeTextArea.setFont(new Font("Open Sans", Font.PLAIN, 16)); // Reset to the default size
    }

    private void showAboutDialog() {
        // Create a new dialog
        JDialog aboutDialog = new JDialog(this, "About Mini Java Compiler", true);
        aboutDialog.setSize(400, 340);
        aboutDialog.setLocationRelativeTo(this); // Center the dialog relative to the parent frame

        // Create a content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add content to the panel
        JLabel titleLabel = new JLabel("Mini Java Compiler", JLabel.CENTER);
        titleLabel.setFont(new Font("Open Sans", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("<html>This Mini Java Compiler allows you to write, analyze, and execute simple Java code.<br><br>"
        + "Features include lexical, syntax, and semantic analysis.<br><br>"
        + "Built using Java Swing for GUI and supporting basic compiler functionalities.</html>",
        JLabel.CENTER);
        descriptionLabel.setFont(new Font("Open Sans", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add a OK button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setPreferredSize(new Dimension(100, 50)); // Same size as the Run button
        okButton.setFont(new Font("Open Sans", Font.PLAIN, 16)); // Same font as the Run button
        okButton.setContentAreaFilled(false); // Same transparency as the Run button
        okButton.setOpaque(true);
        okButton.setBackground(new Color(70, 130, 180)); // Same background color as the Run button
        okButton.setForeground(Color.WHITE); // White text like the Run button
        okButton.setFocusPainted(false); // Remove focus outline

        okButton.addActionListener(e -> aboutDialog.dispose());


        // Add components to the content panel
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        contentPanel.add(descriptionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        contentPanel.add(okButton);

        // Add content panel to the dialog
        aboutDialog.add(contentPanel);

        // Display the dialog
        aboutDialog.setVisible(true);
    }

    // Add this new method for running the code
    private void runCode() {
        String code = codeTextArea.getText().trim();
        if (code.isEmpty()) {
            resultTextArea.setText("No code to run.");
            return;
        }

        // Display a message indicating the start of the run process
        resultTextArea.setText("Running the code...\n");

        // Execute the code (In this case, it will just perform the analysis)
        performLexicalAnalysis();  // You can call any of the analysis methods here, or perform other actions as needed

        // After running, you can add additional actions or output here if necessary
        resultTextArea.append("\nCode execution finished.\n");
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        
        // Make the button smaller
        button.setPreferredSize(new Dimension(200, 50)); // Adjust the size as needed
        button.setFont(new Font("Open Sans", Font.PLAIN, 16)); // Adjust font size if necessary
    
        // Make the button content transparent
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        
        return button;
    }
    

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                codeTextArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    codeTextArea.append(line + "\n");
                }
                resultTextArea.setText(currentFile.getName() + " opened successfully.\n");
                lexicalAnalysisButton.setEnabled(true);
            } catch (IOException ex) {
                resultTextArea.setText("Error reading file: " + ex.getMessage());
            }
        }
    }

    

    private void performLexicalAnalysis() {
        String code = codeTextArea.getText().trim();
        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }
    
        resultTextArea.setText("");
    
        // Updated regex: Make all components optional while preserving order
        String regex = "^\\s*" +
               "(\\b(?:byte|short|int|long|float|double|boolean|char|String)\\b)?\\s*" + // Optional data type
               "([a-zA-Z_][a-zA-Z0-9_]*)?\\s*" +                                       // Optional identifier
               "(=)?\\s*" +                                                             // Optional assignment operator
               "((?:\"[^\"]*\"|'[^']*'|[^;\\s]+))?\\s*" +                               // Optional value (handles strings, chars, numbers, etc.)
               "(;)?\\s*$";                                                             // Optional delimiter

        String[] lines = code.split("\\n"); // Analyze code line-by-line
        List<Token> tokens = new ArrayList<>();
        boolean error = false;

        for (String line : lines) {
            line = line.trim(); // Remove leading/trailing whitespace
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
    
            if (matcher.matches()) {
                // Add tokens only if they are present in the line
                if (matcher.group(1) != null) {
                    tokens.add(new Token("DATA_TYPE", matcher.group(1)));
                    resultTextArea.append("DATA_TYPE: " + matcher.group(1) + "\n");
                }
                if (matcher.group(2) != null) {
                    tokens.add(new Token("IDENTIFIER", matcher.group(2)));
                    resultTextArea.append("IDENTIFIER: " + matcher.group(2) + "\n");
                }
                if (matcher.group(3) != null) {
                    tokens.add(new Token("ASSIGN_OPERATOR", matcher.group(3)));
                    resultTextArea.append("ASSIGN_OPERATOR: " + matcher.group(3) + "\n");
                }
                if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                    tokens.add(new Token("VALUE", matcher.group(4)));
                    resultTextArea.append("VALUE: " + matcher.group(4) + "\n");
                }
                if (matcher.group(5) != null && !matcher.group(5).isEmpty()) {
                    tokens.add(new Token("DELIMITER", matcher.group(5)));
                    resultTextArea.append("DELIMITER: " + matcher.group(5) + "\n");
                }

                resultTextArea.append("\n");
                
            } else {
                tokens.add(new Token("UNKNOWN", line)); // For unrecognized lines
    
                // Add the unrecognized line and stop processing
                resultTextArea.append("Analyzing line: " + line + "\n");
                resultTextArea.append("UNKNOWN: " + line + "\n\n");
                error = true;
                
            }
        }

        if(error) {
            resultTextArea.append("Error: Unknown token(s) detected." + "\nLexical Analysis Failed.");
            lexicalAnalysisButton.setEnabled(false); // Reset button states
            syntaxAnalysisButton.setEnabled(false);
        } else {
            resultTextArea.append("Lexical Analysis completed.");
            lexicalAnalysisButton.setEnabled(false);
            syntaxAnalysisButton.setEnabled(true);
        }
    
    }
    
    private void performSyntaxAnalysis() {
        String code = codeTextArea.getText();
        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }
        resultTextArea.setText(""); // Clear previous results

        String[] lines = code.split("\\n"); // Analyze code line-by-line

        boolean error = false;
        for (String line : lines) {
            try {
                line = line.trim();

                // Perform the syntax analysis
                String analysisResult = generateAnalysisResult(line);
        
                // Display the result in the resultTextArea
                resultTextArea.append(analysisResult);
                
            } catch (Exception e) {
                // If there's an error (e.g., invalid syntax), display the error message
                resultTextArea.append("Error in the line: " + line + "\n\n");
                error = true;
            }
        }
        
        if(error) {
            resultTextArea.append("Syntax error(s) detected." + "\nSyntax Analysis Failed.");
            semanticAnalysisButton.setEnabled(false);
        } else {
            // Indicate successful syntax analysis
            resultTextArea.append("Syntax Analysis completed.");
            semanticAnalysisButton.setEnabled(true);
        }

         syntaxAnalysisButton.setEnabled(false);
    }
    
    // Method for syntax analysis
    private String generateAnalysisResult(String input) throws Exception {
        // Trim any leading/trailing whitespace from input
        input = input.trim();
    
        // Updated regex to correctly capture all parts
        String regex = "^(\\b(?:byte|short|int|long|float|double|boolean|char|String)\\b)\\s+" +
                       "([a-zA-Z_][a-zA-Z0-9_]*)\\s*" +
                       "=\\s*" +
                       "([^;]+)\\s*" +
                       "(;)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();
        result.append("Analyzing line: ").append(input).append("\n");

        if (matcher.matches()) {
            String dataType = matcher.group(1); // Group 1: Data type
            String id = matcher.group(2); // Group 2: Identifier
            String assignOperator = "="; // "=" as literal
            String value = matcher.group(3); // Group 3: Value
            String delimiter = matcher.group(4); // Group 4: Semicolon delimiter
    
            // Add the result with extra spacing for readability
            
            result.append("Data Type: ").append(dataType).append("\n");
            result.append("Identifier: ").append(id).append("\n");
            result.append("Assign Operator: ").append(assignOperator).append("\n");
            result.append("Value: ").append(value).append("\n");
            result.append("Delimiter: ").append(delimiter).append("\n");
            result.append("Line Syntax Analyzed.\n").append("\n");
            return result.toString();
        } else {
            // If input does not match, throw an error
            throw new Exception("Syntax error in the source code.");  
        }
    }
    

    private void performSemanticAnalysis() {
        String code = codeTextArea.getText();
        
        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }
    
        resultTextArea.setText("");
    
        try {
            // Process the code line by line or as a whole (based on your requirements)
            String[] lines = code.split("\n");
            for (String line : lines) {
                try {
                    // Perform semantic analysis for each line of code
                    String result = semanticAnalyzer(line);
                    resultTextArea.append(result + "\n");  // Display successful semantic analysis result
                } catch (Exception e) {
                    resultTextArea.append("Error: " + e.getMessage() + "\n");  // Display error messages
                }
            }
        } catch (Exception e) {
            resultTextArea.append("Error during semantic analysis: " + e.getMessage() + "\n");
        }
    
        // Disable the semantic analysis button after analysis
        semanticAnalysisButton.setEnabled(false);
    }

    private String semanticAnalyzer(String input) throws Exception{
        Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(\".*\"|'.*'|\\S+);");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String variableName = matcher.group(1); // Variable name
            String value = matcher.group(2); // Value assigned to the variable
            
            // Determine the data type of the variable
            String dataType = input.split("\\s+")[0]; // First word is the type (e.g., int, float)

            // Type checking
            return performTypeCheck(variableName, dataType, value);
        } else {
            throw new Exception("Invalid code format: Expected 'dataType variableName = value;'");
        }
    }

    private String performTypeCheck(String variableName, String dataType, String value) throws Exception {
        switch (dataType) {
            case "byte":
                if (!value.matches("-?\\d{1,3}")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid byte.");
                }
                break;
            case "short":
                if (!value.matches("-?\\d{1,5}")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid short.");
                }
                break;
            case "int":
                if (!value.matches("-?\\d+")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid int.");
                }
                break;
            case "long":
                if (!value.matches("-?\\d+")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid long.");
                }
                break;
            case "float":
                if (!value.matches("-?\\d+\\.\\d+")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid float.");
                }
                break;
            case "double":
                if (!value.matches("-?\\d+\\.\\d+")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid double.");
                }
                break;
            case "boolean":
                if (!value.equals("true") && !value.equals("false")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid boolean.");
                }
                break;
            case "char":
                if (!value.matches("'.'")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid char.");
                }
                break;
            case "String":
                if (!value.matches("\"[^\"]*\"")) { // Ensure the value is enclosed in double quotes
                    throw new Exception("Type mismatch: '" + value + "' is not a valid String. Strings must be enclosed in double quotes.");
                }
                break;
            default:
                throw new Exception("Unknown data type: " + dataType);
        }
        return "Semantic analysis passed for variable '" + variableName + "' with value '" + value + "' and type '" + dataType + "'.";
    }
}