package mainPart;

import applicationPart.ApplicationWindows;
import collection.Ticket;
import commands.*;
import exceptions.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.io.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientPart {
    private String lastString = "";
    private final InputStream inputStream;
    private final Scanner in;
    private final ServerConnect serverDeliver;
    private final CommandDecoder cd = new CommandDecoder();
    private int attempt = 0;

    public synchronized static UserData getUserData() {
        return userData;
    }

    public synchronized static void setUserData(UserData userData) {
        ClientPart.userData = userData;
    }

    private static UserData userData;
    private ApplicationWindows applicationWindows;

    private double xOffset = 0;
    private double yOffset = 0;

    public ClientPart (ServerConnect serverDeliver, ApplicationWindows applicationWindows, InputStream inputStream) {
        this.serverDeliver = serverDeliver;
        this.inputStream = inputStream;
        this.in = new Scanner(this.inputStream);
        this.applicationWindows = applicationWindows;
    }

    private String safeRead(String field) {
        do {
            if (this.inputStream == System.in) {
                System.out.println(field);
            }
            try {
                lastString = in.nextLine();
            } catch (NoSuchElementException e) {
                in.close();
                System.exit(0);
            }
        } while (lastString.length() > 200);
        return lastString;
    }

    /*public void authorization() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                applicationWindows.launchApp();
            }
        });
        thread.start();

        while (applicationWindows.getAuthorizationWindowController() == null);
        System.out.println(1);
        authorizationWindowController.signInButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                userData = new UserData(authorizationWindowController.loginTextField.getText(),
                        authorizationWindowController.passwordTextField.getText());
                authorizationWindowController.loginTextField.setText("");
                authorizationWindowController.passwordTextField.setText("");
                serverDeliver.writeData(userData);
                String result = (String) serverDeliver.readData();
                applicationWindows.getAuthorizationWindowController().messageLabel.setText(result);
                if (result.equals("Вход успешно выполнен")) {
                    //ApplicationWindows.primaryStage.close();

                    //Start map window
                    Platform.runLater(() -> {
                        try {
                            applicationWindows.startMap();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    Thread mapBuilder = new Thread() {
                        @Override
                        public void run() {
                            while (ApplicationWindows.mapController == null);
                            //while (!ApplicationWindows.isMouseDragged) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                userData.setData(new GetAllElementsCommand());
                                serverDeliver.writeData(userData);
                                String collection = (String) serverDeliver.readData();
                                applicationWindows.mapDrawing(collection);
                           // }
                        }
                    };
                    mapBuilder.start();
                }
            }
        });

        Platform.runLater(ApplicationWindows.primaryStage::show);
    }*/

    public void writeUserData(Serializable data) {
        userData.setData(data);
        serverDeliver.writeData(userData);
    }

    public void understanding() {
        String command = "";
        while (!command.equals("exit")) {
            command = safeRead("Введите команду: (help - узнать список команд, exit - выход из программы (без сохранения))");
            try {
                understand(command);
            } catch (ConnectionException e) {
                System.out.println(e.getMessage());
                break;
            }
            catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void understand(String command) throws ConnectionException {
        String s = "";
        try {
            if (!command.equals("exit")) {
                if (!command.equals("Unreadable Command")) {
                    Command inputCommand = cd.decode(command.trim());
                    if (inputCommand.getClass() == ExecuteScriptCommand.class) {
                        try {
                            inputCommand.execute();
                            if (((ExecuteScriptCommand) inputCommand).getScanner() != null) {
                                String com = ((ExecuteScriptCommand) inputCommand).giveNewCommandFromFile();
                                while (com != null) {
                                    try {
                                        understand(com);
                                    } catch (ConnectionException e) {
                                        System.out.println(e.getMessage());
                                        System.exit(1);
                                    }
                                    com = ((ExecuteScriptCommand) inputCommand).giveNewCommandFromFile();
                                }
                                ExecuteScriptCommand.executeScriptCommands.clear();
                            }
                        } catch (InfiniteRecursionException e) {
                            System.out.println( e.getMessage());
                        }
                    } else {
                        writeUserData(inputCommand);
                        s = ((String) serverDeliver.readData());
                    }
                    if (s != null && s.startsWith("Введите название билета:")) {
                        serverDeliver.toggleReconnectionIsNeeded();
                        while (!s.contains("Ticket created")) {
                            String safeRead = safeRead(s);
                            try {
                                //userData.setData(safeRead);
                                writeUserData(safeRead);
                            } catch (ConnectionException e) {
                                System.out.println(e.getMessage());
                                serverDeliver.toggleReconnectionIsNeeded();
                                return;
                            }
                            s = ((String) serverDeliver.readData());
                        }
                    }
                    String output = null;
                    if (s != null) {
                        output = s.replace("Ticket created", "");
                    }
                    System.out.println(output);
                }
            }
        } catch (NumberFormatException e) {System.out.println("Аргумент имеет неправльный тип (для id - int, для price - double)");}
        catch(NullPointerException | IllegalArgumentException e){
                if (command.equals("exit")) { System.exit(0); }
                System.out.println("Такой команды не существует.");
        }
        catch(IllegalCountOfArgumentsException e){
                System.out.println(e.getMessage());
        }
    }
}
