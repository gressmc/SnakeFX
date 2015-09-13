package grs.testepam.snake.controller;

import grs.testepam.snake.model.Direction;
import grs.testepam.snake.model.Fog;
import grs.testepam.snake.model.GameObject;
import grs.testepam.snake.model.Snake;
import grs.testepam.snake.view.SceneRenderer;
import javafx.scene.canvas.GraphicsContext;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;

import static grs.testepam.snake.Constants.WORLD_HEIGHT;
import static grs.testepam.snake.Constants.WORLD_WIDTH;

/**
 * Created by gressmc on 17/07/15.
 */
public class GameEngine {


    private Snake snake;
    private Fog fog;
    private SceneRenderer renderer;
    private AtomicBoolean running;

    public static boolean start;
    public static boolean pause;
    public static boolean stop;


    public GameEngine(){
        snake = new Snake(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, Direction.RIGHT);
        renderer = new SceneRenderer(snake);
        placeFog();

        running = new AtomicBoolean(true);
    }

    private void placeFog() {
        int x = 1 + (int)(Math.random() * WORLD_WIDTH);
        int y = 1 + (int)(Math.random() * WORLD_HEIGHT);
        while (!isCellEmpty(x, y)) {
            if (x < WORLD_WIDTH) {
                x++;
            } else {
                if (y < WORLD_HEIGHT) {
                    x = 1;
                    y++;
                } else {
                    x = 1;
                    y = 1;
                }
            }
        }
        fog = new Fog(x, y, Direction.LEFT);
        renderer.setFog(fog);
    }

    private boolean isCellEmpty(int x, int y) {
        for (GameObject.BodyPoint bodyPart : snake.getBody()) {
            if (bodyPart.getX() == x && bodyPart.getY() == y) {
                return false;
            }
        }
        return true;
    }

    private synchronized boolean isGameOver() {
        if (snake.getBody().size() == WORLD_WIDTH * WORLD_HEIGHT) {
            return true;
        }

        for (GameObject.BodyPoint bodyPart : snake.getBody()) {
            if (bodyPart != snake.head() && snake.head().getX() == bodyPart.getX() && snake.head().getY() == bodyPart.getY()) {
                System.out.println("O-lya-lya");
                return true;
            }
        }

        return false;
    }

    private AtomicBoolean getRunning() {
        return running;
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            snake.resume();
            fog.resume();
        }
    }

    public void pause() {
        if (running.compareAndSet(true, false)) {
            snake.pause();
            fog.pause();
        }
    }

    public void stop() {
        snake.stop();
        fog.stop();
    }

    public Snake getSnake() {
        return snake;
    }

    public void processEvent(Event event) {
        MouseEvent mouseEvent = (MouseEvent)event;
        switch (mouseEvent.getButton()) {
            case SECONDARY:
                int nowDirInx = snake.getDirection().ordinal() >= 1 ? snake.getDirection().ordinal() : 4;
                Direction direction = Direction.values()[nowDirInx - 1];
                snake.setDirection(direction);
                break;
            case PRIMARY:
                int nowDirInx2 = snake.getDirection().ordinal() <= 2 ? snake.getDirection().ordinal() : -1;
                Direction direction2 = Direction.values()[nowDirInx2 + 1];
                snake.setDirection(direction2);
                break;
        }
    }

    public synchronized void update() {
        GameObject.BodyPoint head = snake.head();

        if (!getRunning().get()){

        }

        if (fog.getColorFog().equals(Fog.ColorFog.Blue) && fog.getStep() >= 10){
            fog.setIsAlive(false);
            placeFog();
        }

        if (isGameOver()) {
            stop();
        }
        boundary(head);
        boundary(fog.getBodyFog());

//        if (!isCellEmpty(fog.getBodyFog().getX() + fog.getDirection().dX(), fog.getBodyFog().getY() + fog.getDirection().dY())){
//            int directionInx = fog.getDirection().ordinal() >= 2 ? snake.getDirection().ordinal() : 5;
//            Direction newDirection = Direction.values()[directionInx - 2];
//            fog.setDirection(newDirection);
//        }

        if (head.getX() == fog.getBodyFog().getX() && head.getY() == fog.getBodyFog().getY()) {

            switch (fog.getColorFog().toString()){
                case "Green" :
                    snake.count++;
                    changeGreen();
                    break;
                case "Red" :
                    snake.count += 2;
                    changeRed();
                    break;
                case "Blue" :
                    stop();
                    break;
            }
        }
    }

    private void boundary(GameObject.BodyPoint body) {
        if (body.getX() < 0) {
            body.setX(WORLD_WIDTH - 1);
        }
        if (body.getX() >= WORLD_WIDTH) {
            body.setX(0);
        }
        if (body.getY() < 0) {
            body.setY(WORLD_HEIGHT - 1);
        }
        if (body.getY() >= WORLD_HEIGHT) {
            body.setY(0);
        }
    }

    private void changeGreen(){
        java.util.List<GameObject.BodyPoint> body = snake.getBody();
        GameObject.BodyPoint lastPart = body.get(body.size() - 1);
        snake.addLast(lastPart);
        fog.setIsAlive(false);
        placeFog();
    }

    private void changeRed(){
        snake.removeLast();
        fog.setIsAlive(false);
        placeFog();
    }

    public void render(GraphicsContext g) {
        renderer.render(g);
    }
}
