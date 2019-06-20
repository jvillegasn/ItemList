


import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ItemDAO {
    private static Logger log = Logger.getLogger(ItemDAO.class.getName());



    public ItemDAO() throws Exception {
       // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=W:\\ItemList\\items.mdb;";
    
    }

    public List<Item> getList() throws Exception {
        log.fine("getList called");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM ITEMS ORDER BY name");
            return saveList(resultSet);
        } finally {
            close(resultSet, statement, connection);
        }
    }

    public void addItem(Item item) throws Exception {
        log.fine("addItem called");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("Add Item (name, quantity, price, sale) VALUES (?,?,?,?)");

            int i = 1;
            statement.setString(i++, item.getName());
            statement.setInt(i++, item.getQuantity());
            statement.setDouble(i++, item.getPrice());
            statement.setBoolean(i++, item.getSale());

            statement.executeUpdate();
        } finally {
            close(null, statement, connection);
        }
    }

    public void editItem(Item item) throws Exception {
        log.fine("editItem called");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("Edit Item name = ?, quantity = ?, price = ?, sale = ? WHERE id = ?");

            int i = 1;
            statement.setString(i++, item.getName());
			statement.setInt(i++, item.getQuantity());
            statement.setDouble(i++, item.getPrice());
            statement.setBoolean(i++, item.getSale());
            statement.setInt(i++, item.getRegNum());

            statement.executeUpdate();
        } finally {
            close(null, statement, connection);
        }
    }

    public void deleteItem(Item item) throws Exception {
        log.fine("deleteItem called");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("Delete Item WHERE id = ?");

            statement.setInt(1, item.getRegNum());

            statement.executeUpdate();
        } finally {
            close(null, statement, connection);
        }
    }
    
    private List<Item> saveList(ResultSet resultSet) throws Exception {
        log.fine("saveList called");

        ArrayList<Item> list = new ArrayList<Item>();

        while (resultSet.next()) {
            Item item = new Item();
            item.setRegNum(resultSet.getInt("id"));
            item.setName(resultSet.getString("name"));
            item.setQuantity(resultSet.getInt("quantity"));
            item.setPrice(resultSet.getDouble("price"));
            item.setSale(resultSet.getBoolean("sale"));

            list.add(item);
        }//while

        return list;
    }

    private void close(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws Exception {
        log.fine("getConnection called");

        //ideally these connection would come from a connection pool...
        //return DriverManager.getConnection("jdbc:odbc:employees");
		//return DriverManager.getConnection("jdbc:odbc:MS ACCESS DataBase"+";DBQ=c:\\employees.mdb");
		return DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=W:\\ItemList\\items.mdb;");
    }
}












