using System;

namespace StringCalculator
{
    class Program
    {
        static void Main(string[] args)
        {
            // This probably won't work: 
            //     //[\n]\n1\n2
            // Probably crashes?

            //Console.WriteLine(new Calculator().Add(args[0]));

            RunAndPrint("", 0); 
            RunAndPrint("123", 123); 
            RunAndPrint("1,2", 3);
            RunAndPrint("1,2,3,4", 10); 
            RunAndPrint("1\n2,3", 6); 
            RunAndPrint("//;\n1;2;3", 6); 
            RunAndPrint("1,-2", null); 
            RunAndPrint("1001,2,4",6); 
            RunAndPrint("//[***]\n1***2***3", 6); 
            RunAndPrint("//[&][%]\n1%2&3",6); 
            //RunAndPrint("//[\n]\n1\n2",3); // Above 'bug'

            Console.WriteLine();
            if (_passed)
            {
                Console.WriteLine("PASSED OVERALL");
                Console.WriteLine();
            }
            else
            {
                Console.WriteLine("****************");
                Console.WriteLine("**** FAILED ****");
                Console.WriteLine("****************");
            }
        }

        private static int _useCaseNo = 1;
        private static bool _passed = true;

        private static void RunAndPrint(string numbersText, int? expected)
        {
            Console.Write($"{_useCaseNo++} - Input: {numbersText}\t");
            try
            {
                var result = new Calculator().Add(numbersText);
                if (result == expected)
                {
                    Console.WriteLine($"= {result}");
                }
                else
                {
                    _passed = false;

                    Console.WriteLine($"**Failed**: {result}");
                }
            }
            catch (Exception e)
            {
                if (expected == null)
                {
                    Console.WriteLine($"Passed (Exception). Message: " + e.Message);
                }
                else
                {
                    _passed = false;
                    Console.WriteLine("Failed: " + e);
                }
            }
        }
    }
}