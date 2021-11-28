
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class BooksEntryFrame extends JInternalFrame {
    private HashMap fields;
    private BooksEntry books;
    private JPanel leftPanel, rightPanel;
    private String menu;
    private static int xOffset = 0, yOffset = 0;

    private static final String FIRST_NAME = "First Name",
            LAST_NAME = "Last Name", PUBLISHER_NAME = "Publisher Name",
            ISBN = "ISBN", TITLE = "Title", COPYRIGHT = "Copyright",
            IMAGE_FILE = "Image File", EDITION_NUM = "Edition Number", PRICE = "Price",
            AUTHOR_ID = "Author ID", PUBLISHER_ID = "Publisher ID";

    public BooksEntryFrame(String menu) {
        super("Book Entry", true, true);
        this.menu = menu;
        fields = new HashMap();
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        leftPanel.setLayout(new GridLayout(9, 1, 0, 5));
        rightPanel.setLayout(new GridLayout(9, 1, 0, 5));

        if (menu.equalsIgnoreCase("AUTHOR")) {
            super.title = "Author Entry";
            createRow(FIRST_NAME);
            createRow(LAST_NAME);
        } else if (menu.equalsIgnoreCase("PUBLISHER")) {
            super.title = "Publisher Entry";
            createRow(PUBLISHER_NAME);
        } else if (menu.equalsIgnoreCase("TITLE")) {
            super.title = "Title Entry";
            createRow(AUTHOR_ID);
            createRow(ISBN);
            createRow(TITLE);
            createRow(EDITION_NUM);
            createRow(COPYRIGHT);
            createRow(PUBLISHER_ID);
            createRow(IMAGE_FILE);
            createRow(PRICE);
        }

        Container container = getContentPane();
        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);

        setBounds(xOffset, yOffset, 300, 300);
        xOffset = (xOffset + 30) % 300;
        yOffset = (yOffset + 30) % 300;
    }

    public void setBooksEntry(BooksEntry entry) {
        books = entry;

        if (menu.equalsIgnoreCase("AUTHOR")) {
            setField(FIRST_NAME, books.getFirstName());
            setField(LAST_NAME, books.getLastName());
        } else if (menu.equalsIgnoreCase("PUBLISHER")) {
            setField(PUBLISHER_NAME, books.getPublisherName());
        } else if (menu.equalsIgnoreCase("TITLE")) {
            setField(AUTHOR_ID, String.valueOf(books.getAuthorID()));
            setField(ISBN, books.getISBN());
            setField(TITLE, books.getTitle());
            setField(EDITION_NUM, String.valueOf(books.getEditionNum()));
            setField(COPYRIGHT, books.getCopyright());
            setField(PUBLISHER_ID, String.valueOf(books.getPublisherID()));
            setField(IMAGE_FILE, books.getImageFile());
            setField(PRICE, String.valueOf(books.getPrice()));
        }
    }

    public BooksEntry getBooksEntry() {
        if (menu.equalsIgnoreCase("AUTHOR")) {
            books.setFirstName(getField(FIRST_NAME));
            books.setLastName(getField(LAST_NAME));
        } else if (menu.equalsIgnoreCase("PUBLISHER")) {
            books.setPublisherName(getField(PUBLISHER_NAME));
        } else if (menu.equalsIgnoreCase("TITLE")) {
            books.setAuthorID(Integer.parseInt(getField(AUTHOR_ID)));
            books.setISBN(getField(ISBN));
            books.setTitle(getField(TITLE));
            books.setEditionNum(Integer.parseInt(getField(EDITION_NUM)));
            books.setCopyright(getField(COPYRIGHT));
            books.setPublisherID(Integer.parseInt(getField(PUBLISHER_ID)));
            books.setImageFile(getField(IMAGE_FILE));
            books.setPrice(Double.parseDouble(getField(PRICE)));
        }
        return books;
    }

    private void setField(String fieldName, String value) {
        JTextField field = (JTextField) fields.get(fieldName);
        field.setText(value);
    }

    private String getField(String fieldName) {
        JTextField field = (JTextField) fields.get(fieldName);
        return field.getText();
    }

    private void createRow(String name) {
        JLabel label = new JLabel(name, SwingConstants.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.add(label);
        JTextField field = new JTextField(30);
        rightPanel.add(field);
        fields.put(name, field);
    }

    public String getMenu() {
        return menu;
    }
}
