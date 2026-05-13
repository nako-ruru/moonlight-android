package com.limelight.nvstream.http;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;

// 这个工厂负责在创建 Socket 后立即绑定到指定 IP
public class BoundSocketFactory extends SocketFactory {
    private final InetAddress localIP;

    public BoundSocketFactory(InetAddress localIP) {
        this.localIP = localIP;
    }

    private Socket bindSocket(Socket socket) throws IOException {
        // 0 表示让系统随机分配一个本地端口
        socket.bind(new InetSocketAddress(localIP, 0));
        return socket;
    }

    @Override
    public Socket createSocket() throws IOException {
        return bindSocket(new Socket());
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket s = createSocket();
        s.connect(new InetSocketAddress(host, port));
        return s;
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket s = createSocket();
        s.connect(new InetSocketAddress(host, port));
        return s;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        // 这里的参数 localHost 会被覆盖，以确保使用我们指定的 IP
        return bindSocket(new Socket(host, port, localIP, localPort));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return bindSocket(new Socket(address, port, localIP, localPort));
    }
}
