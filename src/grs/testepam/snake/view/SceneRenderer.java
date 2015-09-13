package grs.testepam.snake.view;

import javafx.scene.canvas.GraphicsContext;

import grs.testepam.snake.model.*;
import javafx.scene.paint.Color;

import static grs.testepam.snake.Constants.*;

/**
 * Created by gressmc on 17/07/15.
 */
public class SceneRenderer {

    private Snake snake;
    private Fog fog;

    public SceneRenderer(Snake snake){
        this.snake = snake;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public void render(GraphicsContext g) {
        drawField(g);
        drawSnake(g);
        drawFog(g);

    }

    private void drawField(GraphicsContext g){
        g.setFill(Color.GRAY);
        g.setStroke(javafx.scene.paint.Color.YELLOW);
        g.fillRect(0, 0, WORLD_WIDTH*CELL_SIZE, WORLD_HEIGHT*CELL_SIZE);

        for (int column = CELL_SIZE; column < WORLD_WIDTH*CELL_SIZE; column += CELL_SIZE) {
            g.strokeLine(column, 0, column, WORLD_HEIGHT*CELL_SIZE);
        }

        for (int row = CELL_SIZE; row < WORLD_HEIGHT*CELL_SIZE; row += CELL_SIZE){
            g.strokeLine(0, row, WORLD_WIDTH * CELL_SIZE, row);
        }
    }

    private void drawSnake(GraphicsContext g) {

        for (GameObject.BodyPoint bodyPart: snake.getBody()){

            g.setFill(javafx.scene.paint.Color.YELLOW);

            if (bodyPart == snake.head()) {
                g.fillOval(bodyPart.getX() * CELL_SIZE + 3, bodyPart.getY() * CELL_SIZE + 3, CELL_SIZE - 6 , CELL_SIZE - 6);
            } else if (bodyPart == snake.tail()){
                g.fillOval(bodyPart.getX() * CELL_SIZE + CELL_SIZE / 3, bodyPart.getY() * CELL_SIZE + CELL_SIZE / 3, CELL_SIZE - CELL_SIZE * 2 / 3, CELL_SIZE - CELL_SIZE * 2 / 3);
            } else {
                g.fillOval(bodyPart.getX() * CELL_SIZE + CELL_SIZE / 4, bodyPart.getY() * CELL_SIZE + CELL_SIZE / 4, CELL_SIZE - CELL_SIZE / 2, CELL_SIZE - CELL_SIZE / 2);
            }
        }
    }

    private void drawFog(GraphicsContext g) {
        g.setFill(fog.getColorFog().getColor());

        g.fillOval(fog.getBodyFog().getX() * CELL_SIZE + 3, fog.getBodyFog().getY() * CELL_SIZE + 3, CELL_SIZE - 6, CELL_SIZE - 6);
    }
}
