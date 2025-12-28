package com.example.florify.ui.login;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.VBox;

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
        // reference the buttons here from the loginView
        Button registerButton = loginView.getRegisterButton();
        Button loginButton = loginView.getLoginButton();
        VBox contentPane = loginView.getContentPane();
        Label titleLabel = loginView.getTitleLabel();
        Label subtitleLabel = loginView.getSubtitleLabel();
        TextField usernameField = loginView.getUsernameField();
        TextField passwordField = loginView.getPasswordField();
        TextField confirmPasswordField = loginView.getConfirmPasswordField();

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
    }
}
