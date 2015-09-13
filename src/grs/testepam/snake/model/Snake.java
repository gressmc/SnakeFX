package grs.testepam.snake.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gressmc on 17/07/15.
 */
public final class Snake extends GameObject {

    private List<BodyPoint> body;
    private Direction direction;
    private Thread snakeThread;
    private boolean suspendFlag;
    private boolean isAlive;

    public int count = 0;

    public Snake(int x, int y, Direction direction) {
        this.direction = direction;
        this.suspendFlag = false;
        this.isAlive = true;

        body = new ArrayList<>();
        body.add(new BodyPoint(x, y));
        body.add(new BodyPoint(x - direction.dX(), y - direction.dY()));
        body.add(new BodyPoint(x - direction.dX() * 2, y - direction.dY() * 2));

        snakeThread = new Thread(this);
        snakeThread.start();
    }

    public void addLast(BodyPoint bodyPoint) {
        body.add(new BodyPoint(bodyPoint.getX(), bodyPoint.getY()));
    }

    public void removeLast() {
        if (body.size() > 3){
            body.remove(body.size() - 1);
        }
    }

    @Override
    public void move() {
        moveBody();
        moveHead();
    }

    @Override
    public void run() {

        try{
            while (isAlive){
                move();
                Thread.sleep(300);
                synchronized (this){
                    while (suspendFlag) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e){
            System.out.println("Snake прерван");
        }
        System.out.println("Snake завершен");
    }

    private void moveBody() {
        for (int i = body.size() - 1; i > 0; i--){
            BodyPoint prev = body.get(i-1);
            body.get(i).setX(prev.getX());
            body.get(i).setY(prev.getY());
        }
    }

    private void moveHead() {
        head().setX(head().getX() + direction.dX());
        head().setY(head().getY() + direction.dY());
    }

    public BodyPoint head() {
        return body.get(0);
    }

    public BodyPoint tail() {
        return body.get(body.size() - 1);
    }

    public List<BodyPoint> getBody() {
        return body;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public synchronized void pause(){
        suspendFlag = true;
    }

    public synchronized void resume(){
        suspendFlag = false;
        notify();
    }
    public synchronized void stop(){
        suspendFlag = false;
        snakeThread.interrupt();
    }
}
