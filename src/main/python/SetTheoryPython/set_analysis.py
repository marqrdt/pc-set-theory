import os
import abjad
import argparse
import re

def analyze_sets(in_file):
    set_regex = re.compile(r'\[*([^\]]+)\]*')
    for line in in_file:
        matcher = set_regex.search(line)
        if matcher is not None:
            # print(f'matched {matcher.group(1)}')
            pc_list = [int(x) for x in matcher.group(1).split(',')]
            print(pc_list)

    in_file.close()

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Collect command-line arguments")
    parser.add_argument('--in', dest='in_filename')
    parser.add_argument('--out', dest='out_filename')
    args = parser.parse_args()
    in_file = None
    try:
        in_file = open(args.in_filename)
    except (OSError, IOError) as e:
        print(f'Unable to open file {args.in_filename}: {e}')
    analyze_sets(in_file)