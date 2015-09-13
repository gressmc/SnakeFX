package grs.testepam.snake;

import grs.testepam.snake.controller.GameEngine;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Timeline timeline = new Timeline();

    private GraphicsContext gc;


    private Parent createContent() throws Exception{
        Pane root = new Pane();

        GameEngine engine = new GameEngine();

        Canvas canvas = new Canvas(448, 300);

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                engine.processEvent(event);

            }
        });

        Button startB = new Button("Start");
        Button pauseB = new Button("Pause");
        Button stopB = new Button("Stop");


        startB.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                engine.start();
            }
        });

        pauseB.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                engine.pause();
            }
        });

        stopB.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                engine.stop();
            }
        });

        Label label = new Label("0");
        label.fontProperty().setValue(new Font("Default", 20));

        HBox box = new HBox();
        box.getChildren().addAll(startB, pauseB, stopB, label);
        box.setSpacing(10);
        box.setPadding(new Insets(10));

        BorderPane pane = new BorderPane();
        pane.setBottom(box);
        pane.setTop(canvas);

        gc = canvas.getGraphicsContext2D();

        KeyFrame frame = new KeyFrame(Duration.seconds(0.2), event -> {
            engine.update();
            engine.render(gc);
            label.setText(String.valueOf(engine.getSnake().count));
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().add(pane);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
        timeline.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
