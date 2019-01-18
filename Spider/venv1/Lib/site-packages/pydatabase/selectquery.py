#!/usr/bin/python3

# SelectQuery by Luke Shiner (luke@lukeshiner.com)

from . query import Query


class SelectQuery(Query):
    """ Container used to provide usefull functions for accessing and
    manipulating a MySQL Table.
    """

    def __init__(
            self, database, table, fields, where=None, sort=None, limit=None):
        self.database = database
        self.table = table
        self.fields = fields
        self.where_clause = where
        self.sort_clause = sort
        self.limit = limit

    def __str__(self):
        query = "SELECT "
        query += ', '.join('`' + field + '`' for field in self.fields)
        query += " FROM " + self.database + "." + self.table
        query += " " + str(self.get_where_clause(self.where_clause))
        query += " " + str(self.get_sort_clause(self.sort_clause))
        if self.limit is not None:
            query += " LIMIT " + str(self.limit)
        query += ";"
        return query

    def get_sort_clause(self, sort):
        if sort is None:
            return ''
        sort_clause = "ORDER BY "
        if isinstance(sort, str):
            sort_clause += "`" + sort + "`"
        elif isinstance(sort, list):
            if isinstance(sort[0], str):
                sort_clause += ', '.join(
                    "`" + field + "`" for field in sort
                )
            elif isinstance(sort[0], list):
                clauses = []
                for clause in sort:
                    string = "`" + clause[0] + "`"
                    if len(clause) > 1:
                        string += " " + clause[1]
                    clauses.append(string)
                sort_clause += ', '.join(clauses)
        else:
            raise TypeError("sort must be string or list")
        return sort_clause
