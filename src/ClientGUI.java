import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class ClientGUI {
    private JTextArea textArea1;
    private JPanel panel1;
    private JTextField composeArea;
    private JButton sendButton;
    private JTextField loginName;
    private JButton login;
    private JButton subscribe;
    private JButton unsubscribe;
    private static SocketChannel channel;
    private PrintStream out;

    public ClientGUI() {
        sendButton.setEnabled(false);
        composeArea.setEnabled(false);
        subscribe.setEnabled(false);
        unsubscribe.setEnabled(false);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(composeArea.getText().equalsIgnoreCase("quit")) {
                    HelperMethods.sendMessage(channel, "quit");
                    System.exit(0);
                }
            }
        });
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
                try {
                    channel = SocketChannel.open(address);
                    String observerName = loginName.getText();
                    HelperMethods.sendMessage(channel, observerName);

                    System.out.println("Connected to a Server");

                    login.setEnabled(false);
                    loginName.setEnabled(false);

                    sendButton.setEnabled(true);
                    composeArea.setEnabled(true);
                    subscribe.setEnabled(true);
                    unsubscribe.setEnabled(true);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        subscribe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.append("Subscribed\n\r");
                HelperMethods.sendMessage(channel, "2");
            }
        });
        unsubscribe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea1.append("Unsubscribed\n\r");
                HelperMethods.sendMessage(channel, "1");
            }
        });
    }

    public static void main(String[] args) {
        ClientGUI gui = new ClientGUI();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Client");
                try {
                    frame.setContentPane(gui.panel1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
        while (true) {
            if (channel != null) {
                gui.textArea1.append(HelperMethods.receiveMessage(channel));
            }
            gui.panel1.repaint();
        }
    }
}
