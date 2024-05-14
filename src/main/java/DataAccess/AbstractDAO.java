package DataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;

/**
 * The Abstract Class creates queries using Reflection, and it's extended by ClientDAO, ProductDAO and OrderDAO classes.
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * This method deletes an object by its id, using the delete query above
     * @param id
     */
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

    /**
     * @return all the content in one table
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * This method finds in the database an object based on its id
     * @param id
     * @return null if the object with the given id is not found, or the object with that id
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * This method constructs an SQL INSERT query
     * @param t is inserted in the database
     * @return t
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ");
            sb.append(type.getSimpleName()); //get table name
            sb.append(" (");


            Field[] fields = type.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                //get all fields of the object
                sb.append(fields[i].getName());
                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") VALUES (");

            //add placeholders for parameterized values
            for (int i = 0; i < fields.length; i++) {
                sb.append("?");
                if (i < fields.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");

            statement = connection.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);

            //set values for each parameter
            int cnt = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(t);
                statement.setObject(cnt++, value);
            }

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();

            //if there are generated keys, set the generated ID to the object
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                Field idField = type.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, generatedId);
            }

        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }

    /**
     * This method constructs an SQL UPDATE query
     * @param t is the one that gets updated
     * @return the updated object t
     */
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
                if (!field.getName().equals("id")) {
                    updateFields.add(field);
                }
            }
            //appends the name of the fields to be updated
            for (int i = 0; i < updateFields.size(); i++) {
                sb.append(updateFields.get(i).getName());
                sb.append(" = ?");
                if (i < updateFields.size() - 1) {
                    sb.append(", ");
                }
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

}
