import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Description
 *
 * @author smx
 * @date 2020/12/2020/12/22
 */
public class NioServer {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);// 设置非阻塞，底层调用内核的非阻塞
        ServerSocket serverSocket = channel.socket();
        serverSocket.bind(new InetSocketAddress(8080));


    }
}
