package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Login extends Postgre implements Initializable {
    @FXML
    private TextField loginTF, registerPassField;

    @FXML
    private PasswordField passField;

    ArrayList<Circle> circles = new ArrayList<>();
    int timer = 21;
    boolean properUsername = false, properPassword = false, loaded = false;

    Pane connectionError;
    @FXML
    AnchorPane loadingPane, mainPane, errorPane;
    @FXML
    Circle circle1, circle2, circle3, circle4, circle5;
    @FXML
    Label wrongName, wrongPass;
    @FXML
    CheckBox register;

    @FXML
    void signIn() throws SQLException {
        if(!register.isSelected()){
             if (checkUser(loginTF.getText())){
                if(checkPass(loginTF.getText(), passField.getText())){
                    if(getStatus(loginTF.getText()).equals("Offline")){
                        mainPane.setDisable(true);
                        loading();
                        wrongName.setVisible(false);
                    }else{
                        wrongName.setText("User is already online.");
                        wrongName.setVisible(true);
                    }
                    wrongPass.setVisible(false);


                }else{
                    wrongPass.setText("Incorrect password.");
                    wrongPass.setVisible(true);
                }
                wrongName.setVisible(false);
             }else{
                 wrongName.setText("Incorrect username.");
                 wrongName.setVisible(true);
             }
        }else if(register.isSelected()){
            properUsername = false;
            properPassword = false;

            properUsername();
            properPassword();
            if(properUsername){
                if(properPassword){
                    addUser(loginTF.getText(), registerPassField.getText());
                    loading();
                }else{
                    passField.setText("");
                }
            }
        }
    }

    @FXML
    void clearAll(){
        wrongPass.setVisible(false);
        wrongName.setVisible(false);
        errorPane.setVisible(false);
    }

    void properUsername() throws SQLException {
        int nameLength = loginTF.getText().length();
        if (nameLength < 4){
            wrongName.setText("Username is too short");
            wrongName.setVisible(true);
            errorPane.setVisible(true);
        }else if(nameLength > 15){
            wrongName.setText("Username is too long");
            wrongName.setVisible(true);
            errorPane.setVisible(true);
        }else{
            if(!usernameTaken(loginTF.getText())){
                properUsername = true;
            }else{
                wrongName.setText("Username is taken.");
                wrongName.setVisible(true);
            }
        }
    }

    void properPassword(){
        int nameLength = registerPassField.getText().length();
        if (nameLength < 4){
            passField.setText("");
            wrongPass.setText("Password is too short");
            wrongPass.setVisible(true);
            errorPane.setVisible(true);
        }else if(nameLength > 25){
            passField.setText("");
            wrongPass.setText("Password is too long");
            wrongPass.setVisible(true);
            errorPane.setVisible(true);
        }else{
            properPassword = true;
        }
    }

    void loading(){
        loadingPane.setVisible(true);
        Timeline tml = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            checkColor();
            circles.add(circles.get(0));
            circles.remove(0);
            timer--;
            if(timer <= 0 && !loaded){
                try{
                    loaded = true;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
                    Parent root = loader.load();
                    Controller controller = loader.getController();
                    controller.getValues(loginTF.getText(), passField.getText());
                    Stage stage = (Stage) mainPane.getScene().getWindow();
                    stage.setOnCloseRequest(event -> {
                        try {
                            controller.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                    stage.setScene(new Scene(root, 700, 500));
                    stage.show();

                }catch (IOException exception){
                    System.out.println(exception.getMessage());
                }
            }
        }));
        tml.setAutoReverse(false);
        tml.setCycleCount(300);
        tml.play();
    }

    void checkColor(){
        circles.get(0).setFill(Color.BLACK);
        circles.get(1).setFill(Color.WHITE);
        circles.get(2).setFill(Color.WHITE);
        circles.get(3).setFill(Color.WHITE);
        circles.get(4).setFill(Color.WHITE);
    }

    void retry(){
        if(checkInternet()){
            mainPane.setDisable(false);
            connectionError.setVisible(false);
            connectionError.setDisable(true);
        }
    }

    @FXML
    void registering(){
        if(register.isSelected()){
            registerPassField.setVisible(true);
            registerPassField.setDisable(false);
            passField.setDisable(true);
        }else{
            registerPassField.setVisible(false);
            registerPassField.setDisable(true);
            passField.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!checkInternet()){
            mainPane.setDisable(true);
        }
        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);
        circles.add(circle4);
        circles.add(circle5);
        register.setSelected(false);

        loginTF.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().length() > 0)
                if(!Character.isAlphabetic(change.getText().charAt(0))){
                    change.setText("");
                }
            return change;
        }));

        passField.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().length() > 0)
                if(!Character.isAlphabetic(change.getText().charAt(0)) && !Character.isDigit(change.getText().charAt(0))){
                    change.setText("");
                }
            return change;
        }));

    }
}
