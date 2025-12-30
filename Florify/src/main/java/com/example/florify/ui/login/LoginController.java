package com.example.florify.ui.login;

import com.example.florify.db.Database;
import com.example.florify.db.UserDAO;
import com.example.florify.ui.feed.FeedPage;
import com.example.florify.ui.navigation.SceneManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import com.example.florify.Server.ServerLauncher;
import com.example.florify.common.Session;
import javafx.stage.Stage;

public class LoginController
{
    private final LoginView loginView;

    public LoginController(LoginView loginView)
    {
        this.loginView = loginView;
        setupActions();
    }

    private void setupActions()
    {
        // first initialize the database if it is not already initialized
        // UserDAO.dropUsersTable();
        Database.init();

        // reference the buttons here from the loginView
        Button registerButton = loginView.getRegisterButton();
        Button loginButton = loginView.getLoginButton();
        VBox contentPane = loginView.getContentPane();
        Label titleLabel = loginView.getTitleLabel();
        Label subtitleLabel = loginView.getSubtitleLabel();
        TextField usernameField = loginView.getUsernameField();
        PasswordField passwordField = loginView.getPasswordField();
        PasswordField confirmPasswordField = loginView.getConfirmPasswordField();

        // event for switching login/signup
        registerButton.setOnAction(e ->
        {
            System.out.println("Register button pressed");
            if (loginView.getCurrentButtonState() == LoginView.ButtonState.REGISTER)
            {
                loginView.slideToLoginAnimation(700, contentPane, registerButton, titleLabel,
                        subtitleLabel, usernameField, confirmPasswordField, loginButton);
            }
            else
            {
                loginView.slideToRegisterAnimation(700, contentPane, registerButton, titleLabel,
                        subtitleLabel, usernameField, loginButton);
            }
        });

        // TODO: CONFIRM PASSWORD MUST BE SAME AS PASSWORD
        // TODO: PASSWORD AND CONFIRM PASSWORD HASHING
        // TODO: UI OR SOME LIL LABEL AS AN INDICATOR OF LOGIN/REGISTER STATUS
        loginButton.setOnAction(e ->
        {
            String password = passwordField.getText();

            if(loginView.getCurrentButtonState() == LoginView.ButtonState.LOGIN)
            {
                String email = usernameField.getText().toLowerCase();
                String confirmPassword = confirmPasswordField.getText();
                if(validEmail(email) && matchingPassword(password, confirmPassword))
                {
                    String username = email.substring(0, email.indexOf("@"));
                    boolean registered = UserDAO.registerUser(username, email, password);

                    if(registered)
                    {
                        System.out.println("register successful");
                        loginView.slideToRegisterAnimation(700, contentPane, registerButton, titleLabel,
                                subtitleLabel, usernameField, loginButton);
                    }
                    else
                    {
                        System.out.println("register failed");
                    }
                }
                else
                {
                    System.out.println("Password doesnt match");
                }
            }
            else
            {
                String username = usernameField.getText();
                boolean loggedIn = UserDAO.loginUser(username, password);

                if(loggedIn)
                {
                    System.out.println("Login successful");
                    new Thread(() -> ServerLauncher.startFeedServer()).start();
                    Session.setUsername(username);

                    Scene scene = SceneManager.loadMainScene();
                    SceneManager.switchScene(scene);
                }

                else
                {
                    System.out.println("Login failed");
                }
            }
        }
        );
    }

    private boolean validEmail(String email)
    {
        if(email.isBlank())
        {
            System.out.println("Email is blank invalid input");
            return false;
        }

        boolean valid = false;
        String[] validDomains = {"@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com"};


        // check if the email contains any of the validDomains
        for(String domain : validDomains)
        {
            if(email.endsWith(domain))
            {
                valid = true;
                break;
            }
        }

        System.out.println("Email is invalid please enter a valid domain ex: @outlook.com");
        return valid;
    }

    private boolean matchingPassword(String password, String confirmPassword)
    {
        return password.equals(confirmPassword);
    }
}
