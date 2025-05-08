package main;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Constants {
    public static final Vector2 windowDimensions = new Vector2(700, 500);
    public static final int BORDER_WIDTH = 3;
    public static final float BALL_SPEED = 150;
    public static float MOVEMENT_SPEED = 300;

    // HeartsPanel class
    public static final int MAX_LIFE_NUM = 3;
    public static int LIFE_NUM = 3; // Number of player lives
    public static final Vector2 panelTopLeft = new Vector2(windowDimensions.x() - 200, 10);
    public static final Vector2 panelSize = new Vector2(200, 40);
}
