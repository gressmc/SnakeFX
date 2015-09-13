package grs.testepam.snake.model;

/**
 * Created by gressmc on 17/07/15.
 */
public abstract class GameObject implements Runnable {

    abstract void move();

    public class BodyPoint {
        private int x;
        private int y;

        public BodyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}

