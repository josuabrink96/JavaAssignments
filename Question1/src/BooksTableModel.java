import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class BooksTableModel extends AbstractTableModel {
    private int numberOfRows;
    private Connection connection;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private Statement statement;

    public BooksTableModel(Connection con, String query) throws SQLException {
        connection = con;
        statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        setQuery(query);
    }

    public void setQuery(String query) throws SQLException {
        resultSet = statement.executeQuery(query);
        metaData = resultSet.getMetaData();

        resultSet.last();
        numberOfRows = resultSet.getRow();

        fireTableStructureChanged();
    }

    public int getRowCount() {
        return numberOfRows;
    }

    public Class getColumnClass(int column) {
        try {
            String className = metaData.getColumnClassName(column + 1);
            return Class.forName(className);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Object.class;
    }

    public int getColumnCount() {
        try {
            return metaData.getColumnCount();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return 0;
    }

    public String getColumnName(int column) {
        try {
            return metaData.getColumnName(column + 1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "";
    }

    public Object getValueAt(int row, int column) {
        try {
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return "";
    }

    protected void finalize() {
        try {
            statement.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
