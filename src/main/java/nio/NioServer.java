package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 同步非阻塞io，单线程
 * 解决了产生大量线程的问题
 * 缺陷：会不停的遍历看是否有连接和数据进来，造成cpu资源浪费
 * @author smx
 * @date 2020/12/2020/12/22
 */
public class NioServer {

    public static void main(String[] args) throws Exception {
         // 1.内核启动了一个socket返回一个文件描述符,
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 2.然后调用内核的bind() 把文件描述符绑定到端口上
        // 3.调用内核的listen() 开始监听
        serverSocket.bind(new InetSocketAddress(8080));

        System.out.println("开始监听");
        // 4.设置非阻塞，底层调用内核的非阻塞 fcntl(socket, F_SETFL, flags | O_NONBLOCK);
        serverSocket.configureBlocking(false);

        List<SocketChannel> socketList = new ArrayList<>();
        while (true) {
            //不会阻塞
            SocketChannel socket =  serverSocket.accept();
            if(socket != null) {
                socket.configureBlocking(false);
                socketList.add(socket);
                System.out.println("有客户端连接: " + socket.getRemoteAddress());
            }
            Iterator<SocketChannel> iterator = socketList.iterator();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while (iterator.hasNext()) {
                buf.clear();
                SocketChannel socketChannel = iterator.next();
                //不会阻塞，调用内核recvfrom()
                int readLenth = socketChannel.read(buf);
                if(readLenth > 0) {
                    String content = new String(buf.array(), StandardCharsets.UTF_8);
                    System.out.println(content);
                    buf.flip();
                } else if(readLenth < 0) {
                    System.out.println("有客户端断开连接: " + socketChannel.getRemoteAddress());
                    socketChannel.close();
                    iterator.remove();
                }
            }
        }
    }
}
