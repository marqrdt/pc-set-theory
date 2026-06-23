"""
This application will calculate all All-Interval 12-tone rows
"""
from row_utilities import RowUtilities
import itertools

def all_interval_row_generator():
    aggregate = set(range(0, 12))
    max = 1000
    count = 0
    perms = itertools.permutations(range(1, 12))
    for perm in perms:
        row = [0] + [x % 12 for x in itertools.accumulate(perm)]
        if set(row) == aggregate:
            yield row

def calculate_rows(filename):
    aggregate = set(range(0, 12))
    max = 1000
    count = 0
    row_file = open(filename, 'w')
    perms = itertools.permutations(range(1, 12))
    ai_row_gen = all_interval_row_generator()
    row_utils = RowUtilities()
    for row in ai_row_gen:
        if not row_utils.is_all_interval(row):
            print(f"Row {row} is not all-interval")
        print(row)
        # row_file.write(f"{row}\n")
        count += 1
    row_file.close()
    print( f"Found {count} All-Interval rows")

if __name__ == '__main__':
    calculate_rows(filename='all_interval_rows.txt')