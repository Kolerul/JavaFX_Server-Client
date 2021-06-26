package mainPart;

import collection.Ticket;

import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Computer {

    private final ServerPart serverPart;
    private final ServerMaker serverMaker;
    private static final int numberOfForks = 4;

    public Computer(ServerPart serverPart, ServerMaker serverMaker) {
        this.serverPart = serverPart;
        this.serverMaker = serverMaker;
    }

    public void computingRead() {

        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        while (serverPart.isIsClientConnected()) {
            //final int[] currentNumberOfForks = {0};
            forkJoinPool.invoke(new RecursiveAction() {
                @Override
                protected void compute() {
                    try {
                        SocketChannel socketChannel;
                        socketChannel = serverMaker.acceptConnection();
                        UserData userData = (UserData) serverMaker.deserialize(serverMaker.readData(socketChannel));
                        if (!userData.getExistedAccount()) {
                            try {
                                serverPart.getDataBaseWorker().createNewAccountInDB(userData.getLogin(), userData.getPassword());
                                serverMaker.getRequests().put(userData.getLogin(), "SuccessfullyCreatedNewAccount");
                            } catch (SQLException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                                serverMaker.getRequests().put(userData.getLogin(), "UnsuccessfullyCreatedNewAccount");
                            }
                        } else {
                            try {
                                    serverPart.getDataBaseWorker().checkAccountInDB(userData.getLogin(), userData.getPassword());
                                    if (userData.getData() != null)
                                        serverMaker.getResponses().put(userData.getData(), userData.getLogin());
                                    else serverMaker.getRequests().put(userData.getLogin(), "SuccessfullyEntry");

                            } catch (SQLException | NoSuchAlgorithmException e) {
                                serverMaker.getRequests().put(userData.getLogin(), e.getMessage());
                            }
                        }
                        serverMaker.usersChannels.put(userData.getLogin(), socketChannel);
                        //if (currentNumberOfForks[0] == numberOfForks) {
                            //currentNumberOfForks[0]++;
                            fork();
                       // }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void computingWrite() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        while (serverPart.isIsClientConnected()) {
            forkJoinPool.invoke(new RecursiveAction() {
                @Override
                protected void compute() {
                    for (Iterator<String> it = serverMaker.getRequests().keySet().stream().iterator(); it.hasNext(); ) {
                        String user = it.next();
                        Serializable serializable = serverMaker.getRequests().get(user);
                        serverMaker.getRequests().remove(user);
                        fork();
                        serverMaker.writeData(serverMaker.serialize(serializable), serverMaker.usersChannels.get(user));
                        try {
                            Integer integer = (Integer) serializable;
                        } catch (ClassCastException e) {};
                        //serverMaker.usersChannels.remove(user);
                    }
                }
            });
        }
    }
}
