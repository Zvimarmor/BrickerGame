package gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import main.Constants;

import java.awt.event.KeyEvent;


public class Paddle extends GameObject {
	private static float MOVEMENT_SPEED = Constants.MOVEMENT_SPEED;
	private float WINDOW_WIDTH = Constants.windowDimensions.x();
	private UserInputListener inputListener;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 * @param inputListener Listens to the user keyboard.
	 */
	public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				  UserInputListener inputListener) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDir = new Vector2(0,0);

		float paddleLeft = this.getTopLeftCorner().x();
		float paddleRight = this.getTopLeftCorner().x() + this.getDimensions().x();

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && paddleLeft > 0){
			movementDir = movementDir.add(Vector2.LEFT);
		}
		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && paddleRight < WINDOW_WIDTH) {
			movementDir = movementDir.add(Vector2.RIGHT);
		}
		setVelocity(movementDir.mult(MOVEMENT_SPEED));

	}
}
