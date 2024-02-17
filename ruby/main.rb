# Dependencies: Ruby, Sqlite
#
# The exercise below uses the following schema
#
# CREATE TABLE retailers (id int, name text, liquor_license text, active boolean);
# CREATE TABLE retailer_users (id int, retailer_id int, user_id int, role text);
# CREATE TABLE distributors (id int, name text);
# CREATE TABLE retailer_distributors (id int, retailer_id int, distributor_id int, account_number text);
# CREATE TABLE users (id int, first_name text, last_name text);
#
# The output of this endpoint expects a list of distributors for a retailer the user belongs to
# and the output should be in JSON format.

require "sqlite3"
# require "pry"

class Distributor
  def initialize
    @db_name = "../retailer_distributors.db"
    @db = SQLite3::Database.new @db_name
    @db.results_as_hash = true
  end

  def index(user_id)
    query = <<~EOF
      SELECT
        retailers.id retailer_id,
        retailers.name retailer_name,
        retailers.liquor_license,
        retailer_distributors.account_number,
        retailer_users.id user_id,
        retailer_users.role user_role,
        distributors.id distributor_id,
        distributors.name distributor_name
      FROM retailers
      INNER JOIN retailer_users
        ON retailer_users.retailer_id = retailers.id
      INNER JOIN retailer_distributors
        ON retailer_distributors.retailer_id = retailers.id
      INNER JOIN distributors
        ON retailer_distributors.distributor_id = distributors.id
      WHERE
        retailers.active
        AND retailer_users.user_id = #{user_id}
    EOF

    @db.execute query
  end
end

# Have the output be in JSON format, Do we have relevant data for the endpoint?
puts Distributor.new.index(1)

# What happens if the input is not an id?
#puts Distributor.new.index("a number")

# How do we protect against malicious input?
#puts Distributor.new.index("1; SELECT * FROM users;--")

# What if we only want user's with an admin role to view this?
#puts Distributor.new.index(1)
#puts Distributor.new.index(2)

# Any other refactorings after above changes?
