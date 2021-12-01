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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller extends Admin implements Initializable {

    String nickname = "";
    String passw = "";
    String currentChatUser = "";
    int score;
    int temp, temp3;
    int msgN;
    int currentChatID;
    int timer = 10;
    Rectangle chatBG;

    ArrayList<Rectangle> rects = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();
    ArrayList<Label> descriptions = new ArrayList<>();
    ArrayList<Timeline> timelines = new ArrayList<>();

    @FXML
    AnchorPane mainPane;
    @FXML
    Rectangle playersRect, tasksRect, friendsRect, profRect, settingsRect, shopRect, homeRect, homeRectFront;
    @FXML
    Rectangle playersRectFront, tasksRectFront, friendsRectFront, profRectFront, settingsRectFront, shopRectFront;
    @FXML
    Label timeLabel, lvlLabel, nickLabel, lengthLabel, profN, selectChatLabel, msgErrorLabel;
    @FXML
    Label titleLabel, titleLabel1, titleLabel2, selectedUser, selectedUserStatus, userNotFoundLabel;
    @FXML
    AnchorPane adminMenu ,sqlMenu, playersMenu, tasksMenu, chatMenu, gamesMenu;
    @FXML
    Button adminMenuB, adminGiveAdminB, adminTakeAdminB, sendMsgB, refreshLeaderboardB;
    @FXML
    Button playersMenuB, tasksMenuB, friendsMenuB, profMenuB, gamesMenuB, shopMenuB, homeMenuB;
    @FXML
    ProgressBar actPrg;
    @FXML
    ScrollPane scrollPane;
    @FXML
    ScrollPane onlinePlayersSPane, chatScrollPane, leaderboardScrollPane;
    @FXML
    AnchorPane tasks_content, onlinePlayersAPane, chatsAP, chatContent, leaderboardContent;
    @FXML
    ColorPicker color_picker;
    @FXML
    TextField adminGiftTF, descriptionTF, adminAddTF, msgTF, addChatTF;
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
        score = sql("getScore", nickname);
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

    }//First function

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
        firstCircle.setStroke(Color.BLACK);
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
                circle.setStroke(Color.BLACK);
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

    void refreshChat(String nickname2){
        String newStatus = getStatus(nickname2);
        if (!newStatus.equals(selectedUserStatus.getText())){
            selectedUserStatus.setText(newStatus);
        }

        ArrayList<ArrayList<String>> messages = getMessages(nickname, nickname2);

        if(messages.size() > msgN && msgN >= 9){
            chatBG.setHeight(chatBG.getHeight() + (messages.size() - msgN) * 40);
            chatScrollPane.setVvalue(1.0);
        }
        int dif = messages.size() - msgN;
        if (dif > 0){
            Timeline newMsg = new Timeline(new KeyFrame(Duration.millis(100), e -> {
                Rectangle rect = new Rectangle();
                Label label = new Label();
                String msg = messages.get(msgN).get(1);
                rect.setFill(Color.WHITE);
                rect.setStrokeWidth(1);
                rect.setStroke(Color.BLACK);
                rect.setArcWidth(10);
                rect.setArcHeight(10);
                label.setText(msg);
                rect.setLayoutX(4);
                rect.setLayoutY(15 + 40 * msgN);
                rect.setWidth(24 + 6 * msg.length());
                rect.setHeight(30);
                label.setLayoutX(14);
                label.setPrefWidth(327);
                label.setAlignment(Pos.CENTER_LEFT);
                label.setLayoutY(rect.getLayoutY() + 7);
                label.setFont(new Font(12));
                chatContent.getChildren().add(rect);
                chatContent.getChildren().add(label);
                chatScrollPane.setVvalue(1.0);
                msgN++;
            }));
            newMsg.setAutoReverse(false);
            newMsg.setCycleCount(dif);
            newMsg.play();
        }
    }

    void showChat(String nickname2){
        currentChatUser = nickname2;
        currentChatID = getChatID(nickname, nickname2);
        chatContent.getChildren().clear();
        ArrayList<ArrayList<String>> messages = getMessages(nickname, nickname2);
        Color clr = Color.rgb(183, 255, 192);
        chatBG = new Rectangle();
        chatBG.setFill(clr);
        chatBG.setLayoutY(0);
        chatBG.setLayoutX(0);
        chatBG.setHeight(370);
        chatBG.setWidth(370);
        chatBG.setStrokeWidth(0);
        if(messages.size() > 9){
            chatBG.setHeight(chatBG.getHeight() + (messages.size() - 9) * 40);
        }
        chatContent.getChildren().add(chatBG);
        selectChatLabel.setVisible(false);
        selectChatLabel.setDisable(true);
        selectedUser.setText(nickname2);
        selectedUserStatus.setText(getStatus(nickname2));
        selectedUserStatus.setUnderline(true);
        msgN = messages.size();
        if(messages.size() > 0){
            temp = 0;
            Timeline msgTML = new Timeline(new KeyFrame(Duration.millis(40), e -> {
                String sender = messages.get(temp).get(0);
                Rectangle rect = new Rectangle();
                Label label = new Label();
                rect.setFill(Color.WHITE);
                rect.setStrokeWidth(1);
                rect.setStroke(Color.BLACK);
                rect.setArcWidth(10);
                rect.setArcHeight(10);
                label.setText(messages.get(temp).get(1));
                int length = messages.get(temp).get(1).length();
                if(sender.equals(nickname)){
                    rect.setLayoutX(326 - 6 * length);
                    rect.setLayoutY(15 + 40 * temp);
                    rect.setWidth(24 + 6 * length);
                    rect.setHeight(30);
                    label.setLayoutX(14);
                    label.setPrefWidth(327);
                    label.setAlignment(Pos.CENTER_RIGHT);
                }else{
                    rect.setLayoutX(4);
                    rect.setLayoutY(15 + 40 * temp);
                    rect.setWidth(24 + 6 * length);
                    rect.setHeight(30);
                    label.setLayoutX(14);
                    label.setPrefWidth(327);
                    label.setAlignment(Pos.CENTER_LEFT);
                }
                label.setLayoutY(rect.getLayoutY() + 7);
                label.setFont(new Font(12));
                chatContent.getChildren().add(rect);
                chatContent.getChildren().add(label);
                chatScrollPane.setVvalue(1.0);
                temp++;
            }));
            msgTML.setCycleCount(messages.size());
            msgTML.setAutoReverse(false);
            msgTML.play();
        }
        if(timelines.size() > 0){
            timelines.get(0).stop();
            timelines.remove(0);
        }
        Timeline rfChat = new Timeline(new KeyFrame(Duration.seconds(10), event -> refreshChat(nickname2)));
        rfChat.setCycleCount(Timeline.INDEFINITE);
        rfChat.setAutoReverse(false);
        rfChat.play();
        timelines.add(rfChat);
    }

    void refreshChats(){
        chatsAP.getChildren().clear();
        ArrayList<String> chats = getChats(nickname);
        if (chats.size() > 0){
            temp = 0;
            Timeline chatsTML = new Timeline(new KeyFrame(Duration.millis(10), e ->{
                Button chatButton = new Button();
                chatButton.setLayoutX(5);
                chatButton.setLayoutY(temp * 30 + 10);
                chatButton.setPrefWidth(127);
                chatButton.setPrefHeight(25);
                chatButton.setText(chats.get(temp));
                chatButton.setOnAction(ev -> showChat(chatButton.getText()));
                chatsAP.getChildren().add(chatButton);
                temp++;
            }));
            chatsTML.setCycleCount(chats.size());
            chatsTML.setAutoReverse(false);
            chatsTML.play();
        }
    }

    @FXML
    void refreshLeaderboard(){
        sql("Update", nickname, score);
        temp3 = 0;
        refreshLeaderboardB.setDisable(true);
        leaderboardContent.getChildren().clear();
        ArrayList<ArrayList<String>> leaderboard = getLeaderboard();
        System.out.println(leaderboard.size());
        if(leaderboard.size() > 0) {
            Timeline lbTml = new Timeline(new KeyFrame(Duration.millis(50), e-> {
                Label nick = new Label();
                nick.setLayoutX(6);
                nick.setLayoutY(temp3 * 20 + 10);
                nick.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
                nick.setText((temp3 + 1) + "." + leaderboard.get(temp3).get(0));
                nick.setPrefWidth(172);
                nick.setAlignment(Pos.CENTER_LEFT);

                Label score = new Label();
                score.setLayoutX(180);
                score.setLayoutY(nick.getLayoutY());
                score.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
                score.setText(String.format("%d", defLevel(Integer.parseInt(leaderboard.get(temp3).get(1)))) + " level");
                score.setPrefWidth(111);
                score.setAlignment(Pos.CENTER);

                Label title = new Label();
                String rank = leaderboard.get(temp3).get(2);
                String custom_rank = leaderboard.get(temp3).get(3);
                String custom_color = leaderboard.get(temp3).get(4);
                title.setFont(Font.font("Verdana", FontPosture.ITALIC, 12));
                title.setLayoutX(290);
                title.setLayoutY(nick.getLayoutY());
                title.setPrefWidth(200);
                title.setAlignment(Pos.CENTER);
                if(rank.equals("CUSTOM")){
                    int r = 166;
                    int g = 26;
                    int b = 188;
                    if(!custom_color.equals("default")){
                        r = Integer.parseInt(custom_color.substring(0,3));
                        g = Integer.parseInt(custom_color.substring(5,8));
                        b = Integer.parseInt(custom_color.substring(10));
                    }
                    Color titleColor = Color.rgb(r, g, b);
                    title.setFont(new Font(13));
                    title.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, 12));
                    title.setText(custom_rank);
                    title.setTextFill(titleColor);
                }else{
                    title.setFont(new Font(13));
                    title.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.ITALIC, 12));
                    title.setText(rank);
                }
                leaderboardContent.getChildren().add(nick);
                leaderboardContent.getChildren().add(score);
                leaderboardContent.getChildren().add(title);
                leaderboardContent.setVisible(true);
                temp3++;
                if(temp3 == leaderboard.size()){
                    refreshLeaderboardB.setDisable(false);
                }
            }));
            lbTml.setAutoReverse(false);
            lbTml.setCycleCount(leaderboard.size());
            lbTml.play();
        }

    }

    @FXML
    void sendMsg() throws SQLException {

        Label label = new Label();
        Rectangle rect = new Rectangle();
        if(msgTF.getText().length() != 0)
            rect.setFill(Color.WHITE);
            rect.setStrokeWidth(1);
            rect.setStroke(Color.BLACK);
            rect.setArcWidth(10);
            rect.setArcHeight(10);
            label.setText(msgTF.getText().trim());
            int length = msgTF.getText().trim().length();
            if(length < 41 && length > 0){
                rect.setLayoutX(326 - 6 * length);
                rect.setLayoutY(15 + 40 * msgN - 1);
                rect.setWidth(24 + 6 * length);
                rect.setHeight(30);
                label.setLayoutX(14);
                label.setPrefWidth(327);
                label.setAlignment(Pos.CENTER_RIGHT);

                label.setLayoutY(rect.getLayoutY() + 7);
                label.setFont(new Font(12));
                chatContent.getChildren().add(rect);
                chatContent.getChildren().add(label);
                chatScrollPane.setVvalue(1.0);
                addMessage(nickname, currentChatID, msgTF.getText().trim());
                ArrayList<ArrayList<String>> messages = getMessages(nickname, currentChatUser);
                if(messages.size() > msgN && msgN >= 9){
                    chatBG.setHeight(chatBG.getHeight() + (messages.size() - msgN) * 40);
                    chatScrollPane.setVvalue(1.0);
                }
                msgErrorLabel.setVisible(false);
                msgTF.setText("");
                msgN++;
            }else{
                if(length > 40){
                    msgErrorLabel.setVisible(true);
                    msgErrorLabel.setText("Max length is 40. Now - " + length + ".");
                }
            }
    }

    @FXML
    void addChatA() throws SQLException {
        if(addChatTF.getText().length() > 0){
            if(checkUser(addChatTF.getText().trim())){
                addChat(nickname, addChatTF.getText().trim());
                refreshChats();
                userNotFoundLabel.setVisible(false);
            }else{
                userNotFoundLabel.setVisible(true);
            }
        }
    }

    @FXML
    void adminDeleteUser(){

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
    void showFriendsMenu() { // CHAT MENU
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();
        friendsMenuB.setDisable(true);
        friendsRectFront.setVisible(true);
        chatMenu.setVisible(true);
        chatMenu.setDisable(false);
        refreshChats();
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
    void showGamesMenu(){
        disableAllMenuPanes();
        clearFront();
        ableAllButtons();

        settingsRectFront.setVisible(true);
        gamesMenu.setVisible(true);
        gamesMenu.setDisable(false);
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
        chatMenu.setDisable(true);
        chatMenu.setVisible(false);
        gamesMenu.setVisible(false);
        gamesMenu.setDisable(true);
    }

    void ableAllButtons(){
        playersMenuB.setDisable(false);
        friendsMenuB.setDisable(false);
        profMenuB.setDisable(false);
        tasksMenuB.setDisable(false);
        gamesMenuB.setDisable(false);
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
        userNotFoundLabel.setVisible(false);
        color_picker.setValue(Color.LIGHTBLUE);
        actPrg.setStyle("-fx-accent: rgb(0, 0, 80)");
        msgTF.setTextFormatter(new TextFormatter<>(change -> {
            if(change.getText().length() > 0)
                if(change.getText().equals("'")){
                    change.setText("");
                }else if(change.getText().equals("\"")){
                    change.setText("");
                }else if(change.getText().equals("\\")){
                    change.setText("");
                }
            return change;
        }));

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
