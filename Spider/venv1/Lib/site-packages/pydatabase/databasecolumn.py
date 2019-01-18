#!/usr/bin/python3

# DatabaseColumn by Luke Shiner (luke@lukeshiner.com)


class DatabaseColumn():
    """ Container for columns of data imported from a mysql Database.
    Used by DatabaseTable
    """

    def __init__(self, column_description):
        self.name = None
        self.data_type = None
        self.null = None
        self.name = column_description[0]
        self.data_type = self.get_data_type(column_description[1])

        if column_description[2] == 'NO':
            self.null = False
        elif column_description[2] == 'YES':
            self.null = True

    def get_data_type(self, dtype):
        """ Converts data type from mysql table description to a usable format.
        Used to properly format query statements in DatabaseTable
        """

        datatypes = ['varchar', 'int', 'text', 'float']
        for datatype in datatypes:
            if dtype[0:len(datatype)] == datatype:
                return datatype
