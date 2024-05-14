package DataAccess;

import Model.Bill;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.logging.Level;

import Connection.ConnectionFactory;

public class BillDAO extends AbstractDAO<Bill>  {
    /**
     * This method overrides the one from AbstractDAO, without the update of the id field.
     * @param t is inserted in the database
     * @return
     */
    public Bill insert(Bill t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ");
            sb.append(Bill.class.getSimpleName()); //get table name
            sb.append(" (");


            Field[] fields = Bill.class.getDeclaredFields();
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

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING,  Bill.class.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
}
