from . databaseconnection import DatabaseConnection
from . databasetable import DatabaseTable


def connect(**kwargs):
    database_connection = DatabaseConnection(**kwargs)
    return database_connection


def get_table(**kwargs):
    database_table = DatabaseTable(**kwargs)
    return database_table
