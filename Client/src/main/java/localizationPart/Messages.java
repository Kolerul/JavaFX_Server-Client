package localizationPart;

import static java.util.ResourceBundle.getBundle;

import applicationPart.AuthorizationWindowController;
import com.sun.javafx.scene.control.skin.resources.ControlResources;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * The class with all messages of this application.
 */
public abstract class Messages {

    private static ResourceBundle BUNDLE;

    private static final String FIELD_NAME = "lookup";
    private static final String BUNDLE_NAME = "messages";
    private static final String CONTROLS_BUNDLE_NAME = "com/sun/javafx/scene/control/skin/resources/controls";

    public static String MAIN_TITLE;

    static {
        final Locale locale = Locale.getDefault();
        final ClassLoader classLoader = ControlResources.class.getClassLoader();

        final ResourceBundle controlBundle = getBundle(CONTROLS_BUNDLE_NAME,
                locale, classLoader, PropertyLoader.getInstance());

        final ResourceBundle overrideBundle = getBundle(CONTROLS_BUNDLE_NAME,
                PropertyLoader.getInstance());

        final Map override = ReflectionUtils.getUnsafeFieldValue(overrideBundle, FIELD_NAME);
        final Map original = ReflectionUtils.getUnsafeFieldValue(controlBundle, FIELD_NAME);

        original.putAll(override);

        BUNDLE = getBundle(BUNDLE_NAME, PropertyLoader.getInstance());

        MAIN_TITLE = BUNDLE.getString("Main.title");

    }

    public static ResourceBundle GetBundle() {
        return BUNDLE;
    }
    public static void changeBundle() {
        final Locale locale = Locale.getDefault();
        final ClassLoader classLoader = ControlResources.class.getClassLoader();

        final ResourceBundle controlBundle = getBundle(CONTROLS_BUNDLE_NAME,
                locale, classLoader, PropertyLoader.getInstance());

        final ResourceBundle overrideBundle = getBundle(CONTROLS_BUNDLE_NAME,
                PropertyLoader.getInstance());

        final Map override = ReflectionUtils.getUnsafeFieldValue(overrideBundle, FIELD_NAME);
        final Map original = ReflectionUtils.getUnsafeFieldValue(controlBundle, FIELD_NAME);

        original.putAll(override);

        BUNDLE = getBundle(BUNDLE_NAME, PropertyLoader.getInstance());

        MAIN_TITLE = BUNDLE.getString("Main.title");
    }

    public static void changeAuthorizationWindow(AuthorizationWindowController controller) {

        controller.closeButton.setText(Messages.GetBundle().getString("Controller.closeButton"));
        controller.languageMenuButton.setText(Messages.GetBundle().getString("Controller.languageMenuButton"));
        controller.authLabel.setText(Messages.GetBundle().getString("Controller.authLabel"));
        controller.loginTextField.setPromptText(Messages.GetBundle().getString("Controller.loginTextField"));
        controller.passwordTextField.setPromptText(Messages.GetBundle().getString("Controller.passwordTextField"));
        controller.signInButton.setText(Messages.GetBundle().getString("Controller.signInButton"));
        controller.signUpButton.setText(Messages.GetBundle().getString("Controller.signUpButton"));
    }
}