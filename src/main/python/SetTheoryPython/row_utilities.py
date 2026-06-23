import abjad

class RowUtilities():

    def __init__(self):
        pass

    def get_rowform_signature(self, in_row: abjad.TwelveToneRow) -> int:
        pass

    def is_all_interval(self,interval_sequence):
    #interval_sequence = [((12 + pc_sequence[n]) - pc_sequence[n - 1]) % 12 for n in range(1, len(pc_sequence))]
        return len(set(interval_sequence)) == len(interval_sequence)

    def get_interval_sequence(self, sequence):
        return [((12 + sequence[n]) - sequence[n - 1]) % 12 for n in range(1, len(sequence))]


    
class RowGenerator():
    aggregate = abjad.PitchClassSegment(items=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])

    def __init__(self):
        pass

    def randow_row(self, set_class):
        import random
        out_row = RowGenerator.aggregate.items
        random.shuffle(out_row)
        return abjad.TwelveToneRow( out_row )

'''
This Class provides one primary method, which is 'enumerate_row_forms', with parameters:
    hexachord_list : A List of Hexachord SetClasses (1-50) to use to generate row forms.
        If hexachord_list is an empty list or None, it will generate row forms for all Hexachord classes,
        effectively generating all possible unique 12-tone row forms.
    filter_all_interval: If True, only All-interval row forms will be returned.
'''
class RowFormEnumerator():
    aggregate = abjad.PitchClassSet([0,1,2,3,4,5,6,7,8,9,10,11])
    chord_types = {'trichords': 3, 'tetrachords': 4, 'pentachords': 5, 'hexachords': 6}
    row_rank = 0

    def __init__(self):
        self.aggregate = abjad.PitchClassSet(items=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
        self.chord_types = {'trichord': 3, 'tetrachord': 4, 'pentachord': 5, 'hexachord': 6}
        self.utilities = RowUtilities

    '''
    This method wraps the SetCLass and PitchClassSet classes from Abjad and ensures a consistent set of parameters 
    '''
    def get_set_class_from_items(self, items):
            sc = abjad.SetClass.from_pitch_class_set(abjad.PitchClassSet(items), transposition_only=False, lex_rank=False)
            return sc.cardinality, sc.rank

    '''
    Calculate properties of the sequence and return them as a dictionary.
    Properties include:
        'items': members of the sequence as a list of integers.
        'interval_sequence': the sequence of intervals between successive members of the sequence.
        'is_all_interval': True if intervals between successive members include every interval from 1-11 (inclusive).
        Segments:
            'trichords': a list of all trichord SetClasses contained in this sequence.
            'tetrachords': a list of all tetrachord SetClasses contained in this sequence.
            'pentachords': a list of all pentachord SetClasses contained in this sequence.
            'hexachords': a list of all hexachord SetClasses contained in this sequence.
        'ic_vector': a vector of interval classes between successive members of the sequence
            Example: row['ic_vector'][0] is the number of half-steps in the sequence.
        'pc_string': the members of the sequence expressed as a String, where 10 = 'a' and 11 = 'b'.
            Example: the sequence [0, 1, 3, 8, 6, 9, 4, 5, 10, 11, 2, 7] is  '01386945AB27'
            The value of this field should be sufficient as a 'unique key' for this sequence.
    '''
    def get_segment_data(self, segment):
        segment_data = {}
        segment_data['items'] = segment
        interval_sequence = [((12 + segment[n]) - segment[n - 1]) % 12 for n in range(1, len(segment))]
        segment_data['interval_sequence'] = interval_sequence
        segment_data['is_all_interval'] = self.utilities.is_all_interval(interval_sequence)
        icv = [0 for n in range(0, 6)]
        for chord_type in self.chord_types.keys():
            segment_data[chord_type] = []
            for sub_segment in [segment[n:n + self.chord_types[chord_type]] for n in
                                range(0, len(segment) - (self.chord_types[chord_type] - 1))]:
                segment_sc = self.get_set_class_from_items(sub_segment)
                segment_data[chord_type].append(f'{segment_sc[0]}-{segment_sc[1]}')
        for n in interval_sequence:
            icv[min(n, -n % 12) - 1] += 1
        segment_data['ic_vector'] = icv
        segment_data['pc_string'] = ''.join(["{0:x}".format(n).upper() for n in segment])
        return segment_data

    def enumerate_row_forms_for_set(self, pc_set_items, complement_set_items, is_z_related=False, filter_all_interval=False):
        import itertools
        def is_row_unique(row, all_row_forms):
            # print(f'Testing row {row}')
            row_inversion = [(12 - item) % 12 for item in row]
            # print(f'Row inversion: {row_inversion}')
            row_retrograde = [((12 - row[-1]) + item) % 12 for item in list(reversed(row))]
            # print(f'Row retrograde: {row_retrograde}')
            row_retrograde_inversion = [(row[-1] - item) % 12 for item in list(reversed(row))]
            # print(f'Row retrograde inversion: {row_retrograde_inversion}')
            if (row_inversion in all_row_forms):
                # print(f'Row Form {row} is NOT unique')
                return False
            if (row_retrograde in all_row_forms):
                # print(f'Row Form {row} is NOT unique')
                return False
            if (row_retrograde_inversion in all_row_forms):
                # print(f'Row Form {row} is NOT unique')
                return False
            # print(f'Row Form {row} is unique')
            return True
        row_rank = 0
        if is_z_related:
            if complement_set_items is None or len(complement_set_items) != 6:
                print("Complement set of Z-related hexachords must be a list of length 6")
            row_index = 0
            for set_perm_a in itertools.permutations(pc_set_items[1:]):
                segment_a = [0] + list(set_perm_a)
                if filter_all_interval and not self.utilities.is_all_interval(self.utilities.get_interval_sequence(segment_a)):
                    continue
                # If the method call is filtering on only ALl-Interval sets, we need only check the first hexachord.
                # If the first hexachord is not a unique set of intervals, i.e. has duplicates,
                # there is no way the second set could be, as it could not make up for the missing intervals.
                # This fail-fast check will eliminate a huge amount of wasted computation.
                if filter_all_interval and not self.utilities.is_all_interval(self.utilities.get_interval_sequence(segment_a)):
                    continue
                for set_perm_b in itertools.permutations(complement_set_items):
                    out_row = segment_a + list(set_perm_b)
                    if filter_all_interval and not self.utilities.is_all_interval(self.utilities.get_interval_sequence(out_row)):
                        continue
                    row_rank += 1
                    yield (segment_a + list(set_perm_b), row_rank)
        else:
            tni_row_forms = []
            for set_perm_a in itertools.permutations(pc_set_items[1:]):
                segment_a = [0] + list(set_perm_a)
                if filter_all_interval and not self.utilities.is_all_interval(self.utilities.get_interval_sequence(segment_a)):
                    continue
                # If the method call is filtering on only ALl-Interval sets, we need only check the first hexachord.
                # If the first hexachord is not a unique set of intervals, there is no way the second set could be,
                # as it could not make up for the missing intervals.
                # This fail-fast check will eliminate a huge amount of wasted computation.
                #if all_interval and not self.is_all_interval(interval_sequence):
                #    continue
                for set_perm_b in itertools.permutations(complement_set_items):
                    out_row = segment_a + list(set_perm_b)
                    if filter_all_interval and not self.utilities.is_all_interval(self.utilities.get_interval_sequence(out_row)):
                        continue
                    if is_row_unique(out_row, tni_row_forms):
                        tni_row_forms.append(out_row)
                        # print(f'TNI Row Forms database has {len(tni_row_forms)} rows')
                        row_rank += 1
                        yield (segment_a + list(set_perm_b), row_rank)

    def get_interval_sequence(self, sequence):
        return [((12 + sequence[n]) - sequence[n - 1]) % 12 for n in range(1, len(sequence))]

    def enumerate_row_forms(self, hexachord_list=None, filter_all_interval=False):
        self.row_rank = 0
        # When enumerating for all unique Hexachord Classes, only use ranks 1-36 rather than the complete set of 50.
        # The reasoning is that for Z-related Classes, each has a dual Class formed fom its complement.
        # We only need to calculate the Row Forms for the Z-related hexachords through 29, since the Classes above this
        # rank will have been included in the Retrograde form of one of the lower-numbered Classes.
        # Example: 6-Z36: [0, 1, 2, 3, 4, 7]. Its complement is 6-Z3. If we enumerate all the row forms based on
        # [{6-Z36}, {6-Z3}], all of the Retrograde forms would have been included in the enumerations for [{6-Z3}, {6-Z36}].
        if hexachord_list is None or (type(hexachord_list) == type([]) and len(hexachord_list) == 0):
            hexachord_list = range(1, 36)
        for set_class_rank in hexachord_list:
            # Get the SetClass (SC) for cardinality of 6 (hexachord) and the given SC rank.
            set_class = abjad.SetClass(6, set_class_rank, transposition_only=False, lex_rank=False)
            pc_set_items = sorted([pc.number for pc in set_class.prime_form.items])
            # Calculate the complement PCSet
            complement_pc_set = abjad.PitchClassSet(
                items=self.aggregate - set_class.prime_form,
                item_class=abjad.NumberedPitchClass
            )
            complement_set_items = sorted([pc.number for pc in complement_pc_set.items])
            # Calculate the SC of the complement set
            complement_set_class = abjad.SetClass.from_pitch_class_set( complement_pc_set )
            # If a Hexachord SC is not the same rank as its complement, it is 'Z-related'.
            # Basically, a Z-related Hexachord will never map into its complement under Tn or I (M11).
            z_related = set_class.rank != complement_set_class.rank
            row_form_count = 0
            if z_related:
                #print(f'{set_class.cardinality}-{set_class.rank}:{pc_set_items} : Complement: {complement_set_items}, Complement SetClass: {complement_set_class}')
                for row in self.enumerate_row_forms_for_set(pc_set_items, complement_set_items, is_z_related=True, filter_all_interval=filter_all_interval):
                    self.row_rank += 1
                    row_data = self.get_segment_data(row[0])
                    row_data['rank'] = self.row_rank
                    row_data['source_hexachords'] = []
                    row_data['source_hexachords'].append(f'{set_class.cardinality}-Z{set_class.rank}')
                    row_data['source_hexachords'].append(f'{complement_set_class.cardinality}-Z{complement_set_class.rank}')
                    yield row_data
            else:
                #print(f'{set_class.cardinality}-{set_class.rank}:{pc_set_items} : Complement: {complement_set_items}')
                for row in self.enumerate_row_forms_for_set(pc_set_items, complement_set_items, is_z_related=False, filter_all_interval=filter_all_interval):
                    self.row_rank += 1
                    row_data = self.get_segment_data(row[0])
                    row_data['rank'] = self.row_rank
                    row_data['source_hexachords'] = []
                    row_data['source_hexachords'].append(f'{set_class.cardinality}-{set_class.rank}')
                    yield row_data
            #print(f'{set_class} : SetClass from items: { abjad.SetClass.from_pitch_class_set( abjad.PitchClassSet(items=pc_set_items) ) }')
            #print(f'There are {row_form_count} possible 12-tone Row forms for Set Class {set_class.cardinality}-{set_class.rank}:{pc_set_items}')
