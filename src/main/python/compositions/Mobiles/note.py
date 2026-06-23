class Note:
    def __init__(self, pitch=0, time_position=0, dynamic=0):
        self.pitch = pitch
        self.time_position = abs(time_position)
        self.dynamic = dynamic

    def get_pitch_class(self):
        return self.pitch % 12

    def __repr__(self):
        return f"Note pitch:{self.pitch} position:{self.time_position} dynamic:{self.dynamic}"


class MeasureNote:
    def __init__(self, pitch=0, time_position=0, absolute_timepoint=0, beat=0, tick=0, dynamic=0):
        self.pitch = pitch
        self.time_position = abs(time_position)
        self.absolute_timepoint = absolute_timepoint
        self.beat = beat
        self.tick = tick
        self.dynamic = dynamic

    def get_pitch_class(self):
        return self.pitch % 12

    def __repr__(self):
        return f"Measure Note pitch:{self.pitch} beat:{self.beat} tick:{self.tick} position:{self.time_position} absolute_timepoint:{self.absolute_timepoint} dynamic:{self.dynamic}"
