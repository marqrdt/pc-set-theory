from compositionPatterns import CompositionPatterns
from musicCalculations import MusicCalculations
from note import Note


class Line:
    """
    A Line represents a sequence of events for a single Pitch Class.

    A Line maintains two lists of events: notes and measures, with each event containing a timepoint, pitch,
    and dynamic level. The 'notes' list expresses events as an abstract sequence without regard to musical notation.
    The 'measures' list expresses the events in terms of musical notation, such as time signatures,
    notated pitches, rests and dynamic markings.
    """

    def __init__(self, index: int=0, pitch_class: int=0, composition_patterns=None, length: int=0):
        self.pitch_class = pitch_class % 12
        self.index = index
        self.length = length
        if composition_patterns is None:
            raise ValueError("The value for composition_patterns may not be None. Please reconfigure the application.")
        self.composition_patterns = composition_patterns
        self.start_offset = self.composition_patterns.start_offsets[pitch_class]
        self.register_pattern = self.composition_patterns.register_cycles[self.pitch_class]
        self.duration_pattern = self.composition_patterns.rhythmic_cycles[self.pitch_class]
        self.name = f"Line {self.pitch_class}"
        self.measures = []
        self.notes = []
        self._make_notes()

    def set_name(self, name):
        self.name = name

    def _make_notes(self) -> None:
        current_timepoint = self.start_offset
        note_index = 0
        while current_timepoint <= self.length:
            curr_register = self.composition_patterns.register_cycles[self.pitch_class][
                note_index % len(self.composition_patterns.register_cycles[self.pitch_class])
            ]
            note = Note(
                pitch=MusicCalculations.get_pitch_number(
                    self.pitch_class, curr_register
                ),
                time_position=current_timepoint,
                dynamic=self.composition_patterns.dynamic_cycles[self.pitch_class][note_index % len(self.composition_patterns.dynamic_cycles[self.pitch_class])],
            )
            self.notes.append(note)
            note_index += 1
            current_timepoint = (
                current_timepoint
                + self.duration_pattern[len(self.notes) % len(self.duration_pattern)]
            )
