package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A ball object that bounces off other objects and plays a sound on collision.
 * Keeps track of the number of collisions it experienced.
 */
public class Ball extends GameObject {
	private Sound collisionSound;
	private int collisionCounter = 0;

	/**
	 * Construct a new Ball instance.
	 *
	 * @param topLeftCorner  Top-left position of the ball.
	 * @param dimensions     Size of the ball.
	 * @param renderable     Visual representation.
	 * @param collisionSound Sound to play on collision.
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions,
				Renderable renderable, Sound collisionSound) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision){
		super.onCollisionEnter(other, collision);

		// Flip velocity based on collision normal
		Vector2 newVal = getVelocity().flipped(collision.getNormal());
		setVelocity(newVal);

		collisionCounter++;
		collisionSound.play();
	}

	/**
	 * Returns the number of collisions experienced by the ball.
	 *
	 * @return Collision count.
	 */
	public int getCollisionCounter() {
		return collisionCounter;
	}
}
