// BrickFactory.java
package brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import main.Constants;

import java.util.Random;

public class BrickFactory {
	private final Random rand;
	private final GameObjectCollection gameObjects;
	private final Counter bricksNum;
	private final ImageReader imageReader;
	private final SoundReader soundReader;
	public final UserInputListener userInputListener;


	public BrickFactory(GameObjectCollection gameObjects,
						Counter bricksNum,
						ImageReader imageReader,
						SoundReader soundReader,
						UserInputListener userInputListener) {
		this.rand = new Random();
		this.gameObjects = gameObjects;
		this.bricksNum = bricksNum;
		this.imageReader = imageReader;
		this.soundReader = soundReader;
		this.userInputListener = userInputListener;

	}

	public CollisionStrategy getStrategy() {
		int roll = rand.nextInt(Constants.STRATEGY_ROLL_BOUND); // extend this to 10 when all behaviors are implemented
		switch (roll) {
			case 0:
				return new ExtraBallsStrategy(gameObjects, bricksNum, imageReader, soundReader);
			case 1:
				return new PaddleDuplicatorStrategy(gameObjects, bricksNum,imageReader,userInputListener);
			//          case 2:
			//              return new TurboModeStrategy(gameObjects, bricksNum);
			//          case 3:
			//              return new LifeRestorationStrategy(gameObjects, bricksNum);
			//          case 4:
			//              return new DoubleStrategy(new BasicCollisionStrategy(gameObjects, bricksNum));

			default:
				return new BasicCollisionStrategy(gameObjects, bricksNum);
		}
	}
}