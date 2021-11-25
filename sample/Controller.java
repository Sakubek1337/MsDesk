package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller extends Admin implements Initializable {

    String nickname = "Sakubek";
    int score;

    ArrayList<Rectangle> rects = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<Label> descriptions = new ArrayList<>();

    int temp;

    @FXML
    AnchorPane mainPane;
    @FXML
    Label timeLabel;
    @FXML
    Label lvlLabel;
    @FXML
    Label nickLabel;
    @FXML
    Label profN;
    @FXML
    Label lengthLabel;
    @FXML
    AnchorPane adminMenu;
    @FXML
    AnchorPane sqlMenu;
    @FXML
    Button adminMenuB;
    @FXML
    Button adminGiveAdminB;
    @FXML
    Button adminTakeAdminB;
    @FXML
    ProgressBar actPrg;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane tasks_content;
    @FXML
    ColorPicker color_picker;
    @FXML
    TextField adminAddTF;
    @FXML
    TextField adminGiftTF;
    @FXML
    TextField descriptionTF;
    @FXML
    TextArea inputArea;
    @FXML
    Rectangle levelPrg;
    @FXML
    Rectangle errorLengthRect;
    @FXML
    MenuButton menu;
    @FXML
    CheckMenuItem easy;
    @FXML
    CheckMenuItem medium;
    @FXML
    CheckMenuItem hard;
    @FXML
    CheckMenuItem expert;
    @FXML
    CheckMenuItem imp;

    @FXML
    void add() throws SQLException {
        Rectangle rect = new Rectangle(470, 50);
        if (rects.size() < 1){
            rect.setLayoutY(10);
        }else{
            Rectangle last = rects.get(rects.size() - 1);
            rect.setLayoutY(last.getLayoutY() + 10 + last.getHeight());
            System.out.println(last.getLayoutY());
            System.out.println(rects.size());
        }
        rect.setLayoutX(10);
        rect.setArcHeight(10);
        rect.setArcWidth(10);
        rect.setStrokeWidth(1);
        rect.setStroke(Color.BLACK);
        rect.setFill(color_picker.getValue());

        Button button = new Button();
        button.setFont(new Font(10));
        button.setText("Done");
        button.setLayoutX(437);
        button.setLayoutY(rect.getLayoutY() + 5);
        button.setCursor(Cursor.HAND);
        button.setOnAction(e -> delete(buttons.indexOf(button), "done"));

        Label label = new Label(setStars(menu));
        label.setFont(new Font(10));
        label.setPrefWidth(44);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(434);
        label.setLayoutY(button.getLayoutY() + 25);

        Label desc = new Label(setStars(menu));
        desc.setFont(new Font(14));
        desc.setPrefWidth(420);
        desc.setAlignment(Pos.CENTER_LEFT);
        desc.setLayoutX(rect.getLayoutX() + 10);
        desc.setLayoutY(rect.getLayoutY() + 10);
        desc.setPrefHeight(30);
        desc.setText(descriptionTF.getText());
        if(descriptionTF.getText().length() <= 51 && descriptionTF.getText().length() >= 3){
            rects.add(rect);
            buttons.add(button);
            labels.add(label);
            descriptions.add(desc);
            tasks_content.getChildren().add(rect);
            tasks_content.getChildren().add(button);
            tasks_content.getChildren().add(label);
            tasks_content.getChildren().add(desc);
            check_border(false, 0);
            scrollPane.setVvalue(1.0);
            errorLengthRect.setVisible(false);
        }else{
            System.out.println("Length is not in borders.");
            System.out.println(descriptionTF.getText().length());
            errorLengthRect.setVisible(true);
        }

        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setOnCloseRequest(e -> {sql("Update", nickname, score);
            try {
                closeConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    void delete(int n, String type) {
        if (type.equals("done")) {
            score = addPoints(actPrg, labels.get(n).getText(), nickname, score);
            setLevel();
        }
        double vert = scrollPane.getVvalue();
        double h = rects.get(n).getHeight() + 10;
        check_border(true, h);
        tasks_content.getChildren().remove(rects.get(n));
        tasks_content.getChildren().remove(buttons.get(n));
        tasks_content.getChildren().remove(labels.get(n));
        tasks_content.getChildren().remove(descriptions.get(n));
        rects.remove(n);
        buttons.remove(n);
        labels.remove(n);
        descriptions.remove(n);
        if(rects.size() != 0 && rects.size() != n)
            temp = n;
            Timeline tml = new Timeline(new KeyFrame(Duration.millis(50), e -> {
                try{
                    rects.get(temp).setLayoutY(rects.get(temp).getLayoutY() - h);
                    buttons.get(temp).setLayoutY(buttons.get(temp).getLayoutY() - h);
                    labels.get(temp).setLayoutY(labels.get(temp).getLayoutY() - h);
                    tasks_content.setDisable(temp != rects.size() - 1);
                    temp++;
                    scrollPane.setVvalue(vert);
                }catch (Exception es){
                    System.out.println("Error blyat");
                }
            }));
            tml.setAutoReverse(false);
            tml.setCycleCount(rects.size() - n);
            if (rects.size() != n) {
                tml.play();
            }
    }

    void check_border(boolean deleting, double h){
        int last_index = rects.size() - 1;
        Rectangle last_rect = rects.get(last_index);
        double diff = last_rect.getHeight() + last_rect.getLayoutY() - tasks_content.getPrefHeight();
        if (!deleting){
            if(diff > 0){
                tasks_content.setPrefHeight(tasks_content.getHeight() + diff + 5);
            }
        }else{
            tasks_content.setPrefHeight(tasks_content.getHeight() - h);
        }
    }

    @FXML
    void setDifficulty(ActionEvent event){
        setDiff((CheckMenuItem) event.getSource(), menu);
    }

    void setLevel(){
        lvlLabel.setText("Level - " +  String.format("%d", defLevel(score)));
        levelPrg.setWidth(50 * per(score));
    }

    @FXML
    void showAdminMenu(){
        adminMenu.setDisable(!adminMenu.isDisable());
        adminMenu.setVisible(!adminMenu.isVisible());
        sqlMenu.setDisable(true);
        sqlMenu.setVisible(false);
    }

    @FXML
    void adminAddPoints() {
        try{
            int num = Integer.parseInt(adminAddTF.getText());
            score += num;
            if(score < 0) {
                score = 0;
            }
            setLevel();
            adminAddPointsC(nickname, score);

        }catch(Exception e){
            if (adminAddTF.getText().equals("reset")){
                score = 0;
                setLevel();
                adminAddPointsC(nickname, score);
                System.out.println("Deleted");
            }else{
                System.out.println("Nu i kak bukvy dobavblyat?");
            }
        }
    }

    @FXML
    void adminAddUser() {

    }

    @FXML
    void adminDeleteUser() {

    }

    @FXML
    void adminGift() {
        try{
            int num = Integer.parseInt(adminGiftTF.getText());
            score += num;
            if(score < 0) {
                score = 0;
            }
            setLevel();
            adminGiftC(nickname, score);

        }catch(Exception e){
            if (adminGiftTF.getText().equals("reset")){
                score = 0;
                setLevel();
                adminGiftC(nickname, score);
                System.out.println("Deleted");
            }else{
                System.out.println("Nu i kak bukvy dobavblyat?");
            }
        }
    }

    @FXML
    void adminGiveAdmin() {

    }

    @FXML
    void adminTakeAdmin() {

    }

    @FXML
    void showSQLMenu(){
        sqlMenu.setVisible(!sqlMenu.isVisible());
        sqlMenu.setDisable(!sqlMenu.isDisable());
        adminMenu.setDisable(true);
        adminMenu.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color_picker.setValue(Color.LIGHTBLUE);
        adminMenuB.setDisable(true);
        profN.setText(nickname.substring(0, 1).toUpperCase());
        actPrg.setStyle("-fx-accent: rgb(0, 0, 80)");
        nickLabel.setText(nickname);
        score = Integer.parseInt(sql("getScore", nickname));
        setLevel();
        addAdmin("Sakubek");
        if(adminCheck(nickname)){
            adminMenuB.setDisable(false);
        }
        if(!nickname.equals("Sakubek")){
            adminTakeAdminB.setDisable(true);
            adminGiveAdminB.setDisable(true);
        }

        Timeline tml = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(dtf.format(now));
        }));
        tml.setCycleCount(Timeline.INDEFINITE);
        tml.setAutoReverse(false);
        tml.play();
        System.out.println(inputArea.getText());
    }
}
