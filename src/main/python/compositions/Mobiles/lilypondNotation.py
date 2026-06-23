import abjad

class LilypondNotation:
    def __init__(self):
        pass

    @staticmethod
    def get_pitch_notation(flavor="sharps"):
        if flavor == "flats":
            return [
                "c",
                "df",
                "d",
                "ef",
                "e",
                "f",
                "gf",
                "g",
                "af",
                "a",
                "bf",
                "b",
            ]
        else:
            return [
                "c",
                "cs",
                "d",
                "ds",
                "e",
                "f",
                "fs",
                "g",
                "gs",
                "a",
                "as",
                "b",
            ]

    @staticmethod
    def get_register_notation(register=4):
        register = register % 9
        if register == 0:
            return ",,,,"
        if register == 1:
            return ",,,"
        if register == 2:
            return ",,"
        if register == 3:
            return ","
        if register == 4:
            return ""
        if register == 5:
            return "'"
        if register == 6:
            return "''"
        if register == 7:
            return "'''"
        if register == 8:
            return "''''"

    def get_dynamic_notation(dynamic=4):
        dynamic = dynamic % 7
        if dynamic == 0:
            return "\\pp"
        if dynamic == 1:
            return "\\p"
        if dynamic == 2:
            return "\\mp"
        if dynamic == 3:
            return "\\mf"
        if dynamic == 4:
            return "\\f"
        if dynamic == 5:
            return "\\ff"
        if dynamic == 6:
            return "\\fff"

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
    def get_note_notation(pitch=60, dynamic=4):
        # register_tuple = LilypondNotation.get_ottava_register_notation(register=pitch // 12)
        # return f' {register_tuple[0]} {LilypondNotation.get_pitch_notation(flavor="sharps")[pitch % 12]}{register_tuple[1]}'
        return f'{LilypondNotation.get_pitch_notation(flavor="sharps")[pitch % 12]}{LilypondNotation.get_register_notation(register=pitch // 12)}'
    @staticmethod
    def get_notation(pitch=60, tick=0, ticks_per_beat= 10, clef='treble', dynamic=0):
        value_tuple = (pitch, tick % ticks_per_beat, dynamic)
        # if pitch <= 56:
        #     clef = 'bass'
        # else:
        #     clef = 'treble'
        clef_notation = f'\\clef {clef}'
        """
        Sample notations for an event inside a beat:
        \tuplet 5/4 { r16 cis''4\laissezVibrer }
        r16. fis,,32~ 8\laissezVibrer
        """
        if (ticks_per_beat == 10):
            notation_switch = {
                0: f'{clef_notation} {LilypondNotation.get_note_notation(value_tuple[0])}4{LilypondNotation.get_dynamic_notation(value_tuple[2])}\\laissezVibrer ',
                1: f'\\times 4/5 {{ {clef_notation} r16[ {LilypondNotation.get_note_notation(value_tuple[0])}4{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                2: f' {clef_notation} r16[ {LilypondNotation.get_note_notation(value_tuple[0])}8.{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                3: f'\\times 2/3 {{ {clef_notation} r8[ {LilypondNotation.get_note_notation(value_tuple[0])}4{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                4: f'\\times 4/5 {{{clef_notation}  r8[ {LilypondNotation.get_note_notation(value_tuple[0])}8.{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                5: f'{clef_notation} r8[ {LilypondNotation.get_note_notation(value_tuple[0])}8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                6: f'\\times 4/5 {{{clef_notation}  r8.[ {LilypondNotation.get_note_notation(value_tuple[0])}8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                7: f'\\times 2/3 {{ {clef_notation} r4[ {LilypondNotation.get_note_notation(value_tuple[0])}8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                8: f'{clef_notation}  r8.[ {LilypondNotation.get_note_notation(value_tuple[0])}16{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                9: f'\\times 4/5 {{ {clef_notation} r4[ {LilypondNotation.get_note_notation(value_tuple[0])}16{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
            }
        if ( ticks_per_beat == 14 ):
            notation_switch = {
                0: f'{clef_notation} { LilypondNotation.get_note_notation(value_tuple[0]) }4{LilypondNotation.get_dynamic_notation(value_tuple[2])}\\laissezVibrer ',
                1: f'{clef_notation} r32[ { LilypondNotation.get_note_notation(value_tuple[0]) }8..{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                2: f'\\times 4/5 {{ {clef_notation} r16[ { LilypondNotation.get_note_notation(value_tuple[0]) }4{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                3: f' {clef_notation} r16[ { LilypondNotation.get_note_notation(value_tuple[0]) }8.{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                4: f'\\times 2/3 {{ {clef_notation} r8[ { LilypondNotation.get_note_notation(value_tuple[0]) }4{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                5: f' {clef_notation} r16.[ { LilypondNotation.get_note_notation(value_tuple[0]) }32{LilypondNotation.get_dynamic_notation(value_tuple[2])}\\laissezVibrer r8]',
                6: f'\\times 4/5 {{{clef_notation}  r8[ { LilypondNotation.get_note_notation(value_tuple[0]) }8.{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                7: f'{clef_notation} r8[ { LilypondNotation.get_note_notation(value_tuple[0]) }8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                8: f'\\times 4/5 {{{clef_notation}  r8.[ { LilypondNotation.get_note_notation(value_tuple[0]) }8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                9: f'{clef_notation} r8[ r32 { LilypondNotation.get_note_notation(value_tuple[0]) }16.{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                10: f'\\times 2/3 {{ {clef_notation} r4[ { LilypondNotation.get_note_notation(value_tuple[0]) }8{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                11: f'{clef_notation}  r8.[ { LilypondNotation.get_note_notation(value_tuple[0]) }16{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer ',
                12: f'\\times 4/5 {{ {clef_notation} r4[ { LilypondNotation.get_note_notation(value_tuple[0]) }16{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer }} ',
                13: f'{clef_notation} r8..[ { LilypondNotation.get_note_notation(value_tuple[0]) }32{LilypondNotation.get_dynamic_notation(value_tuple[2])}]\\laissezVibrer '
        }
        return abjad.Container( notation_switch.get((value_tuple[1]), '') )
