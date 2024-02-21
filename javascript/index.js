/*
Dependencies: Node, Sqlite

The exercise below uses the following schema

CREATE TABLE retailers (id int, name text, liquor_license text, active boolean);
CREATE TABLE retailer_users (id int, retailer_id int, user_id int, role text);
CREATE TABLE distributors (id int, name text);
CREATE TABLE retailer_distributors (id int, retailer_id int, distributor_id int, account_number text);
CREATE TABLE users (id int, first_name text, last_name text);

Refer to README.md for the contents of the database

The following program will run but has areas for improvement.
The output of index is expected to be a list of distributors for an active retailer the user belongs to.
The output of index is expected to be in a JSON format.
*/

const sqlite3 = require('sqlite3').verbose();

class Distributor {
    constructor() {
        this.db_name = '../retailer_distributors.db';
        this.db = new sqlite3.Database(this.db_name);
    }

    index(user_id) {
        const query = `
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
                AND retailer_users.user_id = ?`;

        return new Promise((resolve, reject) => {
            this.db.all(query, [user_id], (err, rows) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(rows);
                }
            });
        });
    }
}

const distributor = new Distributor();

// Have the output be in JSON format, Do we have relevant data for the endpoint?
distributor.index("1")
    .then(result => {
        console.log(result);
    })
    .catch(err => {
        console.error(err);
    });

// What happens if the input is not an id?
/*
distributor.index("user_1")
    .then(result => {
        console.log(result);
    })
    .catch(err => {
        console.error(err);
    });
*/

// How do we protect against malicious input?
/*
distributor.index("1; INSERT INTO users VALUES (100, 'New', 'User');--")
    .then(result => {
        console.log(result);
    })
    .catch(err => {
        console.error(err);
    });
*/

// What if we only want user's with an admin role to view this?
/*
distributor.index("1")
    .then(result => {
        console.log(result);
    })
    .catch(err => {
        console.error(err);
    });

distributor.index("2")
    .then(result => {
        console.log(result);
    })
    .catch(err => {
        console.error(err);
    });
*/
// Any other refactorings after above changes?
