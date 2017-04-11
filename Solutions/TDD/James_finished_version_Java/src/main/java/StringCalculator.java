import java.util.*;
import java.util.regex.*;
import static java.util.stream.Collectors.joining;

class StringCalculator {
    private static final String CHARS_TO_ESCAPE ="$()*+.?[\\^{|";

    static int add(String inputString) {
        final Matcher matcher = Pattern.compile("(//(\\D+)\n)?(.*)", Pattern.DOTALL).matcher(inputString);
        final String delimiter = getDelimiterSection(matcher);
        final String data = matcher.group(matcher.groupCount());

        return Arrays.stream(data.split("(" + delimiter + "|\n)"))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .filter(num -> num <= 1000)
                .peek(num -> {if (num < 0) throw new IllegalArgumentException("Negatives not allowed");})
                .sum();
    }

    private static String getDelimiterSection(Matcher matcher) {
        return (matcher.matches() && matcher.group(2) != null)
                ? getListOfDelimiters(matcher.group(2)).stream().collect(joining("|"))
                : ",";
    }

    private static List<String> getListOfDelimiters(String delimiterSection) {
        List<String> delimiterList = new ArrayList<>();
        Matcher m = Pattern.compile("\\[(\\D+?)\\]").matcher(delimiterSection);
        while (m.find())
            delimiterList.add(escapeDelimiter(m.group(1)));
        if (delimiterList.isEmpty())
            delimiterList.add(escapeDelimiter(delimiterSection));
        return delimiterList;
    }

    private static String escapeDelimiter(String delimiterIn) {
        return delimiterIn.codePoints()
                .mapToObj(codePoint -> String.valueOf(Character.toChars(codePoint)))
                .map(character -> (CHARS_TO_ESCAPE.contains(character) ? "\\" : "") + character)
                .collect(joining());
    }
}
