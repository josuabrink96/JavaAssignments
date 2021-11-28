import java.sql.ResultSetMetaData;

public interface BooksDataAccess {
    public boolean addAuthor(BooksEntry person);

    public boolean addPublisher(BooksEntry person);

    public boolean addTitle(BooksEntry person);

    public BooksTableModel getTableModel(String query);

    public void close();
}
