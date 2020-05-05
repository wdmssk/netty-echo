package io.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.echo.handler.EchoHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Echoes back any received data from a client.
 */
public final class EchoServer {

    private EchoServer() {
    }

    public static void start(int port) throws InterruptedException {

        // Configure the server.
        EventLoopGroup acceptorGroup = new NioEventLoopGroup(1);
        EventLoopGroup clientGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(acceptorGroup, clientGroup)
                       .channel(NioServerSocketChannel.class)
                       .option(ChannelOption.SO_BACKLOG, 100)
                       .handler(new LoggingHandler(LogLevel.INFO))
                       .childHandler(
                               new ChannelInitializer<SocketChannel>() {
                                   @Override
                                   public void initChannel(SocketChannel ch) throws Exception {
                                       ChannelPipeline channelPipeline = ch.pipeline();
                                       channelPipeline.addLast(new EchoHandler());
                                   }
                               }
                       );
        try {
            // Start the server.
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // Wait until the server socket is closed.
            channelFuture.channel().closeFuture().sync();
        } finally {
            // Shut down all event loops to terminate all threads.
            acceptorGroup.shutdownGracefully();
            clientGroup.shutdownGracefully();
        }
    }
}