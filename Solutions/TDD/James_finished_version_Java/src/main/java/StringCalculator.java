import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.joining;

class StringCalculator {
    private static final String DEFAULT_DELIMITER = ",";
    private static final List<Character> CHARS_TO_ESCAPE = Arrays.asList('$', '(', ')', '*', '+', '.', '?', '[', '\\', '^', '{', '|');

    static int add(String inputString) {
        return inputString.isEmpty() ? 0 : computeSumOfIntsInString(getDataAndDelimiterSeparator(inputString));
    }

    private static int computeSumOfIntsInString(DataAndDelimiterSeparator model) {
        return Arrays.stream(model.getData().split("(" + model.getDelimiter() + "|\n)"))
                .mapToInt(StringCalculator::validatedParseInt)
                .sum();
    }

    private static int validatedParseInt(String numStr) {
        int parsedIntValue = Integer.parseInt(numStr);
        if (parsedIntValue < 0)
            throw new RuntimeException("Negatives not allowed");
        return (parsedIntValue > 1000) ? 0 : parsedIntValue;
    }

    private static DataAndDelimiterSeparator getDataAndDelimiterSeparator(String inputString) {
        return hasVariableLengthDelimiter(inputString)
                ? new VariableLengthDelimiterSeparator(inputString)
                : new OneCharDelimiterSeparator(inputString);
    }

    private static boolean hasVariableLengthDelimiter(String input) {
        return input.startsWith(VariableLengthDelimiterSeparator.DELIM_START) && input.contains(VariableLengthDelimiterSeparator.DELIM_END);
    }

    private static String escapeDelimiter(String delimiterIn) {
        return delimiterIn.chars()
                .mapToObj(intChar -> (char) intChar)
                .map(character -> (CHARS_TO_ESCAPE.contains(character) ? "\\" : "") + character)
                .collect(joining());
    }

    private static abstract class DataAndDelimiterSeparator {
        String data = "";
        String delimiter = DEFAULT_DELIMITER;

        String getDelimiter() {
            return delimiter;
        }

        String getData() {
            return data;
        }
    }

    private static class VariableLengthDelimiterSeparator extends DataAndDelimiterSeparator {
        private static final String DELIM_START = "//[";
        private static final String DELIM_END = "]\n";
        private static final String DELIM_SEPARATOR = "][";

        VariableLengthDelimiterSeparator(String input) {
            int endOfDelimiterPos = input.indexOf(DELIM_END);
            data = input.substring(endOfDelimiterPos + DELIM_END.length());
            delimiter = processDelimiterSection(input.substring(DELIM_START.length(), endOfDelimiterPos));
        }

        private String processDelimiterSection(String delimiterStr) {
            return delimiterStr.contains(DELIM_SEPARATOR) ? escapeMultipleDelimiters(delimiterStr) : escapeDelimiter(delimiterStr);
        }

        private String escapeMultipleDelimiters(String inputString) {
            return Arrays.stream(inputString.split(escapeDelimiter(DELIM_SEPARATOR)))
                    .map(StringCalculator::escapeDelimiter)
                    .collect(joining("|"));
        }
    }

    private static class OneCharDelimiterSeparator extends DataAndDelimiterSeparator {
        private static final String DELIM_START = "//";
        private static final String DELIM_END = "\n";

        OneCharDelimiterSeparator(String inputString) {
            if (hasCustomOneCharDelimiter(inputString)) {
                delimiter = escapeDelimiter(inputString.substring(2, 3));
                data = inputString.substring(4);
            } else {
                data = inputString;
            }
        }

        private boolean hasCustomOneCharDelimiter(String s) {
            return s.startsWith(OneCharDelimiterSeparator.DELIM_START) && s.substring(3, 4).equals(OneCharDelimiterSeparator.DELIM_END);
        }
    }
}