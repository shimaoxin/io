package test;

import io.netty.channel.EventLoop;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.util.concurrent.EventExecutorChooserFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Description
 *
 * @author smx
 * @date 2020/12/2020/12/24
 */
public class MyEventLoopGroup extends MultithreadEventLoopGroup {


    protected MyEventLoopGroup(int nThreads, Executor executor, Object... args) {
        super(nThreads, executor, args);
    }

    protected MyEventLoopGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
        super(nThreads, threadFactory, args);
    }

    protected MyEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
        super(nThreads, executor, chooserFactory, args);
    }

    @Override
    protected EventLoop newChild(Executor executor, Object... args) throws Exception {
        return null;
    }
}
