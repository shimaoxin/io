import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Description
 *
 * @author smx
 * @date 2020/12/2020/12/22
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost",8080);
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        osw.write("我是客户端1");
        osw.flush();
        Thread.sleep(10000);
        osw.write("\n你好！");
        osw.flush();
        osw.close();

        Socket socket2 = new Socket("localhost",8080);
        OutputStreamWriter osw2 = new OutputStreamWriter(socket2.getOutputStream());
        osw2.write("我是客户端2");
        osw2.flush();
        osw2.write("\n你好吗？");
        osw2.flush();
        osw2.close();
    }
}
