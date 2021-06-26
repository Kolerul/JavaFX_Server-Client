package applicationPart;

import collection.Ticket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class InfoController {
    @FXML
    public TextField ownerTF;
    @FXML
    public TextField nameTF;
    @FXML
    public TextField XTF;
    @FXML
    public TextField YTF;
    @FXML
    public TextField priceTF;
    @FXML
    public TextField typeTF;
    @FXML
    public TextField venueNameTF;
    @FXML
    public TextField venueTypeTF;
    @FXML
    public TextField venueCapacityTF;
    @FXML
    public TextField idTF;
    @FXML
    public TextField venueIdTF;
    @FXML
    public Button editButton;
    @FXML
    public Button closeButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Label messageLabel;

    @FXML
    private ComboBox<String> venuecb;

    @FXML
    private ComboBox<String> cb;

    @FXML
    void initialize() {
        ObservableList<String> listVenuecb = FXCollections.observableArrayList("","BAR", "LOFT", "THEATRE", "MALL", "STADIUM");
        venuecb.setItems(listVenuecb);
        ObservableList<String> listcb = FXCollections.observableArrayList("","VIP", "USUAL", "BUDGETARY", "CHEAP");
        cb.setItems(listcb);
        cb.getEditor().setEditable(false);
        venuecb.getEditor().setEditable(false);
    }

    public void setTFsEditable(boolean b) {
        nameTF.setEditable(b);
        XTF.setEditable(b);
        YTF.setEditable(b);
        priceTF.setEditable(b);
        //typeTF.setEditable(b);
        venueNameTF.setEditable(b);
        //venueTypeTF.setEditable(b);
        venueCapacityTF.setEditable(b);
        deleteButton.setDisable(!b);
        editButton.setDisable(!b);
        cb.setEditable(b);
        venuecb.setEditable(b);
    }

    public String getTicketFields() {
        final String[] s = new String[1];
             s[0] = ownerTF.getText() + "\n" +
                    nameTF.getText() + "\n" +
                    XTF.getText() + "\n" +
                    YTF.getText() + "\n" +
                    priceTF.getText() + "\n" +
                    cb.getSelectionModel().getSelectedItem();
            if (venueIdTF.getText().equals("") && !venueNameTF.getText().equals("")) {
                s[0] += "\n" + 0 + "\n";
            } else s[0] += "\n" + venueIdTF.getText() + "\n";
            s[0] += venueNameTF.getText() + "\n" +
                    venuecb.getSelectionModel().getSelectedItem() + "\n" +
                    venueCapacityTF.getText();
            if (idTF.getText().equals("")) s[0] += "\n" + 0;
            else s[0] += "\n" + idTF.getText();
        return s[0];
    }

    public void setTicketFields(Ticket ticket) {
            ownerTF.setText(ticket.getOwner());
            nameTF.setText(ticket.getName());
            XTF.setText(ticket.getCoordinates().getX().toString());
            YTF.setText(ticket.getCoordinates().getY().toString());
            priceTF.setText(ticket.getPrice().toString());
            if (ticket.getType() != null) {
                cb.getEditor().setText(ticket.getType().name());
            }
            else cb.getEditor().setText("");
            if (ticket.getVenue() != null) {
                venueNameTF.setText(ticket.getVenue().getName());
                if (ticket.getVenue().getType() != null) venuecb.getEditor().setText(ticket.getVenue().getType().name());
                else venuecb.getEditor().setText("");
                if (ticket.getVenue().getCapacity() != null && ticket.getVenue().getCapacity() > 0) venueCapacityTF.setText(ticket.getVenue().getCapacity().toString());
                else venueCapacityTF.setText("");
                venueIdTF.setText(ticket.getVenue().getId().toString());
            } else {
                venueNameTF.setText("");
                venuecb.getEditor().setText("");
                venueCapacityTF.setText("");
                venueIdTF.setText("");
            }
            idTF.setText(ApplicationWindows.ticketId.toString());
    }

    public void clearAllFields(String owner) {

        ownerTF.setText(owner);
        nameTF.setText("");
        XTF.setText("");
        YTF.setText("");
        priceTF.setText("");
        //typeTF.setText("");
        venueNameTF.setText("");
        //venueTypeTF.setText("");
        venueCapacityTF.setText("");
        idTF.setText("");
        venueIdTF.setText("");
        cb.getEditor().setText("");
        venuecb.getEditor().setText("");

    }

}
