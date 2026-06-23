import row_utilities

if __name__ == '__main__':
    row_form_enumerator = row_utilities.RowFormEnumerator()
    hexachords = None
    hexachords = [ '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31', '32', '33', '34', '35']

    for row_form in row_form_enumerator.enumerate_row_forms(hexachord_list=hexachords, filter_all_interval=False):
        print(row_form)