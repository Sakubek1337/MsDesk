package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller extends Admin implements Initializable {

    String nickname = "";
    String passw = "";
    int score;

    ArrayList<Rectangle> rects = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<Label> descriptions = new ArrayList<>();

    int temp;
    int timer = 10;

    @FXML
    AnchorPane mainPane;
    @FXML
    Rectangle playersRect, tasksRect, friendsRect, profRect, settingsRect, shopRect, homeRect, homeRectFront;
    @FXML
    Rectangle playersRectFront, tasksRectFront, friendsRectFront, profRectFront, settingsRectFront, shopRectFront;
    @FXML
    Label timeLabel, lvlLabel, nickLabel, lengthLabel, profN;
    @FXML
    Label titleLabel, titleLabel1, titleLabel2;
    @FXML
    AnchorPane adminMenu ,sqlMenu, playersMenu, tasksMenu;
    @FXML
    Button adminMenuB, adminGiveAdminB, adminTakeAdminB;
    @FXML
    Button playersMenuB, tasksMenuB, friendsMenuB, profMenuB, settingsMenuB, shopMenuB, homeMenuB;
    @FXML
    ProgressBar actPrg;
    @FXML
    ScrollPane scrollPane;
    @FXML
    ScrollPane onlinePlayersSPane;
    @FXML
    AnchorPane tasks_content, onlinePlayersAPane;
    @FXML
    ColorPicker color_picker;
    @FXML
    TextField adminGiftTF, descriptionTF, adminAddTF;
    @FXML
    TextArea inputArea;
    @FXML
    Rectangle levelPrg, errorLengthRect;
    @FXML
    MenuButton menu;
    @FXML
    CheckMenuItem easy, medium, hard, expert, imp;

    @FXML
    void add(){
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
        button.setOnAction(e -> delete(buttons.indexOf(button), false));

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
    }

    void delete(int n, boolean deleting) {
        if (!deleting) {
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
                    descriptions.get(temp).setLayoutY(descriptions.get(temp).getLayoutY() - h);
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

    void setTitle(){
        ArrayList<String> title = getRank(nickname);
        if (title.get(0).trim().equals("CUSTOM")){
            int r = 166;
            int g = 26;
            int b = 188;
            if(!title.get(2).trim().equals("default")){
                r = Integer.parseInt(title.get(2).substring(0,3));
                g = Integer.parseInt(title.get(2).substring(5,8));
                b = Integer.parseInt(title.get(2).substring(10));
            }
            Color titleColor = Color.rgb(r, g, b);
            titleLabel.setFont(new Font(14));
            titleLabel1.setTextFill(titleColor);
            titleLabel2.setTextFill(titleColor);
            titleLabel1.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 14));
            titleLabel2.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.ITALIC, 14));
            String [] strings = title.get(1).trim().split(" ");
            if (strings.length > 1){
                titleLabel1.setText(strings[0]);
                titleLabel2.setText(strings[1]);
            }else{
                titleLabel1.setText(title.get(1));
            }
        }else{
            titleLabel.setFont(new Font(12));
            titleLabel1.setFont(Font.font("System", FontWeight.NORMAL, FontPosture.REGULAR, 12));
            titleLabel1.setText(title.get(0));
        }
    }

    void setLevel(){
        lvlLabel.setText("Level - " +  String.format("%d", defLevel(score)));
        levelPrg.setWidth(50 * per(score));
    }

    void getValues(String nick, String pass){
        nickname = nick;
        passw = pass;
        profN.setText(nickname.substring(0, 1).toUpperCase());
        nickLabel.setText(nickname);
        score = Integer.parseInt(sql("getScore", nickname));
        clearFront();
        ableAllButtons();
        disableAllMenuPanes();
        showPlayersMenu();
        setTitle();
        setLevel();
        makeOnline(nickname);
        addAdmin("Sakubek");
        String adminTitle = getTitle(nickname);
        if(adminTitle.equals("Admin") || adminTitle.equals("Owner")){
            adminMenuB.setDisable(false);
        }
        if(!nickname.equals("Sakubek")){
            adminTakeAdminB.setDisable(true);
            adminGiveAdminB.setDisable(true);
        }

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
            sql("Update", nickname, score);
            System.out.println("Points added");

        }catch(Exception e){
            if (adminAddTF.getText().equals("reset")){
                score = 0;
                setLevel();
                sql("Update", nickname, score);
                System.out.println("Deleted");
            }else{
                System.out.println("Nu i kak bukvy dobavblyat?");
            }
        }
    }

    @FXML
    void refreshPlayers(){
        onlinePlayersAPane.getChildren().clear();
        temp = 1;
        ArrayList<String> players = getOnlinePlayers(nickname);
        ArrayList<String> titles = getOnlinePlayersTitles(nickname);
        ArrayList<Label> labels = new ArrayList<>();
        Label firstNick = new Label();
        firstNick.setText(nickname);
        firstNick.setLayoutY(6);
        firstNick.setLayoutX(20);
        firstNick.setUnderline(true);
        firstNick.setFont(new Font(14));
        String title = getTitle(nickname);
        if (title.equals("Admin") || title.equals("Owner")){
            Label firstTitle = new Label();
            firstTitle.setText(title);
            firstTitle.setLayoutY(6);
            firstTitle.setLayoutX(200);
            firstTitle.setFont(new Font(14));
            firstTitle.setTextFill(Color.rgb(199, 0, 0));
            firstTitle.setAlignment(Pos.CENTER_RIGHT);
            onlinePlayersAPane.getChildren().add(firstTitle);
        }
        Circle firstCircle = new Circle();
        firstCircle.setFill(Color.LIMEGREEN);
        firstCircle.setStrokeWidth(1.0);
        firstCircle.setRadius(3);
        firstCircle.setLayoutX(13);
        firstCircle.setLayoutY(17);
        labels.add(firstNick);
        onlinePlayersAPane.getChildren().add(firstNick);
        onlinePlayersAPane.getChildren().add(firstCircle);

        Timeline onlinePlayersTML = new Timeline(new KeyFrame(Duration.millis(10), e ->{
            if (players.size() > 0){
                Label nextNick = new Label(players.get(temp - 1));
                nextNick.setFont(new Font(14));
                nextNick.setLayoutX(20);
                nextNick.setLayoutY(temp * 17 + 6);

                Circle circle = new Circle();
                circle.setFill(Color.LIMEGREEN);
                circle.setStrokeWidth(1.0);
                circle.setRadius(3);
                circle.setLayoutX(13);
                circle.setLayoutY((temp + 1) * 17);
                onlinePlayersAPane.getChildren().add(circle);

                if(titles.get(temp - 1).equals("Admin") || titles.get(temp - 1).equals("Owner")){
                    Label nextTitle = new Label();
                    nextTitle.setText(titles.get(temp - 1));
                    nextTitle.setLayoutY(nextNick.getLayoutY());
                    nextTitle.setLayoutX(200);
                    nextTitle.setTextFill(Color.rgb(199, 0, 0));
                    nextTitle.setAlignment(Pos.CENTER_RIGHT);
                    nextTitle.setFont(new Font(14));
                    onlinePlayersAPane.getChildren().add(nextTitle);
                }
                labels.add(nextNick);
                onlinePlayersAPane.getChildren().add(nextNick);
                temp++;
            }
        }));
        onlinePlayersTML.setCycleCount(players.size());
        onlinePlayersTML.setAutoReverse(false);
        onlinePlayersTML.play();
    }

    @FXML
    void adminAddUser() {

    }

    @FXML
    void adminDeleteUser() throws IOException {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setScene(new Scene(root, 700, 500));
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

    @FXML
    void showSettingsMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        settingsMenuB.setDisable(true);
        settingsRectFront.setVisible(true);
    }

    @FXML
    void showShopMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        shopMenuB.setDisable(true);
        shopRectFront.setVisible(true);
    }

    @FXML
    void showTasksMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        tasksMenuB.setDisable(true);
        tasksMenu.setVisible(true);
        tasksMenu.setDisable(false);
        tasksRectFront.setVisible(true);
    }

    @FXML
    void showFriendsMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        friendsMenuB.setDisable(true);
        friendsRectFront.setVisible(true);
    }

    @FXML
    void showHomeMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        homeRectFront.setVisible(true);
        homeMenuB.setDisable(true);
    }

    @FXML
    void showPlayersMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();

        refreshPlayers();
        playersRectFront.setVisible(true);
        playersMenu.setVisible(true);
        playersMenu.setDisable(false);
        playersMenuB.setDisable(true);
    }

    @FXML
    void showProfMenu() {
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        profRectFront.setVisible(true);
    }

    void clearFront(){
        playersRectFront.setVisible(false);
        friendsRectFront.setVisible(false);
        profRectFront.setVisible(false);
        tasksRectFront.setVisible(false);
        settingsRectFront.setVisible(false);
        shopRectFront.setVisible(false);
        homeRectFront.setVisible(false);
    }

    void disableAllMenuPanes(){
        tasksMenu.setDisable(true);
        tasksMenu.setVisible(false);
        playersMenu.setVisible(false);
        playersMenu.setDisable(true);

    }

    void ableAllButtons(){
        playersMenuB.setDisable(false);
        friendsMenuB.setDisable(false);
        profMenuB.setDisable(false);
        tasksMenuB.setDisable(false);
        settingsMenuB.setDisable(false);
        shopMenuB.setDisable(false);
        homeMenuB.setDisable(false);
    }

    void close() throws SQLException {
        makeOffline(nickname);
        sql("Update", nickname, score);
        closeConnection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color_picker.setValue(Color.LIGHTBLUE);
        actPrg.setStyle("-fx-accent: rgb(0, 0, 80)");

        Timeline tml = new Timeline(new KeyFrame(Duration.seconds(6), e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(dtf.format(now));
            timer--;
            if (timer == 0){
                timer = 10;
                refreshPlayers();
            }
        }));
        tml.setCycleCount(Timeline.INDEFINITE);
        tml.setAutoReverse(false);
        tml.play();
    }
}
