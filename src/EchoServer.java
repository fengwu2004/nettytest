import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public final class EchoServer {

  private final int port;

  public EchoServer(int port) {

    this.port = port;
  }

  public void start() throws Exception {

    final EchoServerHandler serverHandler = new EchoServerHandler();

    EventLoopGroup group = new NioEventLoopGroup();

    try {

      ServerBootstrap b = new ServerBootstrap();

      b.group(group)
              .channel(NioServerSocketChannel.class)
              .localAddress(new InetSocketAddress(this.port))
              .childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                  socketChannel.pipeline().addLast(serverHandler);
                }
              });

      ChannelFuture f = b.bind().sync();

      f.channel().closeFuture().sync();
    }
    finally {

      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {

    EchoServer server = new EchoServer(8888);

    server.start();
  }
}