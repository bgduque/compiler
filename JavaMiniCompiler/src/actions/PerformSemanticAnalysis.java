package actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerformSemanticAnalysis {

    private String code;

    public PerformSemanticAnalysis(String code) {
        this.code = code;
    }

    public String analyze() {
        if (code.isEmpty()) {
            return "No code to analyze.";
        }

        StringBuilder resultText = new StringBuilder();
        String[] lines = code.split("\n");

        for (String line : lines) {
            try {
                String analysisResult = semanticAnalyzer(line);
                resultText.append(analysisResult).append("\n");
            } catch (Exception e) {
                resultText.append("Error: ").append(e.getMessage()).append("\n");
            }
        }

        return resultText.toString();
    }

    private String semanticAnalyzer(String input) throws Exception {
        Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*(\".*\"|'.*'|\\S+);");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String variableName = matcher.group(1);
            String value = matcher.group(2);
            String dataType = input.split("\\s+")[0];

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
                if (!value.matches("\"[^\"]*\"")) {
                    throw new Exception("Type mismatch: '" + value + "' is not a valid String. Strings must be enclosed in double quotes.");
                }
                break;
            default:
                throw new Exception("Unknown data type: " + dataType);
        }
        return "Semantic analysis passed for variable '" + variableName + "' with value '" + value + "' and type '" + dataType + "'.";
    }
}