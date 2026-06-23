import abjad
import music21
from fractions import Fraction
class MusicXMLNotation:
    def __init__(self):
        pass

    @staticmethod
    def add_ottava_spanner(pitch, note):
        if (pitch > 95):
            os = music21.spanner.Ottava()
            os.type = '8va'
            os.addSpannedElements([note])

    @staticmethod
    def get_pitch_notation(flavor="sharps"):
        if flavor == "flats":
            return [
                "c",
                "d-",
                "d",
                "e-",
                "e",
                "f",
                "g-",
                "g",
                "a-",
                "a",
                "b-",
                "b",
            ]
        else:
            return [
                "c",
                "c#",
                "d",
                "d#",
                "e",
                "f",
                "f#",
                "g",
                "g#",
                "a",
                "a#",
                "b",
            ]

    @staticmethod
    def get_register_notation(register=4):
        register = register % 9
        if register == 0:
            return "0"
        if register == 1:
            return "1"
        if register == 2:
            return "2"
        if register == 3:
            return "3"
        if register == 4:
            return "4"
        if register == 5:
            return "5"
        if register == 6:
            return "6"
        if register == 7:
            return "7"
        if register == 8:
            return "8"

    @staticmethod
    def get_clef(pitch=60, split_point=60):
        if pitch < split_point:
            return music21.clef.BassClef()
        else:
            return music21.clef.TrebleClef()
    def get_dynamic_notation(dynamic=4):
        dynamic = dynamic % 8
        if dynamic == 0:
            return "ppp"
        if dynamic == 1:
            return "pp"
        if dynamic == 2:
            return "p"
        if dynamic == 3:
            return "mp"
        if dynamic == 4:
            return "mf"
        if dynamic == 5:
            return "f"
        if dynamic == 6:
            return "ff"
        if dynamic == 7:
            return "fff"

    """
       Return a tuple of the Ottava string and the pitch register
    """
    @staticmethod
    def get_ottava_register_notation(register=4):
        #register = register % 8
        if register == 0:
            return '\\ottava #1\n', ",,,"
        if register == 1:
            return '\\ottava #0\n', ",,,"
        if register == 2:
            return '\\ottava #0\n', ",,"
        if register == 3:
            return '\\ottava #0\n', ","
        if register == 4:
            return '\\ottava #0\n', ""
        if register == 5:
            return '\\ottava #0\n', "'"
        if register == 6:
            return '\\ottava #0\n', "''"
        if register == 7:
            return '\\ottava #-1\n', "''"
        if register == 8:
            return '\\ottava #-1\n', "'''"

    @staticmethod
    def get_notation(pitch=60, tick=0, ticks_per_beat=14) -> []:
        value_tuple = (pitch, tick % 14)
        out_values = []
        # if pitch <= 56:
        #     clef = 'bass'
        # else:
        #     clef = 'treble'
        if ticks_per_beat == 8:
            if tick == 0:
                note = music21.note.Note(pitch, type='quarter')
                note.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, note)
                out_values.append(note)
            if tick == 1:
                r15 = music21.note.Rest(Fraction(1, 5))
                r15.quarterLength = Fraction(1, 5)
                n45 = music21.note.Note(pitch, typeOrDuration=Fraction(4,5))
                n45.quarterLength = Fraction(4, 5)
                n45.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n45)
                out_values.append(r15)
                out_values.append(n45)
            if tick == 2:
                r14 = music21.note.Rest(Fraction(1, 4))
                r14.quarterLength = Fraction(1, 4)
                n34 = music21.note.Note(pitch, type='eighth', dots=1)
                n34.quarterLength = Fraction(3, 4)
                n34.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n34)
                out_values.append(r14)
                out_values.append(n34)
            if tick == 3:
                r25 = music21.note.Rest(Fraction(2, 5))
                r25.quarterLength = Fraction(2, 5)
                n35 = music21.note.Note(pitch, typeOrDuration=Fraction(3, 5))
                n35.quarterLength = Fraction(3, 5)
                n35.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n35)
                out_values.append(r25)
                out_values.append(n35)
            if tick == 4:
                r24 = music21.note.Rest(type='eighth')
                r24.quarterLength = Fraction(1, 2)
                n24 = music21.note.Note(pitch, type='eighth')
                n24.quarterLength = Fraction(1, 2)
                n24.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n24)
                out_values.append(r24)
                out_values.append(n24)
            if tick == 5:
                r35 = music21.note.Rest(Fraction(3, 5))
                r35.quarterLength = Fraction(3, 5)
                n25 = music21.note.Note(pitch, typeOrDuration=Fraction(2, 5))
                n25.quarterLength = Fraction(2, 5)
                n25.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n25)
                out_values.append(r35)
                out_values.append(n25)
            if tick == 6:
                r34 = music21.note.Rest(Fraction(3, 4))
                r34.quarterLength = Fraction(3, 4)
                n14 = music21.note.Note(pitch, type='16th')
                n14.quarterLength = Fraction(1, 4)
                n14.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n14)
                out_values.append(r34)
                out_values.append(n14)
            if tick == 7:
                r45 = music21.note.Rest(Fraction(4, 5))
                r45.quarterLength = Fraction(4, 5)
                n15 = music21.note.Note(pitch, typeOrDuration=Fraction(1, 5))
                n15.quarterLength = Fraction(1, 5)
                n15.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n15)
                out_values.append(r45)
                out_values.append(n15)
        if ticks_per_beat == 10:
            if tick == 0:
                note = music21.note.Note(pitch, type='quarter')
                note.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, note)
                out_values.append(note)
            if tick == 1:
                r15 = music21.note.Rest(Fraction(1, 5))
                r15.quarterLength = Fraction(1, 5)
                n45 = music21.note.Note(pitch, typeOrDuration=Fraction(4,5))
                n45.quarterLength = Fraction(4, 5)
                n45.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n45)
                out_values.append(r15)
                out_values.append(n45)
            if tick == 2:
                r14 = music21.note.Rest(Fraction(1, 4))
                r14.quarterLength = Fraction(1, 4)
                n34 = music21.note.Note(pitch, type='eighth', dots=1)
                n34.quarterLength = Fraction(3, 4)
                n34.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n34)
                out_values.append(r14)
                out_values.append(n34)
            if tick == 3:
                r13 = music21.note.Rest(Fraction(1, 3))
                r13.quarterLength = Fraction(1, 3)
                n23 = music21.note.Note(pitch, typeOrDuration=Fraction(2,3))
                n23.quarterLength = Fraction(2, 3)
                n23.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n23)
                out_values.append(r13)
                out_values.append(n23)
            if tick == 4:
                r25 = music21.note.Rest(Fraction(2, 5))
                r25.quarterLength = Fraction(2, 5)
                n35 = music21.note.Note(pitch, typeOrDuration=Fraction(3, 5))
                n35.quarterLength = Fraction(3, 5)
                n35.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n35)
                out_values.append(r25)
                out_values.append(n35)
            if tick == 5:
                r24 = music21.note.Rest(type='eighth')
                r24.quarterLength = Fraction(1, 2)
                n24 = music21.note.Note(pitch, type='eighth')
                n24.quarterLength = Fraction(1, 2)
                n24.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n24)
                out_values.append(r24)
                out_values.append(n24)
            if tick == 6:
                r35 = music21.note.Rest(Fraction(3, 5))
                r35.quarterLength = Fraction(3, 5)
                n25 = music21.note.Note(pitch, typeOrDuration=Fraction(2, 5))
                n25.quarterLength = Fraction(2, 5)
                n25.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n25)
                out_values.append(r35)
                out_values.append(n25)
            if tick == 7:
                r23 = music21.note.Rest(Fraction(2, 3))
                r23.quarterLength = Fraction(2, 3)
                n13 = music21.note.Note(pitch, typeOrDuration=Fraction(1,3))
                n13.quarterLength = Fraction(1, 3)
                MusicXMLNotation.add_ottava_spanner(pitch, n13)
                n13.tie = music21.tie.Tie('let-ring')
                out_values.append(r23)
                out_values.append(n13)
            if tick == 8:
                r34 = music21.note.Rest(Fraction(3, 4))
                r34.quarterLength = Fraction(3, 4)
                n14 = music21.note.Note(pitch, type='16th')
                n14.quarterLength = Fraction(1, 4)
                n14.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, n14)
                out_values.append(r34)
                out_values.append(n14)
            if tick == 9:
                r45 = music21.note.Rest(Fraction(4, 5))
                r45.quarterLength = Fraction(4, 5)
                n15 = music21.note.Note(pitch, typeOrDuration=Fraction(1, 5))
                n15.quarterLength = Fraction(1, 5)
                MusicXMLNotation.add_ottava_spanner(pitch, n15)
                n15.tie = music21.tie.Tie('let-ring')
                out_values.append(r45)
                out_values.append(n15)
        if ticks_per_beat == 14:
            if tick == 0:
                note = music21.note.Note(pitch, type='quarter')
                note.tie = music21.tie.Tie('let-ring')
                MusicXMLNotation.add_ottava_spanner(pitch, note)
                out_values.append(note)
            if tick == 1:
                r18 = music21.note.Rest(type='32nd')
                r18.quarterLength = Fraction(1, 8)
                n78 = music21.note.Note(pitch, type='eighth', dots=2)
                n78.quarterLength = Fraction(7, 8)
                n78.tie = music21.tie.Tie('let-ring')
                out_values.append(r18)
                out_values.append(n78)
            if tick == 2:
                r15 = music21.note.Rest(Fraction(1, 5))
                r15.quarterLength = Fraction(1, 5)
                n45 = music21.note.Note(pitch, typeOrDuration=Fraction(4,5))
                n45.quarterLength = Fraction(4, 5)
                n45.tie = music21.tie.Tie('let-ring')
                out_values.append(r15)
                out_values.append(n45)
            if tick == 3:
                r14 = music21.note.Rest(Fraction(1, 4))
                r14.quarterLength = Fraction(1, 4)
                n34 = music21.note.Note(pitch, type='eighth', dots=1)
                n34.quarterLength = Fraction(3, 4)
                n34.tie = music21.tie.Tie('let-ring')
                out_values.append(r14)
                out_values.append(n34)
            if tick == 4:
                r13 = music21.note.Rest(Fraction(1, 3))
                r13.quarterLength = Fraction(1, 3)
                n23 = music21.note.Note(pitch, typeOrDuration=Fraction(2,3))
                n23.quarterLength = Fraction(2, 3)
                n23.tie = music21.tie.Tie('let-ring')
                out_values.append(r13)
                out_values.append(n23)
            if tick == 5:
                r38 = music21.note.Rest(type='16th', dots=1)
                r38.quarterLength = Fraction(3, 8)
                n18 = music21.note.Note(pitch, type='32nd')
                n18.quarterLength = Fraction(1, 8)
                n18.tie = music21.tie.Tie('start')
                n48 = music21.note.Note(pitch, type='eighth')
                n48.tie = music21.tie.Tie('let-ring')
                n48.tie = music21.tie.Tie('stop')
                out_values.append(r38)
                out_values.append(n18)
                out_values.append(n48)
            if tick == 6:
                r25 = music21.note.Rest(Fraction(2, 5))
                r25.quarterLength = Fraction(2, 5)
                n35 = music21.note.Note(pitch, typeOrDuration=Fraction(3, 5))
                n35.quarterLength = Fraction(3, 5)
                n35.tie = music21.tie.Tie('let-ring')
                out_values.append(r25)
                out_values.append(n35)
            if tick == 7:
                r24 = music21.note.Rest(type='eighth')
                r24.quarterLength = Fraction(1, 2)
                n24 = music21.note.Note(pitch, type='eighth')
                n24.quarterLength = Fraction(1, 2)
                n24.tie = music21.tie.Tie('let-ring')
                out_values.append(r24)
                out_values.append(n24)
            if tick == 8:
                r35 = music21.note.Rest(Fraction(3, 5))
                r35.quarterLength = Fraction(3, 5)
                n25 = music21.note.Note(pitch, typeOrDuration=Fraction(2, 5))
                n25.quarterLength = Fraction(2, 5)
                n25.tie = music21.tie.Tie('let-ring')
                out_values.append(r35)
                out_values.append(n25)
            if tick == 9:
                r12 = music21.note.Rest(type='eighth')
                r12.quarterLength = Fraction(2, 4)
                r18 = music21.note.Rest(type='32nd')
                r18.quarterLength = Fraction(1, 8)
                n38 = music21.note.Note(pitch, type='16th', dots=1)
                n38.tie = music21.tie.Tie('let-ring')
                n38.quarterLength = Fraction(3, 8)
                out_values.append(r12)
                out_values.append(r18)
                out_values.append(n38)
            if tick == 10:
                r23 = music21.note.Rest(Fraction(2, 3))
                r23.quarterLength = Fraction(2, 3)
                n13 = music21.note.Note(pitch, typeOrDuration=Fraction(1,3))
                n13.quarterLength = Fraction(1, 3)
                n13.tie = music21.tie.Tie('let-ring')
                out_values.append(r23)
                out_values.append(n13)
            if tick == 11:
                r34 = music21.note.Rest(Fraction(3, 4))
                r34.quarterLength = Fraction(3, 4)
                n14 = music21.note.Note(pitch, type='16th')
                n14.quarterLength = Fraction(1, 4)
                n14.tie = music21.tie.Tie('let-ring')
                out_values.append(r34)
                out_values.append(n14)
            if tick == 12:
                r45 = music21.note.Rest(Fraction(4, 5))
                r45.quarterLength = Fraction(4, 5)
                n15 = music21.note.Note(pitch, typeOrDuration=Fraction(1, 5))
                n15.quarterLength = Fraction(1, 5)
                n15.tie = music21.tie.Tie('let-ring')
                out_values.append(r45)
                out_values.append(n15)
            if tick == 13:
                r78 = music21.note.Rest(type='eighth', dots=2)
                r78.quarterLength = Fraction(7, 8)
                n18 = music21.note.Note(pitch, type='32nd')
                n18.quarterLength = Fraction(1, 8)
                n18.tie = music21.tie.Tie('let-ring')
                out_values.append(r78)
                out_values.append(n18)
        return out_values
