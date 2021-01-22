package com.mygdx.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

//Called only at start, to generate basic entities
public class StartUpSystem extends EntitySystem {
  private EntityFactorySystem factory;

  public StartUpSystem() {
    setProcessing(false);
  } //don't call update

  @Override
  public void addedToEngine(Engine engine) {
    factory = engine.getSystem(EntityFactorySystem.class);
    startUp();
  }

  private void startUp() {
    factory.createBackground();
    factory.createSnake();
    factory.createFrog();
  }
}
