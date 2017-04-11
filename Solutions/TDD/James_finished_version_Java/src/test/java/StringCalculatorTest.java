import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * http://osherove.com/tdd-kata-1/
 */
class StringCalculatorTest {

    private IntPredicate nonDigits = i -> i < 48 || i > 57;
    private IntStream nonDigitCodePoints;

    @BeforeEach
    void setUp() {
        nonDigitCodePoints = IntStream.rangeClosed(0, 255).filter(nonDigits);
    }

    @TestFactory
    Stream<DynamicTest> whenPresentedWithCommaSeparatedValues_thenReturnSumofValues() throws Exception {
        Map<String, Integer> expectedList = new HashMap<>();
        expectedList.put("", 0);
        expectedList.put("1", 1);
        expectedList.put("1,2", 3);
        expectedList.put("1,2,3,4", 10);
        expectedList.put("1,2,3,4,5", 15);
        expectedList.put("11,20,33,4,400", 468);
        return expectedList.keySet().stream().map(key -> dynamicTest(key, expectedList.get(key)));
    }

    DynamicTest dynamicTest(String str, int expected) {
        return DynamicTest.dynamicTest("Test that" + str + " returns: " + expected,
                () -> assertStringSum(expected, str));
    }

    @Test
    void whenNegativeNumber_thenThrowException() {
        Throwable exception = assertThrows(Exception.class, () -> StringCalculator.add("1,-4"));
        assertEquals("Negatives not allowed", exception.getMessage());
    }

    @Test
    void whenNumberGreaterThan1000_thenIgnoreIt() {
        assertStringSum(2, "1,1001,1");
    }

    @Test
    void whenNewLinesAreBetweenNumbers_thenTreatAsDelimiter() {
        assertStringSum(15, "1\n2,3\n4,5");
    }

    @Nested
    class WhenUsingSingleCharacterCustomDelimiter {

        @Test
        void supportDifferentDelimiters() {
            assertStringSum(3, "//;\n1;2");
        }

        @TestFactory
        Stream<DynamicTest> supportALotOfDifferentDelimiters() {
            return nonDigitCodePoints.mapToObj(this::getDynamicTestForCustomDelimiter);
        }

        private DynamicTest getDynamicTestForCustomDelimiter(int codePoint) {
            String delimiter = codePointToString(codePoint);
            String strToTest = "//" + delimiter + "\n1" + delimiter + "2";
            return DynamicTest.dynamicTest(
                    "Test for delimiter [" + delimiter + "], codePoint: [" + codePoint + "]",
                    () -> assertStringSum(3, strToTest));
        }
    }

    @Nested
    class WhenUsingVariableLengthCustomDelimiter {

        @Test
        void supportVariableLengthDelimiters() {
            assertStringSum(6, "//[xxx]\n1xxx2xxx3");
        }

        @TestFactory
        Stream<DynamicTest> supportALotOfDifferentVariableDelimiters() {
            return nonDigitCodePoints.mapToObj(codePoint -> getDynamicTestForVariableDelimiter(codePoint, 3));
        }

        private DynamicTest getDynamicTestForVariableDelimiter(int codePoint, int delimiterLength) {
            String delimiter = repeatString(codePointToString(codePoint), delimiterLength);
            String strToTest = "//[" + delimiter + "]\n1" + delimiter + "2";
            return DynamicTest.dynamicTest(
                    "Test for delimiter [" + delimiter + "], codePoint: [" + codePoint + "]",
                    () -> assertStringSum(3, strToTest));
        }
    }

    @Nested
    class WhenUsingMultipleVariableLengthCustomDelimiters {

        @Test
        void supportMultipleVariableLengthDelimiters() {
            assertStringSum(6, "//[*][%]\n1*2%3");
            assertStringSum(6, "//[xxx][yyy]\n1xxx2yyy3");
            assertStringSum(10, "//[xxx][yyy][pp]\n1xxx2yyy3pp4");
        }

        @TestFactory
        Stream<DynamicTest> supportALotOfMultipleVariableLengthDelimiters() {
            int[] nonDigitCodePointsArr = nonDigitCodePoints.toArray();
            Stream.Builder<DynamicTest> testStreamBuilder = Stream.builder();
            for (int i = 0, j = nonDigitCodePointsArr.length - 1; i < nonDigitCodePointsArr.length; i++, j--) {
                testStreamBuilder.accept(getDynamicTestForTwoDelimiters(nonDigitCodePointsArr[i], nonDigitCodePointsArr[j], 3));
            }
            return testStreamBuilder.build();
        }

        private DynamicTest getDynamicTestForTwoDelimiters(int codePoint1, int codePoint2, int delimiterLength) {
            String delimiter1 = repeatString(codePointToString(codePoint1), delimiterLength);
            String delimiter2 = repeatString(codePointToString(codePoint2), delimiterLength);
            String strToTest = "//[" + delimiter1 + "][" + delimiter2 + "]\n1" + delimiter1 + "2" + delimiter2 + "3";
            return DynamicTest.dynamicTest(
                    "Test for delimiters [" + delimiter1 + "] and [" + delimiter2 + "] , codePoints: [" + codePoint1 + "] and [" +
                            codePoint2 + "]",
                    () -> assertStringSum(6, strToTest));
        }
    }

    private String repeatString(String strToRepeat, int repeatedTimes) {
        return String.join("", Collections.nCopies(repeatedTimes, strToRepeat));
    }

    private String codePointToString(int codePoint) {
        return String.valueOf(Character.toChars(codePoint));
    }

    private void assertStringSum(int expected, String input) {
        assertEquals(expected, StringCalculator.add(input));
    }
}