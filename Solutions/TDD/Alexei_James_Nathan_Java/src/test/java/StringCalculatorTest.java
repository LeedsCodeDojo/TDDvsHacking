import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringCalculatorTest {

    @Test
    void return0ForEmptyString() throws Exception {
        assertEquals(0, StringCalculator.add(""));
    }

    @Test
    void singleInts1ConvertedToInteger() {
        assertEquals(1, StringCalculator.add("1"));
    }

    @Test
    void singleInts123ConvertedToInteger() {
        assertEquals(123, StringCalculator.add("123"));
    }

    @Test
    void csvConvertedToSumIntegerForTwoNumbers() {
        assertEquals(3, StringCalculator.add("1,2"));
    }

    @Test
    void csvConvertedToSumIntegerForFourNumbers() {
        assertEquals(10, StringCalculator.add("1,2,3,4"));
    }

    @Test
    void handleNewLinesBetweenNumbers() {
        assertEquals(6, StringCalculator.add("1\n2,3"));
    }

    @Test
    void supportDifferentDelimiters() {
        assertEquals(3, StringCalculator.add("//;\n1;2"));
    }

    @TestFactory
    Stream<DynamicTest> supportsALotOfDifferentDelimiters() {
        IntStream streamOfNonDigitCodePoints = IntStream.concat(IntStream.rangeClosed(0, 47), IntStream.rangeClosed(58, 255));
        return streamOfNonDigitCodePoints.mapToObj(this::getDynamicTestForCustomDelimiter);
    }

    private DynamicTest getDynamicTestForCustomDelimiter(int codePoint) {
        String delimiter = String.valueOf(Character.toChars(codePoint));
        String strToTest = "//" + delimiter + "\n1" + delimiter + "2";
        return DynamicTest.dynamicTest(
                "Test for delimiter [" + delimiter + "], codePoint: [" + codePoint + "]",
                () -> assertEquals(3, StringCalculator.add(strToTest)));
    }

    @Test
    void callingWithNegativeNumbersThrowsException() {
        assertThrows(Exception.class, () -> StringCalculator.add("-4"));
    }

    @Test
    void ignoreOver1000() {
        assertEquals(3, StringCalculator.add("1,2,1001"));
    }

    @Test
    void allowDelimitersOfLengthGreaterThanOne() {
        assertEquals(6, StringCalculator.add("//[***]\n1***2***3"));
    }

    @Test
    void callingWithNegativeNumbersInListThrowsException() {
        assertThrows(Exception.class, () -> StringCalculator.add("1,-4"));
    }

    @Test
    void callingWithNumberGreaterThan1000InListIsIgnored() {
        assertEquals(2, StringCalculator.add("1,1001,1"));
    }
}