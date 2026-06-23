import logging
import sys
from typing import Type, TypeVar
import time

import music21.stream

from line import Line
from measure import Measure
from note import MeasureNote
from compositionPatterns import CompositionPatterns
from fractions import Fraction
import abjad
import musicxml
import logging

class CompositionManager:
    MeasureType = TypeVar("Measure")
    default_measure_length = 4
    default_ticks_per_beat = 10
    def __init__(self, composition_config: dict = None, lilypond_output_file: str = None, musicxml_output_file: str = None, length: int = 0, log_level : int = logging.WARN):
        self.length = length
        self.config = composition_config
        if self.config is None:
            print("No configuration dictionary found. Exiting.")
            sys.exit(1)
        self.measure_length = CompositionManager.default_measure_length
        self.timepoints_per_beat = CompositionManager.default_ticks_per_beat
        #if ("composition" in self.config and "measure_length" in self.config["composition"]):
        if ("composition" in self.config):
            self.measure_length = self.config["composition"]["measure_length"]
        if ("composition" in self.config and "ticks_per_beat" in self.config["composition"]):
            self.timepoints_per_beat = self.config["composition"]["ticks_per_beat"]
        self.ticks_per_measure = self.timepoints_per_beat * self.measure_length
        self.lilypond_output_file = lilypond_output_file
        self.musicxml_output_file = musicxml_output_file
        self.patterns = CompositionPatterns(in_config=composition_config, shuffle_duration_cycles=False)
        self.pc_dict = self.patterns.pitch_map
        self.rhythm_cycles = self.patterns.rhythmic_cycles
        self.register_cycles = self.patterns.register_cycles
        self.dynamic_cycles = self.patterns.dynamic_cycles
        self.delay_timer = 0.001
        self.logger = logging.Logger( __name__, log_level)
        self.lines = []
        self.score = None
        self.measures = []
        self._make_lines()
        self._make_score()

    def _make_lines(self):
        for line_index in sorted(self.pc_dict.keys()):
            pitch_class = int(self.pc_dict[line_index])
            line = Line(
                index=line_index,
                pitch_class=pitch_class,
                length=self.length,
                composition_patterns=self.patterns
            )
            self.lines.append(line)
            self._make_measures(line)

    def _make_measures(self, line):
        time_signature = Fraction(self.measure_length, 4)

        # Just in case a line doesn't have any notes, which might not be possible.
        if len(line.notes) == 0:
            self.logger.warning(f"Line {line.index} PC:{line.pitch_class} has no notes")
            return
        """
        Maintain a note_pointer index that points to Notes in the Line.
        Since we may need to peek at a Note ahead or behind the one that
        note_pointer points to, it's better to address the Notes by index
        instead of just iterating through them.
        """
        note_pointer = 0
        current_measure_timepoint = 0
        current_note_timepoint = 0
        new_measure = None
        measure_timepoints = set()
        while note_pointer < len(line.notes):
            current_measure_index = 0
            current_note_timepoint = line.notes[note_pointer].time_position
            note_pointer_advanced = False
            self.logger.debug(
                f"Line:Line {line.index} PC:{line.pitch_class} current_note_timepoint at note_pointer {note_pointer} is {current_note_timepoint}"
            )
            # while current_note_timepoint > current_measure_timepoint:
            for n in range(0, (current_note_timepoint - current_measure_timepoint) // self.ticks_per_measure):
                # print(f'#### Line:{line_index} current_measure_timepoint {current_measure_timepoint} is less than current_note_timepoint {current_note_timepoint}')
                new_measure = Measure(measure_len=self.measure_length, ticks_per_beat=self.timepoints_per_beat, start_tp=current_measure_timepoint)
                # if (new_measure.start_timepoint) not in measure_timepoints:
                line.measures.append(new_measure)
                self.logger.debug(f"Appended Empty Measure {new_measure}")
                current_measure_timepoint += self.ticks_per_measure
            # if new_measure is None:
            new_measure = Measure(measure_len=self.measure_length, ticks_per_beat=self.timepoints_per_beat, start_tp=current_measure_timepoint)
            # new_measure = Measure(measure_len=4, start_tp=current_measure_timepoint)
            self.logger.debug(
                f"==== Line {line.index} PC:{line.pitch_class} current_measure_timepoint at note_pointer {note_pointer} is {current_measure_timepoint} ===="
            )
            # current_measure_timepoint += new_measure.get_num_ticks()
            self.logger.debug(
                f'>>>> Before note creation block: {current_measure_timepoint + self.ticks_per_measure} > {current_note_timepoint}: {current_measure_timepoint + self.ticks_per_measure > current_note_timepoint}')
            while current_measure_timepoint + self.ticks_per_measure > current_note_timepoint:
                note_to_add = line.notes[note_pointer]
                self.logger.debug(
                    f"++++ Line {line.index} PC:{line.pitch_class} Note at index {note_pointer} is {line.notes[note_pointer]} ++++"
                )
                measure_note = MeasureNote(
                    pitch=note_to_add.pitch,
                    time_position=note_to_add.time_position
                                  - new_measure.start_timepoint,
                    absolute_timepoint=note_to_add.time_position,
                    dynamic=note_to_add.dynamic,
                    beat=0,
                    tick=0,
                )
                new_measure.split_point = 56
                if (note_pointer > 0):
                    previous_note = line.notes[note_pointer - 1]
                    new_measure.previous_pitch = previous_note.pitch
                note_pointer += 1
                note_pointer_advanced = True
                new_measure.notes.append(measure_note)
                self.logger.debug(
                    f"Line:Line {line.index} PC:{line.pitch_class} Appended '{measure_note}' to measure at {new_measure.start_timepoint} in line {line.index}")
                if note_pointer >= len(line.notes):
                    break
                time.sleep(self.delay_timer)
                current_note_timepoint = line.notes[note_pointer].time_position

            # if (new_measure.start_timepoint) not in measure_timepoints:
            measure_timepoints.add(new_measure.start_timepoint)
            line.measures.append(new_measure)
            self.logger.debug(f"Line:Line {line.index} PC:{line.pitch_class} Appended {new_measure}")
            current_measure_timepoint += new_measure.get_num_ticks()
            if not note_pointer_advanced:
                note_pointer += 1
            time.sleep(self.delay_timer)
            if note_pointer >= len(line.notes):
                break

    def _make_score(self):
        score_preamble = r"""
        \\version "2.24.0
        \language "english"
        #(set-global-staff-size 14)
        \\header {
            composer = Paul Marquardt
            title = Mobile 1
        }
        """
        line_index = 0
        staves = []
        self.musicxml_score = music21.stream.Score(title="Mobiles Score")
        for line in self.lines:
            current_timepoint = 0
            current_measure_index = 0
            voice = abjad.Voice(name=f'Voice {line_index}')
            musicxml_part = music21.stream.Part(id="part{:03d}".format(line_index))
            for measure in line.measures:
                # notation_string = measure.get_notation()
                # print(notation_string)
                # staff.extend(notation_string)
                #measure.get_notation(in_staff=staff)
                musicxml_measure = measure.make_musicxml_measure(current_measure_index, line_index + 1)
                musicxml_part.append([musicxml_measure])
                measure.make_lilypond_notation(in_voice=voice)
                # if current_measure_index == 0:
                #     markup = abjad.Markup(f"Line {line_index}")
                #     line_name = abjad.InstrumentName(markup)
                #     abjad.attach(line_name, staff[0])
                current_measure_index += 1
            self.musicxml_score.append(musicxml_part)
            staff = abjad.Staff([voice], name=f"Line {line_index:02d}")
            staves.append(staff)
            #self.musicxml_score.add_child(musicxml_part)
            #self.score.append(staff)
            line_index += 1
        self.score = abjad.Score(staves, name="Mobiles Score")
        # print(abjad.lilypond(self.score))
        lilypond_file = abjad.LilyPondFile([self.score])
        if self.lilypond_output_file is not None:
            lilypond_string = abjad.lilypond(lilypond_file)
            try:
                output_file = open(self.lilypond_output_file, "w")
                output_file.write(lilypond_string)
                output_file.close()
                abjad.persist.as_midi(self.score, "test.midi")
            except OSError as ose:
                self.logger.critical(ose)
        abjad.show(lilypond_file)
        if self.musicxml_output_file is not None:
            self.musicxml_score.write( fmt='musicxml', fp=self.musicxml_output_file)
            #self.musicxml_score.show()