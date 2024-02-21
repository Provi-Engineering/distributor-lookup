/*
Dependencies: Sqlite

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Distributor {
    private final String db_name = "../retailer_distributors.db";
    private Connection db;

    public Distributor() {
        try {
            db = DriverManager.getConnection("jdbc:sqlite:" + db_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet index(String user_id) {
        String query = """
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
                AND retailer_users.user_id = """ + user_id;

        try {
            Statement stmt = db.createStatement();
            return stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Distributor distributor = new Distributor();


        // Have the output be in JSON format, Do we have relevant data for the endpoint?
        ResultSet result = distributor.index("1");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // What happens if the input is not an id?
        /*
        result = distributor.index("user_1");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        // How do we protect against malicious input?
        /*
        result = distributor.index("1; INSERT INTO users VALUES (100, 'New', 'User');--");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        // What if we only want user's with an admin role to view this?
        /*
        result = distributor.index("1");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = distributor.index("2");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
