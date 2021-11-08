import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {
    private static final Logger logger = Logger.getLogger(SocketClient.class.getName());

    public static void main(String[] args) {
        SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
        try (SocketChannel socketChannel = SocketChannel.open(address)) {
            logger.log(Level.INFO, "Connected to Server");

            boolean running = true;

            while(running)
            {
                String message = "";
                Scanner pathScanner = new Scanner(System.in);
                logger.log(Level.INFO, "Enter file name you wish to transfer");
                String filePath = pathScanner.nextLine();

                HelperMethods.sendMessage(socketChannel, filePath);

                String fileContent = "";

                message = HelperMethods.receiveMessage(socketChannel);
                if(message.equalsIgnoreCase("SUCCESS"))
                {
                    logger.log(Level.INFO, "File found. Transferring contents\n\r");
                    ByteBuffer bytes = ByteBuffer.allocate(4096);
                    while(socketChannel.read(bytes) > 0) {
                        bytes.flip();
                        while(bytes.hasRemaining()) {
                            fileContent += (char) bytes.get();
                        }
                        bytes.clear();
                        bytes.flip();
                    }
                    Files.write(Paths.get("src/client/" + filePath), fileContent.getBytes());
                    logger.log(Level.INFO, "File found. Transferring contents\n\r");
                }
                else if(message.equalsIgnoreCase("FAIL"))
                {
                    logger.log(Level.INFO, "File not found. Please try again");
                }
                else if(message.equalsIgnoreCase("QUIT"))
                {
                    logger.log(Level.INFO, "Closing connection");
                    running = false;
                    socketChannel.close();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
