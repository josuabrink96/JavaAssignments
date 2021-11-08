import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerGUI {
    private JTextField commentArea;
    private JButton sendButton;
    private JTextArea textArea1;
    private JPanel panel1;
    private static ExecutorService serverExecuter;
    private static Subject subject;
    private static Commentary cObject;
    private static SocketChannel socketChannel;

    public ServerGUI() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cObject.setDesc(commentArea.getText());
                textArea1.append(commentArea.getText() + "\n\r");
            }
        });
    }

    public static void main(String[] args) {
        subject = new CommentaryObject(new ArrayList<Observer>(), "Generic Soccer Match");
        cObject = ((Commentary)subject);

        serverExecuter = Executors.newCachedThreadPool();
        serverExecuter.execute(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Server");
                try {
                    frame.setContentPane(new ServerGUI().panel1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(5000));

            while (true) {
                System.out.println("Listening for a client connection");
                socketChannel = serverSocketChannel.accept();
                System.out.println("Connected to a Client");

                String observerName = HelperMethods.receiveMessage(socketChannel);

                new Thread(new SMSUsers(subject, observerName, socketChannel)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Multi-Threaded Server Terminated");
    }
}

