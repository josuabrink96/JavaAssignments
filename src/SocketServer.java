import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {
    private static final Logger logger = Logger.getLogger(SocketServer.class.getName());

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(5000));
            String message = "";

            logger.log(Level.INFO, "Waiting for client");
            SocketChannel socketChannel = serverSocketChannel.accept();

            while (true) {
                message = HelperMethods.receiveMessage(socketChannel);
                try(FileChannel fileChannel = FileChannel.open(Paths.get("src/server/" + message))) {
                    HelperMethods.sendMessage(socketChannel, "SUCCESS");
                    ByteBuffer bytes = ByteBuffer.allocate((int)fileChannel.size());
                    while(fileChannel.read(bytes) > 0) {
                        bytes.flip();
                        socketChannel.write(bytes);
                        bytes.clear();
                    }
                    fileChannel.close();
                }
                catch(IOException ex) {
                    if(message.equalsIgnoreCase("quit")) {
                        logger.log(Level.INFO, "Closing connection");
                        HelperMethods.sendMessage(socketChannel, "QUIT");
                        socketChannel.close();
                        break;
                    }
                    else {
                        HelperMethods.sendMessage(socketChannel, "FAIL");
                    }
                }
                message = "";
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
