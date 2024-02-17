# Dependencies

Install Sqlite on your machine

# Database Contents

```
retailers

id|     name     |liquor_license|active
--|--------------|--------------|------
1 |Franco's Tacos|    123ABC    | 1


retailer_users

id|retailer_id|user_id|role
--|-----------|-------|----
1 |        1  |   1   |admin
1 |        1  |   2   |employee
1 |        1  |   3   |admin


distributors

id|name
--|----
1 |Empire Distributing
2 |Breakthru Distributing
3 |European Imports Distributing
4 |Azteca Distributing


retailer_distributors

id|retailer_id|distributor_id|account_number
--|-----------|--------------|--------------
1 |      1    |       1      |EMP123
1 |      1    |       2      |THU123
1 |      1    |       4      |AZT3C4


users

id|first_name|last_name
--|----------|---------
1 |Franco    |Reyes
2 |Alejandro |Reyes
3 |Josephine |Reyes
```
