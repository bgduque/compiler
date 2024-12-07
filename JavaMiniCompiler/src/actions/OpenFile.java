package actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

public class OpenFile {

    private MainPage mainPage;

    public OpenFile() {
        this.mainPage = mainPage;
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(mainPage);
        if (result == JFileChooser.APPROVE_OPTION) {
            File currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                mainPage.getCodeTextArea().setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    mainPage.getCodeTextArea().append(line + "\n");
                }
                mainPage.getResultTextArea().setText(currentFile.getName() + " opened successfully.\n");
                mainPage.getLexicalAnalysisButton().setEnabled(true);
            } catch (IOException ex) {
                mainPage.getResultTextArea().setText("Error reading file: " + ex.getMessage());
            }
        }
    }
}