#!/usr/bin/python3

# DatabaseTable by Luke Shiner (luke@lukeshiner.com)

from . databaseconnection import DatabaseConnection
from . databasecolumn import DatabaseColumn


class DatabaseTable(DatabaseConnection):
    """ Container used to provide usefull functions for accessing and
    manipulating a MySQL Table.
    """

    update_key = None
    no_update_columns = ()

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.table = kwargs['table']
        self.description = self.query("DESCRIBE " + self.table)
        self.columns = []
        for column in self.description:
            self.columns.append(DatabaseColumn(column))

    def get_all(self):
        """ Returns all data in table as a two dimensional list.  """
        return self.result_to_list(self.query("SELECT * FROM " + self.table))

    def result_to_list(self, set_):
        """ Converts a two dimensional set to a two dimensional list.  """
        newArray = []
        for row in set_:
            newArray.append(list(row))
        return newArray

    def get_columns(self):
        """ Returns a list containing the table's column names.  """
        columnArray = []
        for column in self.columns:
            columnArray.append(column.name)
        return columnArray

    def truncate(self):
        self.query("TRUNCATE " + self.table)

    def replace_from_file(self, update_file):
        self.truncate()
        self.update_from_file(update_file)

    def get_update_from_file_query(self, update_file):
        """ Updates MySQL Table with data from a .csv or a Table object.
        If self.update_key is set any record with a matching value will be
        updated. file.header or the first row of the .csv must match the table
        column names (order is not important).
        Prints a running percentage complete.
        Returns the number of records updated, then the number new records
        inserted.
        """

        query = "INSERT INTO " + self.table + " "
        query += self.get_query_columns_string() + " VALUES "
        update_rows = []
        for row in update_file.rows:
            update_rows.append(self.get_insert_query_line(row))
        query += ', '.join(update_rows)
        if self.update_key is not None:
            query += " ON DUPLICATE KEY UPDATE "
            duplicate_updates = []
            for column in self.get_columns():
                if column != self.update_key:
                    duplicate_updates.append(
                        "`" + column + "`=VALUES(`" + column + "`)")
            query += ', '.join(duplicate_updates)
        query = query + ";"

        return query

    def update_from_file(self, update_file, split_rows=0):
        print("Updating " + self.database + "." + self.table + " from file")
        if split_rows > 0:
            tables = update_file.split_by_row_count(split_rows)
            queries = []
            for table in tables:
                queries.append(self.get_update_from_file_query(table))
            for query in queries:
                self.query(query)
        else:
            self.query(self.get_update_from_file_query(update_file))

    def get_column(self, column):
        """ Uses DatabaseConnection.get_column() to return a set
        containing all values in the specified column.
        """

        return super().get_column(self.table, column)

    def get_column_as_strings(self, column):
        return super().get_column_as_strings(self.table, column)

    def get_insert_query(self, row):
        query = "INSERT INTO " + self.table + " "
        query += self.get_query_columns_string()
        query += " VALUES "
        query += self.get_insert_query_line(row)
        return query

    def get_insert_query_line(self, row):
        """ Creates an apropriate insert query to insert data from a
        TableRow into the table.
        """

        values = self.get_query_values(row)
        query = "("
        query += ', '.join(values)
        query = query + ")"
        return query

    def get_update_query_line(self, row):
        columns = self.get_query_columns()
        values = self.get_query_values(row)
        query = " SET ("
        i = 0
        while i < len(columns):
            value_string = str(columns[i]) + "=" + str(values[i])
            query += ', '.join((query, value_string))
            i += 1
        query += ") WHERE " + self.update_key + "="
        query += self.escape_string(row.get_column(self.update_key))
        return query

    def get_update_query(self, row):
        """ Creates an apropriate update query to update a record with
        data from a TableRow.
        """

        columns = self.get_query_columns()
        values = self.get_query_values(row)
        query = "UPDATE " + self.table
        i = 0
        query += self.get_update_query_line(row) + ";"
        return query

    def get_query_columns(self):
        """ Returns a list of columns required for update and insert
        statements quoted with backticks.
        """

        backtick = '`'
        columns = []
        for column in self.columns:
            if column.name not in self.no_update_columns:
                columns.append(backtick + str(column.name) + backtick)
        return columns

    def get_query_columns_string(self):
        string = '('
        columns = self.get_query_columns()
        string += ', '.join(columns)
        string += ")"
        return string

    def get_query_values(self, row):
        """ Properly formats data from a TableRow for use in a MySQL
        insert or update query.
        """

        values = []
        for column in self.columns:
            if column.name not in self.no_update_columns:
                record = row.get_column(column.name)
                if record == '' or record is None:
                        record = 'NULL'
                elif column.data_type in ('varchar', 'text'):
                    record = "'" + self.escape_string(record) + "'"
                values.append(record)
        return values

    def truncate(self):
        """ This will empty the table. Data may not be recoverable.  """
        self.query("TRUNCATE " + self.table)

    def to_table(self):
        """ Returns Table containg this table's data.  """
        try:
            from .. table . table import Table as Table
        except ImportError:
            print('Table module not found')
            return None
        table = Table()
        table.load_from_database_table(self)
        return table

    def write(self, filepath):
        """ Creates a .csv of the table's data with the specified filepath."""
        self.to_table().write(filepath)
