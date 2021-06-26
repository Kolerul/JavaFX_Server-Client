package mainPart;

import com.sun.org.apache.xpath.internal.operations.Bool;
import exceptions.ConnectionException;
import exceptions.InvalidInputException;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ServerConnect implements Serializable {

    private final int connectionAttempts = 10;

    protected Socket client;
    private final String ip;
    private final int port;
    private boolean reconnectionIsNeeded = true;

    private final int maxLengthOfBytesArray = 4096;

    public ServerConnect (String ADDR, int PORT) {
        ip = ADDR;
        port = PORT;
    }

    public void writeData(Serializable data) throws ConnectionException {
        for (int attempt = 0; attempt <= connectionAttempts; attempt++) {
            try {
                client = new Socket(ip, port);
                OutputStream outputStream = client.getOutputStream();
                byte[] bytes = serialize(data);
                if (bytes.length <= maxLengthOfBytesArray) outputStream.write(bytes);
                else throw new InvalidInputException();
                break;
            } catch (IOException e) {
                e.printStackTrace();
                if (reconnectionIsNeeded) makeDelay(attempt);
                else attempt = connectionAttempts;
                if (attempt == connectionAttempts){
                    System.exit(1);
                    throw new ConnectionException("Время ожидания переподключения для отправки данных исстекло");
                }
            }
        }
    }
    public void writeDataWithoutCreatingConnection(Serializable data) throws ConnectionException {
            try {
                OutputStream outputStream = client.getOutputStream();
                byte[] bytes = serialize(data);
                if (bytes.length <= maxLengthOfBytesArray) outputStream.write(bytes);
                else throw new InvalidInputException();
            } catch (IOException e) {
                System.exit(1);
                throw new ConnectionException("Время ожидания переподключения для отправки данных исстекло");
            }
    }

    public Serializable readData() throws ConnectionException{
        try {
            InputStream inputStream = client.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object obj = objectInputStream.readObject();
            client.close();
            return (Serializable) obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            throw new ConnectionException("Нет подключения с сервером, чтобы получить данные");
        }
    }

    private byte[] serialize(Serializable data) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public void toggleReconnectionIsNeeded() {
        reconnectionIsNeeded = !reconnectionIsNeeded;
    }

    private void makeDelay(int attempt) {
        System.out.println("Переподключение... (Попытка " + attempt + " из " + connectionAttempts + ")");
        try {
            int connectionDelayMillis = 5000;
            Thread.sleep(connectionDelayMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}