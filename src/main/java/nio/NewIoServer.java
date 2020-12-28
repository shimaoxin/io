package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * java新io,使用多路复用器
 * bug:空轮询会造成cpu爆满，java nio bug
 * 缺陷：当代码量多的时候，整套项目代码可能就显得非常格外繁重
 * @author smx
 * @date 2020/12/2020/12/23
 */
public class NewIoServer {

    public static void main(String[] args) throws Exception{
        // 1.内核启动了一个socket返回一个文件描述符,
        ServerSocketChannel serverSocket = ServerSocketChannel.open();

        // 2.然后调用内核的bind() 把文件描述符绑定到端口上
        // 3.调用内核的listen() 开始监听
        serverSocket.bind(new InetSocketAddress(8080));

        System.out.println("开始监听");
        // 4.设置非阻塞，底层调用内核的非阻塞 fcntl(socket, F_SETFL, flags | O_NONBLOCK);
        serverSocket.configureBlocking(false);

        // 5.底层调用epoll_create(),创建epoll对象，给多路复用器开辟一块内存
        Selector selector = Selector.open();

        // 6.底层调用epoll_add(),把channel注册到多路复用器上
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //阻塞获取,调用epoll_wait()
            int count = selector.select();
            if(count > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                ByteBuffer buf = ByteBuffer.allocate(1024);
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //主动移除，保证下次不回重复读取
                    iterator.remove();
                    if(key.isAcceptable()) {
                        //处理请求连接
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
                        SocketChannel socket = serverSocketChannel.accept();
                        //设置不阻塞
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                        System.out.println("有客户端连接: " + socket.getRemoteAddress());
                    } else if(key.isReadable()) {
                        buf.clear();
                        //读取数据
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        //不会阻塞，调用内核recvfrom()
                        int readLenth = socketChannel.read(buf);
                        if(readLenth > 0) {
                            String content = new String(buf.array(), StandardCharsets.UTF_8);
                            System.out.println(content);
                            buf.flip();
                        } else if(readLenth < 0) {
                            System.out.println("有客户端断开连接: " + socketChannel.getRemoteAddress());
                            socketChannel.close();
                        }
                    }
                }
            }
        }

    }
}
