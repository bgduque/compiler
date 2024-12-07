import javax.swing.SwingUtilities;

public class JavaMiniCompiler {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}