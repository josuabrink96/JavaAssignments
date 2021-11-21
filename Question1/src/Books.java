import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Books extends JFrame {
    private JDesktopPane desktop;
    private BooksDataAccess database;
    Action addAuthorAction, addPublisherAction, addTitleAction, exitAction, saveAction;

    public Books() {
        super( "Books" );
        try {
            database = new CloudscapeDataAccess();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JToolBar toolBar = new JToolBar();
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( 'F' );

        addAuthorAction = new AddAuthorAction();
        addPublisherAction = new AddPublisherAction();
        addTitleAction = new AddTitleAction();
        exitAction = new ExitAction();
        saveAction = new SaveAction();
        saveAction.setEnabled( false );

        toolBar.add(addAuthorAction);
        toolBar.add(addPublisherAction);
        toolBar.add(addTitleAction);
        toolBar.add(saveAction);

        fileMenu.add(exitAction);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar( menuBar );

        desktop = new JDesktopPane();
        Container c = getContentPane();
        c.add( toolBar, BorderLayout.NORTH );
        c.add( desktop, BorderLayout.CENTER );

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing( WindowEvent event )
                    {
                        shutDown();
                    }
                }
        );

        Toolkit toolkit = getToolkit();
        Dimension dimension = toolkit.getScreenSize();

        setBounds( 100, 100, dimension.width - 200, dimension.height - 200 );
        setVisible( true );
    }

    private void shutDown()
    {
        database.close();
        System.exit( 0 );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Books();
            }
        });
    }

    private class AddAuthorAction extends AbstractAction {
        public AddAuthorAction() {
            putValue(NAME,"Add Author");
            putValue(SHORT_DESCRIPTION,"Author");
            putValue(LONG_DESCRIPTION,"Add a new author entry");
            putValue(MNEMONIC_KEY,new Integer('A'));
        }
        public void actionPerformed( ActionEvent e )
        {
            BooksEntryFrame entryFrame = createBooksEntryFrame("AUTHOR");
            entryFrame.setBooksEntry(new BooksEntry() );

            desktop.add( entryFrame );
            entryFrame.setVisible( true );
        }
    }

    private class AddPublisherAction extends AbstractAction {
        public AddPublisherAction() {
            putValue(NAME,"Add Publisher");
            putValue(SHORT_DESCRIPTION,"Publisher");
            putValue(LONG_DESCRIPTION,"Add a new publisher entry");
            putValue(MNEMONIC_KEY,new Integer('P'));
        }
        public void actionPerformed( ActionEvent e )
        {
            BooksEntryFrame entryFrame = createBooksEntryFrame("PUBLISHER");
            entryFrame.setBooksEntry(new BooksEntry() );

            desktop.add( entryFrame );
            entryFrame.setVisible( true );
        }
    }

    private class AddTitleAction extends AbstractAction {
        public AddTitleAction() {
            putValue(NAME,"Add Title");
            putValue(SHORT_DESCRIPTION,"Title");
            putValue(LONG_DESCRIPTION,"Add a new title entry");
            putValue(MNEMONIC_KEY,new Integer('T'));
        }
        public void actionPerformed( ActionEvent e )
        {
            BooksEntryFrame entryFrame = createBooksEntryFrame("TITLE");
            entryFrame.setBooksEntry(new BooksEntry() );

            desktop.add( entryFrame );
            entryFrame.setVisible( true );
        }
    }

    private class ExitAction extends AbstractAction {
        public ExitAction() {
            putValue(NAME,"Exit Application");
            putValue(SHORT_DESCRIPTION,"Exit");
            putValue(LONG_DESCRIPTION,"Terminate the program");
            putValue(MNEMONIC_KEY,new Integer('X'));
        }
        public void actionPerformed( ActionEvent e )
        {
            shutDown();
        }
    }

    private class SaveAction extends AbstractAction {
        public SaveAction() {
            putValue(NAME,"Save Entry");
            putValue(SHORT_DESCRIPTION,"Save");
            putValue(LONG_DESCRIPTION,"Save data entry");
            putValue(MNEMONIC_KEY,new Integer('S'));
        }
        public void actionPerformed( ActionEvent e )
        {
            BooksEntryFrame currentFrame = ( BooksEntryFrame ) desktop.getSelectedFrame();
            BooksEntry entry = currentFrame.getBooksEntry();

            if(currentFrame.getMenu().equalsIgnoreCase("AUTHOR")) {
                database.addAuthor(entry);
            }
            else if (currentFrame.getMenu().equalsIgnoreCase("PUBLISHER")) {
                database.addPublisher(entry);
            }
            else if (currentFrame.getMenu().equalsIgnoreCase("TITLE")) {
                database.addTitle(entry);
            }
            currentFrame.dispose();
        }
    }

    private BooksEntryFrame createBooksEntryFrame(String menu)
    {
        BooksEntryFrame frame = new BooksEntryFrame(menu);
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        frame.addInternalFrameListener(new InternalFrameAdapter() {
                    public void internalFrameActivated(InternalFrameEvent event) {
                        saveAction.setEnabled( true );
                    }
                    public void internalFrameDeactivated( InternalFrameEvent event ) {
                        saveAction.setEnabled( false );
                    }
            }
        );
        return frame;
    }
}
