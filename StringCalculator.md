# String Calculator

Create a simple String calculator with a method int Add(string numbers).

Use Cases:
1. The method should Return 0 for an empty string
2. The method can take single integer numbers, and will return them e.g. "1" -> 1, "123" -> 123 
3. Allow two numbers separated by a comma, returning their sum  - for example “1,2” -> 3
4. Allow the Add method to handle an unknown amount of numbers 
5. Allow the Add method to handle new lines between numbers (instead of commas). 
    * The following input is ok:  “1\n2,3”  (will equal 6) 
    * The following input is NOT ok:  “1,\n” 
6. Support different delimiters
    * To change a delimiter, the beginning of the string will contain a separate line that looks like this:   “//[delimiter]\n[numbers…]” for example “//;\n1;2” should return three where the default delimiter is ‘;’ .
    * The first line is optional. All existing scenarios should still be supported.
7. Calling Add with a negative number will throw an exception/exception/fail, with the message “negatives not allowed” if possible.
8. Numbers bigger than 1000 should be ignored, so adding 2 + 1001  = 2
9. Delimiters can be of any length with the following format:  “//[delimiter]\n” for example: “//[***]\n1***2***3” should return 6
10. Allow multiple delimiters like this:  “//[delim1][delim2]\n” 
    * For example “//[*][%]\n1*2%3” should return 6.
    * Make sure you can also handle multiple delimiters with length longer than one char
