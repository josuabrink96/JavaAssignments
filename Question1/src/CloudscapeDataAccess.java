import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class CloudscapeDataAccess implements BooksDataAccess {
    private Connection connection;

    private PreparedStatement sqlInsertAuthor;
    private PreparedStatement sqlInsertPublisher;
    private PreparedStatement sqlInsertAuthorISBN;
    private PreparedStatement sqlInsertTitle;

    public CloudscapeDataAccess() throws Exception {
        connect();
        sqlInsertAuthorISBN = connection.prepareStatement(
                "INSERT INTO authorisbn ( authorID, isbn )" +
                        "VALUES ( ? , ? )");

        sqlInsertAuthor = connection.prepareStatement(
                "INSERT INTO authors ( firstName, lastName ) " +
                        "VALUES ( ? , ? )");

        sqlInsertPublisher = connection.prepareStatement(
                "INSERT INTO publishers ( publisherName ) " +
                        "VALUES ( ? )");

        sqlInsertTitle = connection.prepareStatement(
                "INSERT INTO titles ( isbn, title, editionNumber, copyright, publisherID, imageFile, price)" +
                        "VALUES ( ? , ? , ? , ? , ? , ? , ? )");
    }

    private void connect() throws Exception {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/books";
        Class.forName(driver);
        connection = DriverManager.getConnection(url, "root", "root");
        connection.setAutoCommit(false);
    }

    public void close() {
        try {
            sqlInsertAuthor.close();
            sqlInsertPublisher.close();
            sqlInsertAuthorISBN.close();
            sqlInsertTitle.close();
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    protected void finalize() {
        close();
    }


    public boolean addAuthor(BooksEntry person) {
        try {
            int result;
            sqlInsertAuthor.setString(1, person.getFirstName());
            sqlInsertAuthor.setString(2, person.getLastName());
            result = sqlInsertAuthor.executeUpdate();

            if (result == 0) {
                connection.rollback();
                return false;
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addPublisher(BooksEntry person) {
        try {
            int result;
            sqlInsertPublisher.setString(1, person.getPublisherName());
            result = sqlInsertPublisher.executeUpdate();

            if (result == 0) {
                connection.rollback();
                return false;
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addTitle(BooksEntry person) {
        try {
            int result;
            sqlInsertTitle.setString(1, person.getISBN());
            sqlInsertTitle.setString(2, person.getTitle());
            sqlInsertTitle.setInt(3, person.getEditionNum());
            sqlInsertTitle.setString(4, person.getCopyright());
            sqlInsertTitle.setInt(5, person.getPublisherID());
            sqlInsertTitle.setString(6, person.getImageFile());
            sqlInsertTitle.setDouble(7, person.getPrice());
            result = sqlInsertTitle.executeUpdate();

            if (result == 0) {
                connection.rollback();
                return false;
            }
            sqlInsertAuthorISBN.setInt(1, person.getAuthorID());
            sqlInsertAuthorISBN.setString(2, person.getISBN());
            result = sqlInsertAuthorISBN.executeUpdate();

            if (result == 0) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BooksTableModel getTableModel(String query) {
        try {
            return new BooksTableModel(connection, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}