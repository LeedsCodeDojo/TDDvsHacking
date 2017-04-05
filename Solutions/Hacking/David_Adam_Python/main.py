#!/usr/env/python3

import re

DEFAULT_SPLIT = re.compile(r"[,\n]")
CUSTOM = re.compile(r"//(?:\[?(.+)\])+?\n")


def main(string):
    if not string:
        return 0

    matches = CUSTOM.match(string)
    if matches:
      string = string.split('\n', 1)[1]
      split_re = re.compile("(" + '|'.join(map(re.escape, matches.groups())) + ")")
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
    print("'%s' = %d" % (string, ret))

if __name__ == '__main__':
    import sys
    use('1,2,3\n4')
    use('//;\n1;2;3;4')
    use('//[;]\n1;2;3;4')
    use('//[;][$]\n1;2$3$4')
    use('//***\n1***2***3***4')
    use('//[***][^^^^]\n1***2***3^^^^4')
