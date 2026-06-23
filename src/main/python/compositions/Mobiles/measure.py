from typing import TypeVar, Type
import abjad
import sys
from note import MeasureNote
from lilypondNotation import LilypondNotation
from musicXMLnotation import MusicXMLNotation
import music21
import uuid

class Measure:
    NoteType = TypeVar("MeasureNote")
    def __init__(self, measure_len=4, start_tp=0, ticks_per_beat: int=10, split_point: int=56, previous_pitch: int=-1):
        self.measure_length = measure_len
        self.start_timepoint = start_tp
        self.previous_pitch = previous_pitch
        self.split_point = split_point
        self.clef = 'treble'
        self.notes = []
        self.ticks_per_beat = ticks_per_beat

    def get_num_ticks(self):
        return self.measure_length * self.ticks_per_beat

    def time_position_getter(item):
        return item.time_position

    def make_musicxml_measure(self, measure_number: int, staff_index: int) -> music21.stream.Measure:
        """
        This method creates a music21.stream.Measure for a note in a measure.

        Right now, it really only works if there is one note per measure, which is the case for the current iteration
        of this composition paradigm. It should not be difficult to accommodate multiple notes per measure.
        :param measure_number:
        :param staff_index:
        :return: music21.stream.Measure
        """
        # half_rest = music21.note.Rest(type='half')
        measure = music21.stream.Measure(number=measure_number)
        current_beat = 1
        if len(self.notes) == 0:
            whole_rest = music21.note.Rest(type='whole')
            # whole_rest.id = str( uuid.uuid5(uuid.NAMESPACE_OID, name=f'{current_beat}') )
            measure.append(whole_rest)
            return measure
        event_id = 0
        note = self.notes[0]
        while note.time_position >= (current_beat * self.ticks_per_beat):
            quarter_rest = music21.note.Rest(type='quarter')
            measure.append(quarter_rest)
            current_beat += 1
            event_id += 1
        if note.pitch <= self.split_point:
            self.clef = 'bass'
        else:
            self.clef = 'treble'
        music_objects = MusicXMLNotation.get_notation(pitch=note.pitch, ticks_per_beat=self.ticks_per_beat, tick=note.time_position % self.ticks_per_beat)
        did_see_note = False
        for object in music_objects:
            if not object.isRest:
                if not did_see_note:
                    measure.append(music21.dynamics.Dynamic(MusicXMLNotation.get_dynamic_notation(note.dynamic)))
                    ## This could be combined into a single statement, but it's clearer for now to see how it works.
                    if note.pitch >= self.split_point and self.previous_pitch < self.split_point:
                        measure.append(MusicXMLNotation.get_clef(note.pitch, self.split_point))
                    if note.pitch < self.split_point and self.previous_pitch >= self.split_point:
                        measure.append(MusicXMLNotation.get_clef(note.pitch, self.split_point))
                else:
                    let_ring = music21.tie.Tie('let-ring')
                    object.tie = let_ring
                did_see_note = True
            measure.append(object)
        current_beat += 1
        while current_beat <= self.measure_length:
            quarter_rest = music21.note.Rest(type='quarter')
            measure.append(quarter_rest)
            current_beat += 1
        return measure
    def make_lilypond_notation(self, in_voice: abjad.Voice) -> abjad.Voice :
        # return r"""a'8 [ ( g' f' e' g, f, e, d'] """
        empty_bar_container = abjad.Container('r1 ')
        quarter_rest_container = abjad.Container('r4 ')
        measure_voice = abjad.Voice()
        if len(self.notes) == 0:
            return in_voice.append('r1')
        current_beat = 1
        #     """
        #     Fill the measure with quarter-note rests until there is a note in the current_beat
        #     """
        note = self.notes[0]
        while note.time_position >= (current_beat * self.ticks_per_beat):
            in_voice.append('r4 ')
            current_beat += 1
        rendered_tps = set()
        #for note in sorted(self.notes, key=lambda item: item.time_position):
        # for reach Measure, keep track of notes already rendered using their time_position attribute.
        if note.pitch <= 56:
            self.clef = 'bass'
        else:
            self.clef = 'treble'
        try:
            in_voice.append( LilypondNotation.get_notation(pitch=note.pitch, ticks_per_beat=self.ticks_per_beat, tick=note.time_position % self.ticks_per_beat, clef=self.clef, dynamic=note.dynamic) )
        except(abjad.exceptions.LilyPondParserError) as lppe:
            print(f' {lppe} Lilypond String:{LilypondNotation.get_notation(pitch=note.pitch, ticks_per_beat=self.ticks_per_beat, tick=note.time_position % self.ticks_per_beat, clef=self.clef, dynamic=note.dynamic)}')
            sys.exit(1)
        current_beat += 1
        # Each beat in the measure should be either a notated note or a quarter rest 'r4'.
        #for beat in range(1, self.measure_length):
        """
        Man that's ugly...
        """
        while current_beat <= self.measure_length:
            in_voice.append('r4 ')
            current_beat += 1
        return in_voice
        # return 'r4 r4 r4 r4'
    def __repr__(self):
        measure_out = ""
        measure_out += f"Measure: start_timepoint={self.start_timepoint}, Notes: "
        measure_out += ", ".join([note.__repr__() for note in self.notes])
        return measure_out
