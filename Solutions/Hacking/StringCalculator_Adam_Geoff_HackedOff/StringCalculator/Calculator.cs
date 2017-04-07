using System;
using System.Linq;

namespace StringCalculator
{
    public class Calculator
    {
        public int Add(string numbers)
        {
            if (numbers == "")
            {
                return 0;
            }

            if (numbers.StartsWith("//"))
            {
                return CustomDelimiters(numbers);
            }
            else
            {
                return SumNumbersText(numbers, new[] {",", "\n"});
            }
        }

        private static int CustomDelimiters(string numbers)
        {
            string firstLineText = numbers.Split('\n').First();
            string delimiter = firstLineText.Substring(2, firstLineText.Length - 2);
            if (delimiter == string.Empty) // Delimiter is a \n
            {
                string restOfText = string.Join("\n", numbers.Split('\n').Skip(1));
                return SumNumbersText(restOfText, new[] {"\n"});
            }
            else
            {
                string restOfText = numbers.Split('\n').Skip(1).Single();
                
                if (firstLineText.Contains("["))
                {
                    string[] delimiters = delimiter
                        .Split(new [] { ']'}, StringSplitOptions.RemoveEmptyEntries)
                        .Select(txt => txt.Substring(1, txt.Length - 1))
                        .ToArray()
                        ;

                    return SumNumbersText(restOfText, delimiters);
                }
                else
                {
                    return SumNumbersText(restOfText, new[] {delimiter});
                }
            }
        }

        private static int SumNumbersText(string numbers, string[] delimiters)
        {
            AssertValidNumbersText(numbers);

            var intList = numbers
                .Split(delimiters, StringSplitOptions.None)
                .Select(int.Parse)
                .Where(i => i <= 1000);

            if (intList.Any(i => i < 0))
            {
                throw new ArgumentException("Negatives not allowed");
            }

            return intList.Sum();
        }

        private static void AssertValidNumbersText(string numbers)
        {
            if (numbers.Contains(",\n"))
            {
                throw new ArgumentException("Invalud numbers text. Contains ',\\n'");
            }

            if (numbers.Contains("\n,"))
            {
                throw new ArgumentException("Invalud numbers text. Contains '\\n,'");
            }
        }
    }
}