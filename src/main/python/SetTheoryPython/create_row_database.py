import row_utilities
from dotenv import load_dotenv
import pymongo
import logging
import os

def populate_rowform_db(hexachords=None, all_interval=False):
    env_path = '.env'
    load_dotenv(env_path)
    db_name = 'twelve_tone_rows'
    CONNECTION_STRING = f'mongodb://{os.environ["MONGODB_USER"]}:{os.environ["MONGODB_PASSWORD"]}@{os.environ["MONGODB_SERVER"]}/{db_name}'
    #CONNECTION_STRING = 'mongodb://localhost/artwork'
    #CONNECTION_STRING = 'mongodb://root:example@localhost/artwork'
    # Create a connection using MongoClient. You can import MongoClient or use pymongo.MongoClient
    from pymongo import MongoClient
    client = MongoClient(CONNECTION_STRING)
    #print( client.list_databases() )
    # Create the database for our example (we will use the same database throughout the tutorial
    dbname = client[db_name]
    collection = dbname["rowforms"]
    logger = logging.Logger("TwelveToneRow")
    load_dotenv()  # take environment variables from .env.
    row_form_enumerator = row_utilities.RowFormEnumerator()
    for row_form in row_form_enumerator.enumerate_row_forms(hexachords, filter_all_interval=all_interval):
        print(row_form)
        collection.insert_one(row_form)
    #collection.insert_many( [item_1, item_2] )



if __name__ == '__main__':
    hexachords = ['11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27',
                  '28', '29', '30', '31', '32', '33', '34', '35']

    populate_rowform_db(hexachords, False)