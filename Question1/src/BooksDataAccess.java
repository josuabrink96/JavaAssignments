
public interface BooksDataAccess {
    public boolean addAuthor( BooksEntry person );

    public boolean addPublisher( BooksEntry person );

    public boolean addTitle( BooksEntry person );

    public void close();
}
