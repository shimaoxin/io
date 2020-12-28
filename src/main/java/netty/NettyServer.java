package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Description
 *
 * @author smx
 * @date 2020/12/2020/12/23
 */
public class NettyServer {

    public static void main(String[] args) {
        //创建一个线程组，用于处理客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建一个线程组，用于处理与客户端的I/O操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //引导类，帮助服务启动的辅助类，可以设置 Socket参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //添加一个channel处理器，为我们自定义的类，需要继承ChannelHandler
                            ch.pipeline().addLast(new MyIoHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = bootstrap.bind(8080).sync(); // (7)
            System.out.println("开始监听");
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
