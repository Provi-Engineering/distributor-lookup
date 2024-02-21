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
        ResultSet result = distributor.index("1");
        try {
            while (result.next()) {
                System.out.println(result.getString("retailer_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
