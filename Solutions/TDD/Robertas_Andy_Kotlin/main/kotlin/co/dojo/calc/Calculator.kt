package co.dojo.calc

object Calculator {

    fun add(value: String): Int {

        val (inputSequence, separators) = parseInputArgument(value)

        return when {
            inputSequence == "" -> 0
            inputSequence.endsWith("\n") -> throw IllegalValueException("Input cannot end with newline")
            else -> {
                val numbers = convertInputToNumbers(inputSequence, separators)
                if (numbers.any { it < 0 }) throw IllegalValueException("negatives not allowed")
                numbers.filter { it <= 1000 }.sum()
            }
        }
    }

    private fun convertInputToNumbers(inputSequence: String, separators: List<String>) = inputSequence.split(*separators.toTypedArray()).map(String::toInt)

    private fun parseInputArgument(value: String): Pair<String, List<String>> {
        val defaultSeparators = listOf(",", "\n")
        val regex = Regex("^//(.*)\\n(.*)$")  /* Regex which separate inputs into 2 lines for custom delimiters */

        return if (value.startsWith("//")) {
            val matches = regex.find(value) ?: throw IllegalValueException("Invalid custom delimiter pattern")
            val customDelimiter = matches.groupValues[1]
            val secondLine = matches.groupValues[2]
            Pair(secondLine, listOf(customDelimiter).plus(defaultSeparators))
        } else {
            Pair(value, defaultSeparators) // Default is to use whole value & standard delimiters
        }
    }

}

class IllegalValueException(message: String) : RuntimeException(message)



