# vertx-sql-assist
Read this in other languages: [English](./README.md), [简体中文](./README.zh.md).

vertx-sql-assist is the SQL operation help tool of [Vert.x](https://vertx.io/), which provides the support of insert, delete, update, query, join, paging query, cooperate with SqlAssist help class, it basically does not need to write a line of SQL code.

We recommend that you use [ScrewDriver](https://github.com/MirrenTools/screw-driver) to generate code, so you will find the world very beautiful!
## Dependencies
To use vertx-sql-assist, add the following dependency to the dependencies section of your build descriptor

``` XML
<dependency>
  <groupId>org.mirrentools</groupId>
  <artifactId>vertx-sql-assist</artifactId>
  <version>RELEASE</version>
</dependency>
```
## SQL class method description
* **getCount** Get the total number of data rows
* **selectAll** Query multiple rows of data
* **limitAll** Paging query
* **selectById** Query data by ID
* **selectByObj** Query data by attributes that are not empty in the object
* **selectSingleByObj** Query the first row of data returned by data withdrawal through the attribute not empty in the object
* **insertBatch** Batch add insert
* **insertAll** Insert an object including a value with a null property value
* **insertNonEmpty** Insert an object, only the property whose value is not null
* **replace** Insert an object. If the object does not exist, create a new one. If the object already exists, update it
* **updateAllById** Update all properties in an object, including null value, if it is the primary key value in the object
* **updateAllByAssist** Update all properties in an object including null value, by SqlAssist
* **updateNonEmptyById** Update a non null value of a property in an object, by ID
* **updateNonEmptyByAssist** Update a non null value of a property in an object, by SqlAssist
* **updateSetNullById** Set the column to null by ID
* **updateSetNullByAssist** Set the column to null by SqlAssist
* **deleteById** Delete by ID
* **deleteByAssist** Delete by SqlAssist
* **queryAsObj** Execution query result is JsonObject
* **queryAsListObj** Execution query result is JsonArray
* **queryAsListArray** Execution query result is JsonArray
* **update** Execution update result is number of affected rows
* **batch** Batch Execution

## SqlAssist方法说明
* **setOrders** Set OrderBy with SqlAssist.order(column,mode)
* **setGroupBy** Set GroupBy
* **setHaving** Set Having
* **setDistincts** Set distinct or not, true De duplicate
* **setPage** Set the page number. The value is only valid in the limitAll method , finally will be converted to startRow
* **setStartRow** Set data start row
* **setRowSize** Set how many rows of data to get
* **setResultColumn** Set to return column, Multiple columns are separated by ,
* **setJoinOrReference** Set join query or multi table query statement
* **and** Add and condition
* **or** Add or condition
* **andEq** Add and equal condition
* **orEq** Add or equal condition
* **andNeq** Add and not equal condition
* **orNeq** Add or not equal condition
* **andLt** Add and less than condition
* **orLt** Add or less than condition
* **andLte** Add and less than or equal to condition
* **orLte** Add or less than or equal to condition
* **andGt** Add and greater than condition
* **orGt** Add or greater than condition
* **andGte** Add and greater than or equal to condition
* **orGte** Add or greater than or equal to condition
* **andLike** Add and like condition
* **orLike** Add or like condition
* **andNotLike** Add and not like condition
* **orNotLike** Add or not like condition
* **andIsNull** Add and is null condition
* **orIsNull** Add or is null condition
* **andIsNotNull** Add and is not null condition
* **orIsNotNull** Add or is not null condition
* **setConditions** Add query condition
* **customCondition** Add custom query condition

## SqlAssist Use example
[Example project](https://github.com/shenzhenMirren/vertx-sql-assist-examples)

``` java
// (1)Create SqlAssist
SqlAssist assist = new SqlAssist();
// (2)Add condition type=1 or 3,equivalent to SQL: where type=1 or type=3
assist.orEq("type", 1).orEq("type", 3);
// (3)Eliminating duplicate data ,equivalent to SQL: select distinct ...
assist.setDistincts(true);
// (4)Custom return column only [id,type,name,seq]
assist.setResultColumn("id,type,name,seq");
// (5)order by seq desc
assist.setOrders(SqlAssist.order("seq", false));
// (6)To get the data of lines 20-35 in the database, you can also use setpage (page number) to get the data by page,equivalent to SQL: limit 20,15
assist.setStartRow(20).setRowSize(15);
// (7)Execute get data
Future future=Promise.promise().future();
future.setHandler(//Processed results);
itemsSQL.selectAll(assist,future);
```
For more help, see the method notes for SqlAssist class


## How use it?

**Example**

1.Create entity class

``` java
@Table("table name")
public class User {
  @TableId("primary key")
  private Long id;
  @TableColumn("column name")
  private String name;
  @TableColumn(value = "column name", alias = "column name,is not required")
  private Integer type;
  //Other necessary
}  
```
2.Create SQL class and  extends CommonSQL

``` java
public class UserSQL extends CommonSQL<User,JDBCClient> {//(1)
	public UserSQL(SQLExecute<JDBCClient> execute) {
		super(execute);
	}
  //(1)
  //The User must be an entity class annotation with @Table, @TableId, @TableColumn
  //JDBCClient can be another database client
  //Override other methods
}  
```
3.Execute

``` java
public static void main(String[] args) {
  // Other necessary
  UserSQL userSQL = new UserSQL(SQLExecute.createJDBC(jdbcClient));
  // Query Example
  // Create SqlAssist
  SqlAssist assist = new SqlAssist();
  assist.setStartRow(0).setRowSize(15);
  assist.andEq("type", 1);
  assist.setOrders(SqlAssist.order("id", true));
  // Execution query
  userSQL.selectAll(assist,res->{
    if (res.succeeded()) {
      System.out.println(res.result());
    }else {
      System.err.println(res.cause());
    }
  });
  //Save Example
  User user =new User();
  user.setId(1001L);
  user.setName("org.mirrentools");
  user.setType(1);
  userSQL.insertNonEmpty(user,res->{//Processed results});
  
}
```
## common setting
**Set different database SQL statements** SQL statement uses MySQL standard statement by default,You can set different database SQL statements through SQLStatement,support : MySQL、PostgreSQL、Oracle、DB2、SQL Server、SQLite,For example, if you set it to Oracle, you can:
``` java
SQLStatement.register(OracleStatementSQL.class);
```
**Set limit data return result name** The default value of data return result name is:totals=Total data rows,pages=Total data pages ,page=Current page number,size=How many rows of data are displayed per page,data=Result data,If you want to change the data result name to something else, you can:
``` java
SqlLimitResult.registerResultKey("totals", "counts");
```
