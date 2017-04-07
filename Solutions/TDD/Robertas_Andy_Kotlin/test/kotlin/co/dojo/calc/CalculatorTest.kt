package co.dojo.calc

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class CalculatorTest {

    @Test fun `Given empty string then should return zero`() {
        assertThat(Calculator.add(""), equalTo(0))
    }

    @Test fun `Given single integer string then returns their value`(){
        assertThat(Calculator.add("123"), equalTo(123))
    }

    @Test fun `Given 2 comma separated numbers then return their sum`(){
        assertThat(Calculator.add("1,2"), equalTo(3))
    }

    @Test fun `Given any comma separated list of numbers then returns sum of elements`(){
        assertThat(Calculator.add("1,2,3,4"), equalTo(10))
    }

    @Test fun `Given any comma or newline separated list of numbers then returns sum of elements`(){
        assertThat(Calculator.add("1,2\n3,4"), equalTo(10))
    }

    @Test(expected = IllegalValueException::class)
    fun `Given a string ending in a newline the should raise exception`(){
        Calculator.add("1,2,3,4\n")
    }

    @Test fun `Given a custom delimiter on a separate line return the sum of the elements`(){
        assertThat(Calculator.add("//;\n1;2;3"), equalTo(6))
    }

    @Test(expected = IllegalValueException::class)
    fun `Given a sequence containing a negative number Then throw exception`(){
        Calculator.add("1,-2,3,4,5")
    }

    @Test fun `Given a sequence which contains numbers greater than 1000 then these should be ignored`(){
        assertThat(Calculator.add("1,1102,4,5000"), equalTo(5))
    }

    @Test fun `Allow custom delimiters longer than a single character`(){
        assertThat(Calculator.add("//***\n1***2***3"), equalTo(6))
    }

}


