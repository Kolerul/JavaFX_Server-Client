package applicationPart;

import collection.Ticket;
import commands.*;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import localizationPart.Messages;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mainPart.ServerConnect;
import mainPart.UserData;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ApplicationWindows extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    public AuthorizationWindowController authorizationWindowController;
    public MapController mapController;
    public InfoController infoController;
    public TableController tableController;
    public static Stage primaryStage;
    public static volatile Stage infoStage;
    public static Integer ticketId = 0;
    public MenuButton availableTickets = null;

    public synchronized boolean isNeedToDraw() {
        return isNeedToDraw;
    }

    public synchronized void setNeedToDraw(boolean needToDraw) {
        isNeedToDraw = needToDraw;
    }

    public boolean isNeedToDraw = true;

    public synchronized boolean isNeedToFill() {
        return isNeedToFill;
    }

    public synchronized void setNeedToFill(boolean needToFill) {
        isNeedToFill = needToFill;
    }

    public boolean isNeedToFill = true;

    public synchronized boolean isItTable() {
        return isItTable;
    }

    public synchronized void setItTable(boolean itTable) {
        isItTable = itTable;
    }

    public boolean isItTable = false;

    public UserData userData;

    public Thread mapBuilder;
    private Thread tableBuilder;

    ConcurrentHashMap<Integer, ImageView> circles = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Color> ownerColors = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, String> idNames = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, String> idCP = new ConcurrentHashMap<>();
    ArrayList<MenuItem> menuItems = new ArrayList<>();

    private ServerConnect serverDeliver = new ServerConnect("127.0.0.1",2425);

    volatile Double xx = 0.;
    volatile Double yy = 0.;

    public final int stageX = 1000;
    public final int stageY = 800;


    @Override
    public void start(Stage authStage) throws Exception{
        primaryStage = authStage;
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample.fxml"));
        loader.setResources(Messages.GetBundle());
        Parent root = loader.load();
        authStage.setTitle(Messages.MAIN_TITLE);
        authorizationWindowController = loader.getController();
        System.out.println(authorizationWindowController);
        authStage.setScene(new Scene(root, 1500, 750));
        authStage.setResizable(false);

        authorizationWindowController.russianMenuItem.setOnAction(event -> {
            Locale.setDefault(new Locale("ru", "RU"));
            Messages.changeBundle();
            Messages.changeAuthorizationWindow(authorizationWindowController);
        });
        authorizationWindowController.lithuanianMenuItem.setOnAction(event -> {
            Locale.setDefault(new Locale("lt", "LT"));
            Messages.changeBundle();
            Messages.changeAuthorizationWindow(authorizationWindowController);
        });
        authorizationWindowController.columbianMenuItem.setOnAction(event -> {
            Locale.setDefault(new Locale("co", "CO"));
            Messages.changeBundle();
            Messages.changeAuthorizationWindow(authorizationWindowController);
        });
        authorizationWindowController.romanianMenuItem.setOnAction(event -> {
                Locale.setDefault(new Locale("ro", "RO"));
                Messages.changeBundle();
                Messages.changeAuthorizationWindow(authorizationWindowController);
        });
        setAuthStageButtons();
        authStage.show();
    }


    public void setAuthStageButtons() {
        authorizationWindowController.closeButton.setOnMouseClicked(event -> System.exit(0));
        authorizationWindowController.signInButton.setOnMouseClicked(event -> {
            userData = new UserData(authorizationWindowController.loginTextField.getText(),
                    authorizationWindowController.passwordTextField.getText());
            authorizationWindowController.loginTextField.setText("");
            authorizationWindowController.passwordTextField.setText("");
            serverDeliver.writeData(userData);
            String result = (String) serverDeliver.readData();
            authorizationWindowController.messageLabel.setText(Messages.GetBundle().getString(result));
            if (result.equals("SuccessfullyEntry")) {
                Platform.runLater(() -> {
                    try {
                        setInfoPanel();
                        startMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        authorizationWindowController.signUpButton.setOnMouseClicked(event -> {
            userData = new UserData(authorizationWindowController.loginTextField.getText(),
                    authorizationWindowController.passwordTextField.getText(), false);
            authorizationWindowController.loginTextField.setText("");
            authorizationWindowController.passwordTextField.setText("");
            serverDeliver.writeData(userData);
            String result = (String) serverDeliver.readData();
            authorizationWindowController.messageLabel.setText(Messages.GetBundle().getString(result));
        });
    }

    public void startMap() throws IOException {
        //Stage mapStage = new Stage();
        //primaryStage = mapStage;
        setItTable(false);
        circles = new ConcurrentHashMap<>();
        idNames = new ConcurrentHashMap<>();
        idCP = new ConcurrentHashMap<>();
        setNeedToDraw(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/map.fxml"));
        loader.setResources(Messages.GetBundle());
        Parent root = loader.load();
        mapController = loader.getController();
        mapController.userLabel.setText(userData.getLogin());
        primaryStage.setScene(new Scene(root, stageX, stageY));
        mapController.map.setOnMousePressed(event -> {
            xOffset = event.getScreenX();
            yOffset = event.getScreenY();
        });

        mapController.addButton.setOnMouseClicked((event) -> {
            setNeedToDraw(false);
            try {
                mapBuilder.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showInfoStageForAdd();
        });
        mapController.changeButton.setOnMouseClicked(event -> {
            setNeedToDraw(false);
            try {
                mapBuilder.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                startTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        startThreadToMapDraw();

    }

    public synchronized void startThreadToMapDraw() {
        mapBuilder = new Thread(() -> {
            while (isNeedToDraw()) {
                userData.setData(new GetAllElementsCommand());
                serverDeliver.writeData(userData);
                String collection = (String) serverDeliver.readData();
               Platform.runLater(() -> {
                   mapDrawing(collection);
               });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mapBuilder.start();
    }

    public void mapDrawing(String dataToDraw) {


        ArrayList<Integer> ids = new ArrayList<>();
        if (!dataToDraw.equals("")) {
            String[] coll = dataToDraw.split("\n");
            for (int i = 0; i < coll.length; i += 5) {

                //ImageCode

                Integer id = Integer.parseInt(coll[i]);
                ids.add(id);
                String owner = coll[i + 1];
                double price = Double.parseDouble(coll[i + 3]);
                String name = "";
                try {
                    name = coll[i + 4];
                } catch (ArrayIndexOutOfBoundsException e) {}
                String[] coords = coll[i + 2].split(" ");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                boolean needToDraw = true;

                if (idNames.containsKey(id)) {
                    String oldName = idNames.get(id);
                    String[] cp = idCP.get(id).split("\n");
                    Double oldX = Double.parseDouble(cp[0]);
                    Double oldY = Double.parseDouble(cp[1]);
                    Double oldPrice = Double.parseDouble(cp[2]);
                    if (!oldName.equals(name)) idNames.put(id, name);
                    needToDraw = !oldX.equals(x) || !oldY.equals(y) || !oldPrice.equals(price);
                }
                if (needToDraw) {
                    idCP.put(id, x + "\n" + y + "\n" + price);
                    Color color;
                    if (ownerColors.containsKey(owner)) color = ownerColors.get(owner);
                    else {
                        System.out.println("yes " + owner);
                        Random random = new Random();
                        final float r = random.nextFloat();
                        final float g = random.nextFloat();
                        final float b = random.nextFloat();
                        color = new Color(r, g, b, 0.5);
                        ownerColors.put(owner, color);
                    }

                    Class<?> clazz = this.getClass();



                    InputStream input = clazz.getResourceAsStream("/ticket.png");
                    if (price < 30) price = 30.;
                    if (price > 100) price = 100.;
                    int width = 48;
                    int height = 36;
                    Image image = new Image(input, width, height, false, true);


                    ImageView imageView = new ImageView(image);
                    imageView.setClip(new ImageView(image));
                    ColorAdjust monochrome = new ColorAdjust();
                    monochrome.setSaturation(1);

                    Blend blush = new Blend(
                            BlendMode.SCREEN,
                            monochrome,
                            new ColorInput(
                                    0,
                                    0,
                                    imageView.getImage().getWidth(),
                                    imageView.getImage().getHeight(),
                                    color
                            )
                    );
                    imageView.setEffect(blush);
                    imageView.setCache(true);
                    imageView.setCacheHint(CacheHint.SPEED);
                    y = y + 48 - yy;
                    if (y > 465 + 130 - height) y = 465 + 130 - height;
                    x = x + 48 + xx;
                    if (x > stageX - width) x = stageX - width - 20.;
                    AnchorPane.setBottomAnchor(imageView, y);
                    AnchorPane.setLeftAnchor(imageView, x);

                    double finalY = y;
                    double finalX = x;
                    //System.out.println(finalX + " " +finalY);
                        //mapController.map.getChildren().removeAll(circles.get(id));
                        //circles.remove(id);
                    FadeTransition scaleTransition = new FadeTransition(Duration.seconds(2), imageView);
                    scaleTransition.setFromValue(0);
                    scaleTransition.setToValue(1.0);
                    scaleTransition.setCycleCount(1);
                        if (circles.containsKey(id)) {

                            AnchorPane.setBottomAnchor(circles.get(id), finalY);
                            AnchorPane.setLeftAnchor(circles.get(id), finalX);

                        } else {
                            circles.put(id, imageView);
                            mapController.map.getChildren().add(imageView);
                            scaleTransition.play();
                        }
                        idNames.put(id, name);
                }
            }
        }
        for (Integer id : idNames.keySet()) {
            if (!ids.contains(id)) {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), circles.get(id));
                fadeTransition.setFromValue(1.);
                fadeTransition.setToValue(0.);
                fadeTransition.setCycleCount(1);
                fadeTransition.play();
                mapController.map.getChildren().removeAll(circles.get(id));
                idNames.remove(id);
                circles.remove(id);
                idCP.remove(id);
            }

        }

        mapController.map.setOnMouseClicked(event -> {
            menuItems = new ArrayList<>();
            for (Integer id : circles.keySet()) {
                ImageView circle = circles.get(id);
                System.out.println(event.getSceneX() + " " + event.getSceneY() + " " + AnchorPane.getLeftAnchor(circle) + " " +AnchorPane.getBottomAnchor(circle));
                if (AnchorPane.getBottomAnchor(circle) + 70 <= stageY - event.getSceneY() + 50 && AnchorPane.getBottomAnchor(circle) + 70 >= stageY - event.getSceneY() - 50 &&
                        AnchorPane.getLeftAnchor(circle) <= event.getSceneX() + 50 && AnchorPane.getLeftAnchor(circle) >= event.getSceneX() - 50) {
                    MenuItem mi = new MenuItem(id + ": " + idNames.get(id));
                    menuItems.add(mi);
                    //System.out.println("yes");
                }
            }
            //System.out.println(1);
            if (menuItems.size() == 1) {
                for (Integer id : idNames.keySet()) {
                    if ((id + ": " + idNames.get(id)).equals(menuItems.get(0).getText())) {
                        isNeedToDraw = false;
                        try {
                            mapBuilder.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        showInfoPanel(id);
                    }
                }
            } else
            if (!menuItems.isEmpty()) {
                //System.out.println(2);
                mapController.map.getChildren().removeAll(availableTickets);
                availableTickets = new MenuButton("Okey", null, menuItems.toArray(menuItems.toArray(new MenuItem[0])));
                AnchorPane.setBottomAnchor(availableTickets, stageY - event.getSceneY() - 70);
                AnchorPane.setLeftAnchor(availableTickets, event.getSceneX());
                if (event.getSceneX() > 900) AnchorPane.setLeftAnchor(availableTickets, event.getSceneX() - 100);
                mapController.map.getChildren().add(availableTickets);
                //MenuButton finalAvailableTickets = availableTickets;
                for (MenuItem mi : availableTickets.getItems()) {
                    mi.setOnAction(event1 -> {
                        mapController.map.getChildren().removeAll(availableTickets);
                        for (Integer id : idNames.keySet()) {
                            if ((id + ": " + idNames.get(id)).equals(mi.getText())) {
                                setNeedToDraw(false);
                                try {
                                    mapBuilder.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                showInfoPanel(id);
                            }
                        }
                        //System.out.println(mi.getText());
                    });
                }
            }
        });
    }

    public void setInfoPanel() throws IOException {

        infoStage = new Stage();
        infoStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/info.fxml"));
        loader.setResources(Messages.GetBundle());
        Parent root = loader.load();
        infoController = loader.getController();
        infoStage.setScene(new Scene(root, 850, 600));
        setInfoPanelButtons();

    }

    public void showInfoPanel(Integer id) {
        infoController.messageLabel.setText("");
        ticketId = id;
        getInfoForInfoPanel();
        infoStage.show();
    }

    public void hideInfoPanel() {

        if (isItTable()) {
            setNeedToFill(true);
            startThreadToTable();
        } else {
            setNeedToDraw(true);
            startThreadToMapDraw();
        }
        infoStage.hide();
    }

    public void setInfoPanelButtons() {
        infoController.editButton.setOnMouseClicked(event -> {
            userData.setData(new SendTicketDataCommand(infoController.getTicketFields()));
            serverDeliver.writeData(userData);
            String result = (String) serverDeliver.readData();
            infoController.messageLabel.setText(Messages.GetBundle().getString(result));

            GetElementByIdCommand getElementByIdCommand = new GetElementByIdCommand();
            getElementByIdCommand.id = ApplicationWindows.ticketId;
            userData.setData(getElementByIdCommand);
            serverDeliver.writeData(userData);
            Ticket ticket = (Ticket) serverDeliver.readData();
            infoController.setTicketFields(ticket);
            idNames.put(ticket.getId(), ticket.getName());
        });
        infoController.deleteButton.setOnMouseClicked(event -> {
            RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand();
            removeByIdCommand.addArgument(infoController.idTF.getText());
            userData.setData(removeByIdCommand);
            serverDeliver.writeData(userData);
            String result = (String) serverDeliver.readData();
            System.out.println(result);
            hideInfoPanel();
        });
        infoController.closeButton.setOnMouseClicked(event -> hideInfoPanel());
        infoStage.setOnCloseRequest((event) -> hideInfoPanel());
    }

    public void getInfoForInfoPanel() {

        GetElementByIdCommand getElementByIdCommand = new GetElementByIdCommand();
        getElementByIdCommand.id = ApplicationWindows.ticketId;
        userData.setData(getElementByIdCommand);
        serverDeliver.writeData(userData);
        Ticket ticket = (Ticket) serverDeliver.readData();
        infoController.setTicketFields(ticket);
        if (!ticket.getOwner().equals(userData.getLogin())) {
            infoController.setTFsEditable(false);
            infoController.editButton.setDisable(true);
        }
        else {
            infoController.setTFsEditable(true);
            infoController.editButton.setDisable(false);
        }
    }

    public void startTable() throws IOException {
        setItTable(true);
        setNeedToFill(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/table.fxml"));
        loader.setResources(Messages.GetBundle());
        Parent root = loader.load();
        tableController = loader.getController();
        tableController.userLabel.setText(userData.getLogin());
        primaryStage.setScene(new Scene(root, stageX, stageY));

        tableController.addButton.setOnMouseClicked((event) -> {
            setNeedToFill(false);
            showInfoStageForAdd();
        });
        tableController.changeButton.setOnMouseClicked(event -> {

            setNeedToFill(false);
            try {
                tableBuilder.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                startMap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        tableController.editButton.setOnAction(event1 -> {
            ObservableList<Ticket> selectedTickets;
            //allTickets = tableController.table.getItems();
            selectedTickets = tableController.table.getSelectionModel().getSelectedItems();


                    setNeedToFill(false);
                    try {
                        tableBuilder.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!selectedTickets.isEmpty()) showInfoPanel(selectedTickets.get(0).getId());
                });
        startThreadToTable();
    }

    public synchronized void startThreadToTable() {
        tableBuilder = new Thread(() -> {
            //ServerConnect serverDeliver = new ServerConnect("127.0.0.1", 2425);
            //final UserData userData1 = userData;
            while (isNeedToFill()) {
                userData.setData(new GetAllIdsCommand());
                serverDeliver.writeData(userData);
                String[] ids = ((String) serverDeliver.readData()).split("\n");
                tableFilling(ids);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        tableBuilder.start();
    }

    public void tableFilling(String[] ids) {
        ArrayList<String> ids1 = new ArrayList<>();
        for (String id : ids) {
            ids1.add(id);
            //System.out.println(id);
            GetElementByIdCommand getElementByIdCommand = new GetElementByIdCommand();
            getElementByIdCommand.addArgument(id);
            userData.setData(getElementByIdCommand);
            serverDeliver.writeData(userData);
            Ticket ticket = (Ticket) serverDeliver.readData();
            Ticket ticket1 = null;
            int indexToAdd = 0;
            boolean itNeedToAdd = true;
            for (int i = 0; i < tableController.list.size(); i++) {
                if (tableController.list.get(i).getId() == ticket.getId()) {
                    indexToAdd = i;
                    ticket1 = tableController.list.get(i);
                    itNeedToAdd = false;
                }
            }
            if (itNeedToAdd) tableController.list.add(ticket);
            else {
                if (!ticket.equals(ticket1)) {
                    tableController.list.remove(indexToAdd);
                    tableController.list.add(indexToAdd, ticket);
                }
            }
        }
        tableController.list.removeIf(t -> !ids1.contains(String.valueOf(t.getId())));
    }

    private void showInfoStageForAdd() {
        infoController.clearAllFields(userData.getLogin());
        infoController.setTFsEditable(true);
        infoController.deleteButton.setDisable(true);
        infoController.editButton.setOnMouseClicked((event1 -> {
            userData.setData(new SendTicketDataCommand(infoController.getTicketFields()));
            serverDeliver.writeData(userData);
            String result = (String) serverDeliver.readData();
            infoController.messageLabel.setText(Messages.GetBundle().getString(result));
            if (result.equals("Success")) {
                hideInfoPanel();
                setInfoPanelButtons();
            }
        }));

        infoStage.show();
    }

    public void launchApp() {
        // Forced to set the language
        //Locale.setDefault(new Locale("en", "EN"));
        //Locale.setDefault(new Locale("be", "BY"));
        Locale.setDefault(new Locale("ru", "RU"));
        launch();
    }
    public ApplicationWindows(ServerConnect serverDeliver) {
        this.serverDeliver = serverDeliver;
    }
    public ApplicationWindows() {}
}
