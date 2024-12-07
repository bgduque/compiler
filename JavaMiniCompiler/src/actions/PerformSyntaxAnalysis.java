package actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformSyntaxAnalysis {

    public String performSyntaxAnalysis(String code) {
        if (code.isEmpty()) {
            return "No code to analyze.";
        }

        StringBuilder result = new StringBuilder();
        String[] lines = code.split("\\n"); // Analyze code line-by-line

        boolean error = false;
        for (String line : lines) {
            try {
                line = line.trim();
                String analysisResult = generateAnalysisResult(line);
                result.append(analysisResult);
            } catch (Exception e) {
                result.append("Error in the line: ").append(line).append("\n\n");
                error = true;
            }
        }

        if (error) {
            result.append("Syntax error(s) detected.\nSyntax Analysis Failed.");
        } else {
            result.append("Syntax Analysis completed.");
        }

        return result.toString();
    }

    private String generateAnalysisResult(String input) throws Exception {
        input = input.trim();

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
            String dataType = matcher.group(1);
            String id = matcher.group(2);
            String assignOperator = "=";
            String value = matcher.group(3);
            String delimiter = matcher.group(4);

            result.append("Data Type: ").append(dataType).append("\n");
            result.append("Identifier: ").append(id).append("\n");
            result.append("Assign Operator: ").append(assignOperator).append("\n");
            result.append("Value: ").append(value).append("\n");
            result.append("Delimiter: ").append(delimiter).append("\n");
            result.append("Line Syntax Analyzed.\n\n");
            return result.toString();
        } else {
            throw new Exception("Syntax error in the source code.");
        }
    }
}