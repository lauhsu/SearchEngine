#!/usr/bin/python3

# DatabaseConnection by Luke Shiner (luke@lukeshiner.com)

import pymysql


class DatabaseConnection(object):
    """ Used as an interface with a local MySQL instance
    Requires pymysql (https://pypi.python.org/pypi/PyMySQL#downloads)
    """

    host = 'localhost'
    user = 'axevalley'
    password = 'fred'
    charset = 'utf8'

    def __init__(self, **kwargs):
        self.database = kwargs['database']
        if 'host' in kwargs:
            self.host = kwargs['host']
        if 'user' in kwargs:
            self.user = kwargs['user']
        if 'password' in kwargs:
            self.password = kwargs['password']
        if 'charset' in kwargs:
            self.charset = kwargs['charsest']

    def query(self, query):
        """ Sends query to MySQL database and returns query results.  """

        conn = pymysql.connect(host=self.host, user=self.user,
                               password=self.password, db=self.database,
                               charset=self.charset)
        cur = conn.cursor()
        try:
            cur.execute(str(query))
        except:
            print('Query Error: ')
            print(str(query))
            conn.close()
            return None
        conn.commit()
        results = cur.fetchall()
        cur.close()
        conn.close()
        return results

    def get_column(self, table, column):
        """Queries MySQL database for specified column from specified table and
        returns the data therein as a set.  """

        results = self.query("SELECT " + column + " FROM " + table)
        column = []
        for record in results:
            column.append(record[0])
        return set(column)

    def get_column_as_strings(self, table, column):
        """Queries MySQL database for specified column from specified table and
        returns the data therein as a set of strings.  """

        results = self.query("SELECT " + column + " FROM " + table)
        column = []
        for record in results:
            column.append(str(record[0]))
        return set(column)

    def escape_string(self, string):
        """Provides basic string escapes for single quote characters.  """

        newstring = str(string)
        newstring = newstring.replace("'", "\\'")
        return newstring
