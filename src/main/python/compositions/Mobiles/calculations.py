def order_by( items, order ):
    out_list = [None for x in range(len(items))]
    for i in range( len(items) ):
        out_list[i] = sorted(items)[ order.index(i) ]
    return out_list

# items: [0,2,3,1,5,6,7,9,8,10]
# order: [0,1,2,3,4,5,6,7,8,9,10]


def do_calculations():
    # Use a breakpoint in the code line below to debug your script.
    import numpy as np
    import math
    import itertools

    def prime3(start=3, upto=1000):
        return list(
            filter(
                lambda num: (num % np.arange(start, 1 + int(math.sqrt(num)), 2)).all(),
                range(start, upto + 1, 2),
            )
        )

    small_primes = prime3(start=3, upto=1500)
    min = 100
    max = 200
    ordering = [6,2,3,0,5,7,4,1]
    cycle_length = len(ordering)
    prime_list = list(filter(lambda num: (num <= max and num >= min), small_primes))
    prime_combinations = itertools.combinations(prime_list, cycle_length)
    index = 0
    total = 0
    for comb in prime_combinations:
        index += 1
        if 1000 <= sum(comb) <= 1200:
            print(f"{order_by(comb, ordering)} : {sum(comb)}")
        total += sum(comb)
    print(
        f"There are {index} combinations of primes between {min} and {max} taken {cycle_length} at a time."
    )
    print(f"The average sum is {total/index}.")
    # print(len(prime_combinations))


# Press the green button in the gutter to run the script.

if __name__ == "__main__":
    do_calculations()
