\version "2.22.1"

global= {
  \time 4/4
  \key c \major
}

\score {
  \new StaffGroup <<
    \new Staff \with { instrumentName = "Line 1" }
    \new Voice {
        \clef treble
        r4 r4 \tuplet 5/4 { r16 cis''4\laissezVibrer } r4
        r1
        \bar "|."
    }
    \new Staff \with { instrumentName = "Line 2" }
    \new Voice {
        \clef treble
        r4 \tuplet 3/2 { r4 es''8\laissezVibrer } r4 r4
        r1
        \bar "|."
    }
    \new Staff \with { instrumentName = "Line 3" }
    \new Voice {
        \clef bass
        e2 d
        c1
        \bar "|."
    }
    \new Staff \with { instrumentName = "Line 4" }
    \new Voice {
        \clef bass
        r16. fis,,32~ 8\laissezVibrer r4 r4 r4
        r4 r4 r4 \tuplet 5/4 { r8. f,8\laissezVibrer }
        \bar "|."
    }
  >>
  \layout { }
  \midi { }
}