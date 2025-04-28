package gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Ball extends GameObject {

	private Sound collisionSound;
	private  int collisionCounter = 0;

	/**
	 * Construct a new GameObject instance.
	 *
	 * @param topLeftCorner Position of the object, in window coordinates (pixels).
	 *                      Note that (0,0) is the top-left corner of the window.
	 * @param dimensions    Width and height in window coordinates.
	 * @param renderable    The renderable representing the object. Can be null, in which case
	 *                      the GameObject will not be rendered.
	 */
	public Ball(Vector2 topLeftCorner, Vector2 dimensions,
				Renderable renderable, Sound collisionSound) {
		super(topLeftCorner, dimensions, renderable);
		this.collisionSound = collisionSound;
	}

	@Override
	public void onCollisionEnter(GameObject other, Collision collision){
		super.onCollisionEnter(other,collision);
		Vector2 newVal = getVelocity().flipped(collision.getNormal());
		setVelocity(newVal);
		// Add one collision to the counter
		this.collisionCounter++;
		// Play sound when collision
		collisionSound.play();
	}

	public int getCollisionCounter() {
		return collisionCounter;
	}
}
