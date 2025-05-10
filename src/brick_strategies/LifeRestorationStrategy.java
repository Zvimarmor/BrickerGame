package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;
import gameobjects.HeartsPanel;
import main.Constants;

public class LifeRestorationStrategy implements CollisionStrategy {
	private final GameObjectCollection gameObjects;
	private final Counter bricksCounter;
	private final ImageReader imageReader;
//	private final GameObject mainPaddle;
	private final HeartsPanel heartsPanel;

	public LifeRestorationStrategy(GameObjectCollection gameObjects,
								   Counter bricksCounter,
								   ImageReader imageReader,
								   HeartsPanel heartsPanel) {
		this.gameObjects = gameObjects;
		this.bricksCounter = bricksCounter;
		this.imageReader = imageReader;
//		this.mainPaddle = mainPaddle;
		this.heartsPanel = heartsPanel;
	}

	@Override
	public void onCollision(GameObject brick, GameObject collider) {
		if (gameObjects.removeGameObject(brick, Layer.STATIC_OBJECTS)) {
			bricksCounter.decrement();
		}

		Renderable heartImage = imageReader.readImage("assets/heart.png", true);
		Vector2 heartSize = new Vector2(Constants.panelSize.y(), Constants.panelSize.y());
		Vector2 heartPos = brick.getCenter().subtract(heartSize.mult(0.5f));

		GameObject fallingHeart = new GameObject(heartPos, heartSize, heartImage) {
			@Override
			public boolean shouldCollideWith(GameObject other) {
				return "MainPaddle".equals(other.getTag());
			}

			@Override
			public void onCollisionEnter(GameObject other, Collision collision) {
				if ("MainPaddle".equals(other.getTag())) {
					if (heartsPanel.getLifeNum() < Constants.MAX_LIFE_NUM) {
						heartsPanel.addHeart(gameObjects);
					}
					gameObjects.removeGameObject(this);
				}
			}


			@Override
			public void update(float deltaTime) {
				super.update(deltaTime);
				setTopLeftCorner(getTopLeftCorner().add(
						new Vector2(0, Constants.FALLING_HEART_SPEED * deltaTime)));
				if (getTopLeftCorner().y() > Constants.windowDimensions.y()) {
					gameObjects.removeGameObject(this);
				}
			}
		};

		fallingHeart.setTag("FallingHeart");
		gameObjects.addGameObject(fallingHeart, Layer.DEFAULT);
	}
}
