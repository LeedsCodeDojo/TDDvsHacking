#!/usr/env/python3

import re

DEFAULT_SPLIT = re.compile(r"[,\n]")
CUSTOM = re.compile(r"//(.*)\n")


def regex_real_escape(string):
    string = string.replace("\\", "\\\\")
    string = string.replace(r".", r"\.")
    string = string.replace(r"^", r"\^")
    string = string.replace(r"$", r"\$")
    string = string.replace(r"[", r"\[")
    string = string.replace(r"]", r"\]")
    string = string.replace(r"(", r"\(")
    string = string.replace(r")", r"\)")
    string = string.replace(r"{", r"\{")
    string = string.replace(r"}", r"\}")
    string = string.replace(r"*", r"\*")
    return string


def main(string):
    if not string:
        return 0

    matches = CUSTOM.match(string)
    if matches:
        delims = matches.group(1)
        if '[' in delims:
            d_matches = delims.strip('[]').split('][')
            regex = '(?:' + '|'.join(map(regex_real_escape, d_matches)) + ')'
            split_re = re.compile(regex)
        else:
            split_re = re.compile(re.escape(delims))
        string = string.split('\n', 1)[1]
    else:
        split_re = DEFAULT_SPLIT

    numbers = split_re.split(string)
    total = 0
    for number in numbers:
        val = int(number)
        if val < 0:
            raise Exception("No negative numbers!")
        if val < 1000:
            total += val

    return total


def use(string):
    ret = main(string)
    print("%r = %d" % (string, ret))

if __name__ == '__main__':
    use('1,2,3\n4')
    use('//;\n1;2;3;4')
    use('//[;]\n1;2;3;4')
    use('//[;][$]\n1;2$3$4')
    use('//***\n1***2***3***4')
    use('//[***][^^^^]\n1***2***3^^^^4')
