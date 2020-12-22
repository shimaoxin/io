import com.sun.deploy.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Description
 *
 * @author smx
 * @date 2020/12/2020/12/22
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        /**
         * 1.内核启动了一个socket返回一个文件描述符,
         * 2.然后调用内核的bind() 把文件描述符绑定到端口上
         * 3.调用内核的listen() 开始监听
         */
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("开始监听");
        while (true) {
            //调用内核的poll(),会阻塞
            Socket socket = serverSocket.accept();
            socket.getRemoteSocketAddress();
            System.out.println("有客户端连接: " + socket.getRemoteSocketAddress());
            new Thread(() -> {
                while (true) {
                    byte[] buf = new byte[100];
                    try {
                        //如果客户端没有发消息来，会阻塞等待消息，调用内核recvfrom()
                        socket.getInputStream().read(buf);
                        String content = new String(buf, StandardCharsets.UTF_8);
                        if(content.trim().length() > 0) {
                            System.out.println(content);
                        } else {
                            System.out.println("客户端连接断开: " + socket.getRemoteSocketAddress());
                            break;
                        }
                    } catch (Exception e) {
                        break;
                    }
                }

            }).start();
        }
    }
}
