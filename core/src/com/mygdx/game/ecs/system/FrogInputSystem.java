package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.MovementComponentXYR;
import com.mygdx.game.ecs.component.FrogComponent;


public class FrogInputSystem extends IteratingSystem {

  private static final Family FAMILY = Family.all(
          FrogComponent.class,
          MovementComponentXYR.class
  ).get();

  public FrogInputSystem() {
    super(FAMILY);
  }

  // we don't need to override update Iterating system method because there is no batch.begin/end

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    MovementComponentXYR movement = Mappers.MOVEMENT.get(entity);

    movement.xSpeed = 0;

    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      movement.xSpeed = GameConfig.MAX_FROG_X_SPEED * deltaTime;
    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      movement.xSpeed = -GameConfig.MAX_FROG_X_SPEED * deltaTime;
    }

  }
}
