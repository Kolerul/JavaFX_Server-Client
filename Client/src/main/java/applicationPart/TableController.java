package applicationPart;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import collection.Coordinates;
import collection.Ticket;
import collection.TicketType;
import collection.VenueType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableController {

    @FXML
    private ResourceBundle resources;
    @FXML
    public Label userLabel;

    @FXML
    private URL location;

    @FXML
    public TableView<Ticket> table;
    @FXML
    public Button changeButton;
    @FXML
    public Button editButton;

    @FXML
    private TableColumn<Ticket, Integer> id;
    @FXML
    private TableColumn<Ticket, String> owner;
    @FXML
    private TableColumn<Ticket, String> name;

    @FXML
    private TableColumn<Ticket, Double> x;

    @FXML
    private TableColumn<Ticket, Double> y;

    @FXML
    private TableColumn<Ticket, Double> price;

    @FXML
    private TableColumn<Ticket, TicketType> ticketType;

    @FXML
    private TableColumn<Ticket, Long> venueId;

    @FXML
    private TableColumn<Ticket, String> venueName;

    @FXML
    private TableColumn<Ticket, Integer> capacity;

    @FXML
    public Button addButton;

    @FXML
    public Button deleteButton;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField xInput;

    @FXML
    private TextField yInput;

    @FXML
    private TextField priceInput;

    @FXML
    private TextField ticketTypeInput;

    @FXML
    private TextField venueNameInput;

    @FXML
    private TextField capacityInput;

    @FXML
    private TextField venueTypeInput;


    @FXML
    private TableColumn<Ticket, VenueType> venueType;
    ObservableList<Ticket> list = FXCollections.observableArrayList(


    );

    @FXML
    void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        x.setCellValueFactory(new PropertyValueFactory<>("x"));
        y.setCellValueFactory(new PropertyValueFactory<>("y"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ticketType.setCellValueFactory(new PropertyValueFactory<>("type"));
        capacity.setCellValueFactory(new PropertyValueFactory<>("venueCapacity"));
        venueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));
        venueName.setCellValueFactory(new PropertyValueFactory<>("venueName"));
        venueType.setCellValueFactory(new PropertyValueFactory<>("venueType"));
        table.setItems(list);

    }
}

