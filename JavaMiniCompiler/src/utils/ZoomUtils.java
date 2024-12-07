package utils;

import java.awt.Font;
import javax.swing.JTextArea;

public class ZoomUtils {

    public static void zoomIn(JTextArea textArea) {
        Font currentFont = textArea.getFont();
        int newSize = currentFont.getSize() + 2;
        textArea.setFont(currentFont.deriveFont((float) newSize));
    }

    public static void zoomOut(JTextArea textArea) {
        Font currentFont = textArea.getFont();
        int newSize = currentFont.getSize() - 2;
        if (newSize > 4) {
            textArea.setFont(currentFont.deriveFont((float) newSize));
        }
    }

    public static void restoreDefaultZoom(JTextArea textArea) {
        textArea.setFont(new Font("Open Sans", Font.PLAIN, 16));
    }
}