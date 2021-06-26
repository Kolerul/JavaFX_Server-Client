package applicationPart;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class AuthorizationWindowController {

    @FXML
    public Button closeButton;
    @FXML
    public MenuButton languageMenuButton;
    @FXML
    public MenuItem russianMenuItem;
    @FXML
    public MenuItem lithuanianMenuItem;
    @FXML
    public MenuItem columbianMenuItem;
    @FXML
    public MenuItem romanianMenuItem;
    @FXML
    public Label authLabel;
    @FXML
    public TextField loginTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button signInButton;
    @FXML
    public Button signUpButton;
    @FXML
    public Label messageLabel;


    @FXML
    public void buttonClicked() {
        System.exit(0);
    }
}
