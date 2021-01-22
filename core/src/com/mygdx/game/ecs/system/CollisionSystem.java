package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.EatTheFlyGameECS;
import com.mygdx.game.GameManager;
import com.mygdx.game.ecs.component.BlueCandyParticleComponent;
import com.mygdx.game.ecs.component.CandyComponent;
import com.mygdx.game.ecs.component.SnakeComponent;
import com.mygdx.game.ecs.component.FlyComponent;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.FrogComponent;
import com.mygdx.game.ecs.system.passive.EntityFactorySystem;
import com.mygdx.game.ecs.system.passive.SoundSystem;

public class CollisionSystem extends EntitySystem {

  private static final Family FROG_FAMILY = Family.all(FrogComponent.class, BoundsComponent.class).get();
  private static final Family SNAKE_FAMILY = Family.all(SnakeComponent.class, BoundsComponent.class).get();
  private static final Family FLY_FAMILY = Family.all(FlyComponent.class, BoundsComponent.class).get();
  private static final Family CANDY_FAMILY = Family.all(CandyComponent.class, BoundsComponent.class).get();

  private EntityFactorySystem factory;
  private SoundSystem soundSystem;

  public CollisionSystem() {
  }

  @Override
  public void addedToEngine(Engine engine) {
    factory = engine.getSystem(EntityFactorySystem.class);
    soundSystem = engine.getSystem(SoundSystem.class);
  }

  @Override
  public void update(float deltaTime) {
    if (GameManager.INSTANCE.isGameOver()) return;
    ImmutableArray<Entity> frogs = getEngine().getEntitiesFor(FROG_FAMILY);
    ImmutableArray<Entity> snakes = getEngine().getEntitiesFor(SNAKE_FAMILY);
    ImmutableArray<Entity> flies = getEngine().getEntitiesFor(FLY_FAMILY);
    ImmutableArray<Entity> candies = getEngine().getEntitiesFor(CANDY_FAMILY);
    for (Entity frogEntity : frogs) {
      BoundsComponent firstBounds = Mappers.BOUNDS.get(frogEntity);
      for (Entity snakeEntity : snakes) {
        SnakeComponent snakeComponent = Mappers.SNAKE.get(snakeEntity);

        if (snakeComponent.hit) {
          continue;
        }
        BoundsComponent secondBounds = Mappers.BOUNDS.get(snakeEntity);

        if (Intersector.overlaps(firstBounds.rectangle, secondBounds.rectangle)) {
          //snakeComponent.hit = true;
          GameManager.INSTANCE.damage();
          soundSystem.frogCroakingSound();

        }
      }
      for (Entity flyEntity : flies) {
        FlyComponent flyComponent = Mappers.FLIES.get(flyEntity);

        if (flyComponent.hit) {
          continue;
        }
        BoundsComponent secondBounds = Mappers.BOUNDS.get(flyEntity);

        if (Intersector.overlaps(firstBounds.rectangle, secondBounds.rectangle)) {
          flyComponent.hit = true;
          GameManager.INSTANCE.incResult();
          soundSystem.pick();
          getEngine().removeEntity(flyEntity);
        }
      }

      for (Entity candyEntity : candies) {
        CandyComponent candyComponent = Mappers.CANDIES.get(candyEntity);

        if (candyComponent.hit) {
          continue;
        }

        BoundsComponent secondBounds = Mappers.BOUNDS.get(candyEntity);

        // Bouncing candy
        /*
        if (secondBounds.rectangle.y <= 1)  {
          candyComponent.isBounced=true;
        }
        if (candyComponent.isBounced && candyComponent.bounceCounter<=3) {
          candyComponent.bounceCounter++;
          candyComponent.isBounced=true;
          secondBounds.rectangle.y= -GameConfig.CANDY_SPEED_X_MIN;
        } else {
          continue;
        }*/


        if (Intersector.overlaps(firstBounds.rectangle, secondBounds.rectangle)) {
          candyComponent.hit = true;
          GameManager.INSTANCE.setHealth(GameManager.INSTANCE.getHealth()+10);
          soundSystem.pick();
          getEngine().removeEntity(candyEntity);
        }
      }

    }

  }
}
