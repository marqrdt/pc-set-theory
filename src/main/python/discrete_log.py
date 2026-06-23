import math
from decimal import Decimal

max = 80

keyboard_prime = 89

exp = 31

for n in range(max):
    print ( int(  exp ** n ) % keyboard_prime)