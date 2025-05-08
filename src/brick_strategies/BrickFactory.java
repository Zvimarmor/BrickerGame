package brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import gameobjects.Brick;

import java.util.Random;

public class BrickFactory {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;

	public BrickFactory(GameObjectCollection gameObjects, Counter bricksNum) {
		this.rand = new Random();
		this.gameObjects = gameObjects;
		this.bricksNum = bricksNum;
	}

	public CollisionStrategy getStrategy() {
		int roll = rand.nextInt(10);
		switch (roll) {
			case 0:
				return new ExtraBallsStrategy(gameObjects, bricksNum);
			case 1:
				return new PaddleDuplicatorStrategy(gameObjects, bricksNum);
			case 2:
				return new TurboModeStrategy(gameObjects, bricksNum);
			case 3:
				return new LifeRestorationStrategy(gameObjects, bricksNum);
			case 4:
				return new DoubleStrategy(new BasicCollisionStrategy(gameObjects, bricksNum));
			default:
				return new BasicCollisionStrategy(gameObjects, bricksNum);
		}
	}
}
