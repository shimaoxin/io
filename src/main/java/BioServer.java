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
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("开始监听");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("有客户连接");
            new Thread(() -> {
                while (true) {
                    byte[] buf = new byte[100];
                    try {
                        socket.getInputStream().read(buf);
                        String content = new String(buf, StandardCharsets.UTF_8);
                        if(content.trim().length() > 0) {
                            System.out.println(content);
                        }
                        socket.sendUrgentData(0xFF);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        break;
                    }
                }

            }).start();
        }
    }
}
