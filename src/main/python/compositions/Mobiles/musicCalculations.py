"""
Every note in the piece should have only one note per beat due to the wide spacing of the notes.
The design of the tick/rhythm dictionary is a tuple of:
(tuple notation, rest notation, note duration). For example, a rhythm of 5/4 over the span of a
quarter note expressing a 16th rest followed by a quarter note would look like this:
('\tuplet 5/4', 'r16, '8')
An 8th rest followed by an 8th note would look like;
('', 'r8', '8')
"""
tick_notation = {
    0: ("", "", "4"),
    1: ("", "r32", "8.."),
    2: ("\tuple 5/4", "r16", "4"),
    3: ("", "r16", "8."),
    4: ("\tuple 3/2", "r8", "4"),
    5: ("", "r16.", "32~ 8"),
    6: ("\tuple 5/4", "r8", "8."),
    7: ("", "r8", "8"),
    8: ("\tuple 5/4", "r8.", "8"),
    9: ("", "r8 r32", "16."),
    10: ("\tuplet 3/2", "r8", 8),
    11: ("", "r8.", "16"),
    12: ("\tuple 5/4", "r4", " 16"),
    13: ("", "r8..", "32"),
}


class MusicCalculations:
    def __init__(self):
        pass

    @staticmethod
    def get_number_of_beats(seconds=60, bpm=60):
        return (1 / (bpm / seconds)) * 60

    @staticmethod
    def get_pitch_number(pitch_class=0, register=0):
        return (pitch_class % 12) + 12 * (register + 1)

    @staticmethod
    def get_tick_notation(tick):
        return tick_notation[tick % 14]
