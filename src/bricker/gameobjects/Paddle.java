package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.main.Constants;

import java.awt.event.KeyEvent;

/**
 * A player-controlled paddle that moves horizontally based on user input.
 */
public class Paddle extends GameObject {
	private static final float MOVEMENT_SPEED = Constants.MOVEMENT_SPEED;
	private static final float WINDOW_WIDTH = Constants.windowDimensions.x();

	private UserInputListener inputListener;

	/**
	 * Constructs a Paddle instance.
	 *
	 * @param topLeftCorner Top-left position.
	 * @param dimensions    Paddle size.
	 * @param renderable    Paddle appearance.
	 * @param inputListener User input source.
	 */
	public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
				  UserInputListener inputListener) {
		super(topLeftCorner, dimensions, renderable);
		this.inputListener = inputListener;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		Vector2 movementDir = new Vector2(0, 0);
		float paddleLeft = getTopLeftCorner().x();
		float paddleRight = paddleLeft + getDimensions().x();

		if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && paddleLeft > 0)
			movementDir = movementDir.add(Vector2.LEFT);

		if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && paddleRight < WINDOW_WIDTH)
			movementDir = movementDir.add(Vector2.RIGHT);

		setVelocity(movementDir.mult(MOVEMENT_SPEED));
	}
}
