#!/usr/bin/python3

# DatabaseTable by Luke Shiner (luke@lukeshiner.com)


class Query():
    """ Container used to provide usefull functions for accessing and
    manipulating a MySQL Table.
    """

    def __init__(self, **kwargs):
        pass

    def test_list_match(self, item_list):
        if not isinstance(item_list, list):
            raise TypeError("Can only test list object")
        list_type = type(item_list[0])
        for item in item_list:
            if not isinstance(item, list_type):
                return False
        return True

    def enforce_list_match(self, item_list):
        if not isinstance(item_list, list):
            raise TypeError("Can only test list object")
        list_type = type(item_list[0])
        for item in item_list:
            if not isinstance(item, list_type):
                raise TypeError("List must contain only on type")
                return False
        return True

    def get_where_clause(self, where):
        if isinstance(where, str):
            where_clause = where
        elif isinstance(where, dict):
            where_clause = "WHERE " + ' AND '.join(
                "`" + field + "`" + "=" + where[field] for field in where
            )
        else:
            where_clause = ''
        return where_clause
