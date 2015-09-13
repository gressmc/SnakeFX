package grs.testepam.snake.model;

/**
 * Created by gressmc on 17/07/15.
 */
public enum Direction {

    UP, RIGHT, DOWN, LEFT;

    public int dX() {
        switch (this) {
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
            default:
                return 0;
        }
    }
    public int dY() {
        switch (this) {
            case UP:
                return 1;
            case DOWN:
                return -1;
            default:
                return 0;
        }
    }
}