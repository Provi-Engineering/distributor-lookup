import sqlite3

class Distributor:
    def __init__(self):
        self.db_name = "../retailer_distributors.db"
        self.db = sqlite3.connect(self.db_name)
        self.db.row_factory = self.dict_factory

    def dict_factory(self, cursor, row):
        fields = [column[0] for column in cursor.description]
        return {key: value for key, value in zip(fields, row)}

    def index(self, user_id):
        query = """
            SELECT
                retailers.id AS retailer_id,
                retailers.name AS retailer_name,
                retailers.liquor_license,
                retailer_distributors.account_number,
                retailer_users.id AS user_id,
                retailer_users.role AS user_role,
                distributors.id AS distributor_id,
                distributors.name AS distributor_name
            FROM retailers
            INNER JOIN retailer_users
                ON retailer_users.retailer_id = retailers.id
            INNER JOIN retailer_distributors
                ON retailer_distributors.retailer_id = retailers.id
            INNER JOIN distributors
                ON retailer_distributors.distributor_id = distributors.id
            WHERE
                retailers.active
                AND retailer_users.user_id = ?
        """

        cursor = self.db.cursor()
        cursor.execute(query, (user_id,))
        return cursor

if __name__ == "__main__":
    distributor = Distributor()

    # Have the output be in JSON format, Do we have relevant data for the endpoint?
    for row in  distributor.index("1"):
        print(row)

    # What happens if the input is not an id?
    # for row in  distributor.index("user_1"):
    #    print(row)

    # How do we protect against malicious input?
    # for row in  distributor.index("1; INSERT INTO users VALUES (100, 'New', 'User');--")
    #    print(row)

    # What if we only want user's with an admin role to view this?
    # for row in  distributor.index("1"):
    #    print(row)
    # for row in  distributor.index("2"):
    #    print(row)

    # Any other refactorings after above changes?
