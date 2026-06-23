import abjad

score = abjad.Score("{ c,4 r4 r4 r4 r16 c'8. r4 r4 r4 r1 { 4/5 r8. c''''8 } r4 r4 r4 r1 r8 r32 c16. r4 r4 r4 r1 r1 { 4/5 r8 c'''8. } r4 r4 r4 r1 r16. c''''32 r8 r4 r4 r4 r1 r1 { 4/5 r16 c''4 } r4 r4 r4 r1 r1 r16. c,32 r8 r4 r4 r4 c'4 r4 r4 r4 } { r1 r16 cs,8. r4 r4 r4 r1 { 4/5 r4 cs''16. } r4 r4 r4 r1 r8 r32 cs''''16. r4 r4 r4 r1 { 2/3 r4 cs'''8 } r4 r4 r4 r1 r8. cs16 r4 r4 r4 r1 { 4/5 r8. cs''''8 } r4 r4 r4 r1 r1 r16 cs'8. r4 r4 r4 r1 r1 cs,4 r4 r4 r4 }", name='Mobiles Score', simultaneous=True))
print(abjad.lilypondfile(score))