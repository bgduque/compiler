package actions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.Token;

public class PerformLexicalAnalysis {

    private String code;

    public PerformLexicalAnalysis(String code) {
        this.code = code;
    }

    public void analyze() {
        if (code.isEmpty()) {
            System.out.println("No code to analyze.");
            return;
        }

        String regex = "^\\s*" +
                "(\\b(?:byte|short|int|long|float|double|boolean|char|String)\\b)?\\s*" +
                "([a-zA-Z_][a-zA-Z0-9_]*)?\\s*" +
                "(=)?\\s*" +
                "((?:\"[^\"]*\"|'[^']*'|[^;\\s]+))?\\s*" +
                "(;)?\\s*$";

        String[] lines = code.split("\\n");
        List<Token> tokens = new ArrayList<>();
        boolean error = false;

        for (String line : lines) {
            line = line.trim();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);

            if (matcher.matches()) {
                if (matcher.group(1) != null) {
                    tokens.add(new Token("DATA_TYPE", matcher.group(1)));
                    System.out.println("DATA_TYPE: " + matcher.group(1));
                }
                if (matcher.group(2) != null) {
                    tokens.add(new Token("IDENTIFIER", matcher.group(2)));
                    System.out.println("IDENTIFIER: " + matcher.group(2));
                }
                if (matcher.group(3) != null) {
                    tokens.add(new Token("ASSIGN_OPERATOR", matcher.group(3)));
                    System.out.println("ASSIGN_OPERATOR: " + matcher.group(3));
                }
                if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                    tokens.add(new Token("VALUE", matcher.group(4)));
                    System.out.println("VALUE: " + matcher.group(4));
                }
                if (matcher.group(5) != null && !matcher.group(5).isEmpty()) {
                    tokens.add(new Token("DELIMITER", matcher.group(5)));
                    System.out.println("DELIMITER: " + matcher.group(5));
                }

                System.out.println();
            } else {
                tokens.add(new Token("UNKNOWN", line));
                System.out.println("Analyzing line: " + line);
                System.out.println("UNKNOWN: " + line + "\n");
                error = true;
            }
        }

        if (error) {
            System.out.println("Error: Unknown token(s) detected. Lexical Analysis Failed.");
        } else {
            System.out.println("Lexical Analysis completed.");
        }
    }
}