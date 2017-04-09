public class StringCalculator {

    public static int add(String inputString) {
        int returnIntSum = 0;
        String inputStringWithoutDelim = inputString;

        if (!inputString.isEmpty()) {
            String defaultDel = ",";
            if (hasCustomDelimiter(inputString)) {
                defaultDel = inputString.substring(2, 3);
                inputStringWithoutDelim = inputString.substring(4);
            }
            returnIntSum = computeSumOfIntString(inputStringWithoutDelim, defaultDel);
        }
        return returnIntSum;
    }

    private static boolean hasCustomDelimiter(String s) {
        return s.startsWith("//") && s.substring(3, 4).equals("\n");
    }

    private static boolean hasVariableDelimiter(String s) {
        return s.matches("//\\[.+?\\]\n.*");
    }

    private static int computeSumOfIntString(String s, String defaultDel) {
        int sum =0;
        String[] intArray = s.split("[" + defaultDel + "\n]");
        for (String numStr : intArray) {
            int parsedIntValue = Integer.parseInt(numStr);
            if (parsedIntValue < 0) {
                throw new RuntimeException("Negatives not allowed");
            } else if (parsedIntValue < 1000) {
                sum += parsedIntValue;
            }
        }
        return sum;
    }
}
