package grs.testepam.snake.model;

import javafx.scene.paint.Color;

/**
 * Created by gressmc on 17/07/15.
 */
public final class Fog extends GameObject {

    public enum ColorFog {
        Green(Color.GREEN), Red(Color.RED), Blue(Color.BLUE);

        private Color color;

        ColorFog(Color color){
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

    }

    private BodyPoint bodyFog;
    private Direction direction;
    private Thread fogThread;
    private ColorFog colorFog;
    private boolean suspendFlag;
    private boolean isAlive;
    private int step;


    public Fog(int x, int y, Direction direction) {

        this.direction = direction;
        this.suspendFlag = false;
        this.isAlive = true;
        bodyFog = new BodyPoint(x, y);
        step = 0;

        int rand = (int)(Math.random() * 1000 % 3);
        this.colorFog = ColorFog.values()[rand];
        start();
        System.out.println("New Fog");
    }

    @Override
    public void move() {

        int rand = (int)(Math.random() * 1000 % 4);
        direction = Direction.values()[rand];

        bodyFog.setX(bodyFog.getX() + direction.dX());
        bodyFog.setY(bodyFog.getY() + direction.dY());
    }

    @Override
    public void run() {
        try{
            while (isAlive){
                move();
                step++;

                Thread.sleep(1000);
                synchronized (this){
                    while (suspendFlag) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e){
            System.out.println("Fog прерван");
        }
        System.out.println("Fog завершен");
    }

    public int getStep() {
        return step;
    }

    public BodyPoint getBodyFog() {
        return bodyFog;
    }

    public void start() {
        this.fogThread = new Thread(this);
        this.fogThread.start();
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public ColorFog getColorFog() {
        return colorFog;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
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
        fogThread.interrupt();
    }
}
