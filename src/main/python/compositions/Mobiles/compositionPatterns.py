from fractions import Fraction


class CompositionPatterns:
    def __init__(self, in_config: dict = None, shuffle_duration_cycles=False) -> None:
        self.num_lines = 12
        self.composition_config = in_config
        self.pitch_map = self._get_pitch_map()
        self.rhythmic_cycles = self._get_rhythmic_cycles(shuffle=shuffle_duration_cycles)
        self.register_cycles = self._get_register_cycles()
        self.dynamic_cycles = self._get_dynamic_cycles()
        self.start_offsets = self._get_offsets()
        self.tick_position = self._get_tick_positions()

    def _get_tick_positions(self) -> list:
        return [
            Fraction(0, 1),
            Fraction(1, 8),
            Fraction(1, 5),
            Fraction(1, 4),
            Fraction(1, 3),
            Fraction(3, 8),
            Fraction(2, 5),
            Fraction(1, 2),
            Fraction(3, 5),
            Fraction(5, 8),
            Fraction(2, 3),
            Fraction(3, 4),
            Fraction(4, 5),
            Fraction(7, 8),
        ]

    def _get_pitch_map(self):
        if "pitch_map" in self.composition_config.keys():
            return {int(key): value for (key, value) in self.composition_config["pitch_map"].items()}
        return {}

    def _get_register_cycles(self) -> list:
        if "register_map" in self.composition_config.keys():
            return {int(key): value for (key, value) in self.composition_config["register_map"].items()}
        return {}

    def _get_rhythmic_cycles(self, shuffle=False) -> dict:
        from random import Random
        rand = Random()
        """
        map keys of the config dictionary to int()
        """
        cycles = {}
        if "rhythm_map" in self.composition_config.keys():
            cycles = {int(key): value for (key, value) in self.composition_config["rhythm_map"].items()}
        # cycles = {
        #     0: [79, 101, 103, 127, 137, 139, 151, 157],  # sum 994
        #     1: [103, 107, 109, 113, 127, 137, 149, 151],  # sum 996
        #     2: [83, 97, 101, 107, 127, 139, 163, 179],  # sum 992
        #     3: [89, 97, 101, 107, 127, 151, 163, 173],  # sum 998
        #     4: [83, 89, 97, 107, 109, 163, 173, 179],  # sum 1000
        #     5: [83, 89, 97, 103, 139, 151, 167, 173],  # sum 1002
        #     6: [89, 97, 103, 109, 131, 151, 157, 167],  # sum 1004
        #     7: [83, 89, 113, 127, 131, 149, 151, 167],  # sum 1006
        #     8: [97, 101, 103, 107, 127, 149, 157, 167],  # sum 1008
        #     9: [101, 103, 107, 113, 127, 131, 149, 179],  # sum 1010,
        #     10: [103, 107, 109, 113, 131, 137, 149, 163],  # sum 1012
        #     11: [107, 109, 113, 127, 131, 137, 139, 151],  # sum 1014
        # }
        if shuffle:
            for key in cycles:
                cycles[key] = rand.sample(cycles[key], len(cycles[key]))
        return cycles

    def _get_dynamic_cycles(self):
        if "dynamic_map" in self.composition_config.keys():
            return {int(key): value for (key, value) in self.composition_config["dynamic_map"].items()}
        return {}

    def _get_offsets(self) -> dict:
        if "offset_map" in self.composition_config.keys():
            return {int(key): value for (key, value) in self.composition_config["offset_map"].items()}
        return {}
