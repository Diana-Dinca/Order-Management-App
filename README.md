#### The current manual process of managing products, clients, and orders in a warehouse is inefficient and time-consuming. To address this issue, we can identify a system with the main entities: Clients, Products and Orders. The user is able to manage Clients and Products in the same way: he can add a new entity, update or delete an existing entity, or view the complete list of each entity. The user can place an order for a client, containing a specific product and the quantity. In this system, the main objective is to minimize the effort of the employees of a warehouse.
#### The application's implementation is organized into five distinct packages:
###### • *model package*: within this package, you'll find the Client, Product, OrderItem and Bill classes, essential for representing and manipulating the orders for a warehouse;
###### • *connection package*: contains the ConnectionFactory class, which makes the connection with SQL possible;
###### • *presentation package*: this package houses the StartInterface class, which manages the graphical user interface for managing each entity, ClientOperation and ProductOperation classes, which allow the user to perform specific changes on the entities, and OrderOperation class, which allows the user to place an order for a client, with a specific quantity of a product;
###### • *data access package*: contains the AbstractDAO class that creates queries using Reflection, and it's implemented by ClientDAO, ProductDAO, OrderDAO and BillDAO classes;
###### • *logic package*: this package includes ClientBLL, ProductBLL, OrderBLL, BillBLL classes implement all CRUD methods using DAO classes.
####  
I implemented the following methods using Reflection: *createSelectQuery*- creates a Select query for SQL, *deleteById*- deletes an object by its id, *findAll*- returns a list of all the objects in one table, *findById*- finds in the database an object based on its id, *createObjects*- manages to create objects based on a resulted set of data, *insert*- constructs an SQL INSERT query, *update*- constructs an SQL UPDATE query;
###### Here is an example of how I implemented the methods createSelectQuery, deleteById and update: 
```java
  private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
  }
  public String  deleteById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            return "Delete completed!";
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return "Delete could not be done!";
  }
  public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ");
            sb.append(type.getSimpleName());
            sb.append(" SET ");

            Field[] fields = type.getDeclaredFields();
            List<Field> updateFields = new ArrayList<>();
            for (Field field : fields) {
                //get all fields except for the id field
                if (!field.getName().equals("id")) {updateFields.add(field);}
            }
            //appends the name of the fields to be updated
            for (int i = 0; i < updateFields.size(); i++) {
                sb.append(updateFields.get(i).getName());
                sb.append(" = ?");
                if (i < updateFields.size() - 1) {sb.append(", ");}
            }
            sb.append(" WHERE id = ?");
            statement = connection.prepareStatement(sb.toString());
            //set values for the fields
            int cnt = 1;
            for (Field field : updateFields) {
                field.setAccessible(true);
                Object value = field.get(t);
                statement.setObject(cnt++, value);
            }
            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            statement.setObject(cnt, idValue);

            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
```
#### 
I've gained insights into utilizing Reflection in Java, for inspecting and manipulating classes, methods, and fields. Also, I have deepened my understanding of database design principles, SQL queries, and data manipulation techniques.
#### 
I have conducted many tests. First, I tested the validation functionality and performance by introducing invalid data. Secondly, I tested various cases on adding, updating and deleting entities, checking with the view option if the table changed in the right way.
