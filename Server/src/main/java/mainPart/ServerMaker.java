package mainPart;

import exceptions.ConnectionException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMaker {
    protected SocketAddress socketAddress;
    protected ServerSocketChannel serverChannel;
    protected Selector selector;
    private final ConcurrentHashMap<Serializable, String> responses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Serializable> requests = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, SocketChannel> usersChannels = new ConcurrentHashMap<>();
    boolean itsWritten = false;

    public ServerMaker(int PORT) {
        this.socketAddress = new InetSocketAddress(PORT);
        try {
            selector = Selector.open();
            this.serverChannel = ServerSocketChannel.open();
            serverChannel.bind(socketAddress);
            System.out.println("Ожидание подключения.");
        } catch (IOException e) {
            System.out.println("Клиент не подключён к серверу");
            System.exit(1);
        }
    }
    public  ConcurrentHashMap<Serializable, String> getResponses() {return responses;}
    public  void removeResponse(Serializable key) {getResponses().remove(key);}
    public  void addRequest(String user, Serializable request) {
        getRequests().put(user, request);
    }
    public ConcurrentHashMap<String, Serializable> getRequests() {return requests;}



    public SocketChannel acceptConnection() throws IOException {
        SocketChannel socketChannel = null;
        while (socketChannel == null) {
            socketChannel = serverChannel.accept();
        }
        socketChannel.configureBlocking(false);
        return socketChannel;
    }

    public byte[] readData(SocketChannel channel)  {
    byte[] a = new byte[4096];
    ByteBuffer buffer = ByteBuffer.wrap(a);
    try {
        buffer.clear();
        int b = channel.read(buffer);
        while (b == -1 || b == 0) {
            b = channel.read(buffer);
            buffer.clear();
        };
        buffer.flip();
        buffer.clear();
        return a;
    } catch (IOException e) {
        throw new ConnectionException("Клиент не подключён серверу для получения данных");
    }
    }

    public static byte[] serialize(Serializable obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Serializable deserialize(byte[] rawData) {

        try {
            if (rawData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(rawData);
                ObjectInputStream objectInputStream = new ObjectInputStream(bis);
                return (Serializable) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public void writeData(byte[] bytes, SocketChannel channel) throws ConnectionException {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            while(channel.write(buffer) == 0) {};
            itsWritten = true;
            getRequests().remove(channel);
        } catch (IOException e) {
            throw new ConnectionException("Клиент не подключён к серверу для отправки данных");
        }
        buffer.flip();
        buffer.clear();
    }

    private void closeChannel(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

