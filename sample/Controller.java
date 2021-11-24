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
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller extends Extra implements Initializable {

    ArrayList<Rectangle> rects = new ArrayList<>();
    ArrayList<Button> buttons = new ArrayList<>();
    ArrayList<Label> labels = new ArrayList<>();

    int temp;

    @FXML
    Label timeLabel;
    @FXML
    ProgressBar actPrg;
    @FXML
    ScrollPane scrollPane;
    @FXML
    AnchorPane tasks_content;
    @FXML
    ColorPicker color_picker;
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
        button.setOnAction(e -> delete(buttons.indexOf(button)));

        Label label = new Label(setStars(menu));
        label.setFont(new Font(10));
        label.setPrefWidth(44);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(434);
        label.setLayoutY(button.getLayoutY() + 25);

        rects.add(rect);
        buttons.add(button);
        labels.add(label);
        tasks_content.getChildren().add(rect);
        tasks_content.getChildren().add(button);
        tasks_content.getChildren().add(label);
        check_border(false, 0);
        scrollPane.setVvalue(1.0);
    }

    void delete(int n) {
        addPoints(actPrg, labels.get(n).getText());
        double vert = scrollPane.getVvalue();
        double h = rects.get(n).getHeight() + 10;
        check_border(true, h);
        tasks_content.getChildren().remove(rects.get(n));
        tasks_content.getChildren().remove(buttons.get(n));
        tasks_content.getChildren().remove(labels.get(n));
        rects.remove(n);
        buttons.remove(n);
        labels.remove(n);
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
            tml.play();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color_picker.setValue(Color.LIGHTBLUE);
        actPrg.setStyle("-fx-accent: rgb(0, 0, 80)");
        Timeline tml = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(dtf.format(now));
        }));
        tml.setCycleCount(Timeline.INDEFINITE);
        tml.setAutoReverse(false);
        tml.play();
    }
}
