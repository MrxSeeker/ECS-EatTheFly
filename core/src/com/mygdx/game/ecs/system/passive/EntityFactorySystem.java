package com.mygdx.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.AssetPaths;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.component.BlueCandyParticleComponent;
import com.mygdx.game.ecs.component.CandyComponent;
import com.mygdx.game.ecs.component.SnakeComponent;
import com.mygdx.game.ecs.component.FlyComponent;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.CleanUpComponent;
import com.mygdx.game.ecs.component.DimensionComponent;
import com.mygdx.game.ecs.component.MovementComponentXYR;
import com.mygdx.game.ecs.component.PositionComponent;
import com.mygdx.game.ecs.component.FrogComponent;
import com.mygdx.game.ecs.component.TextureComponent;
import com.mygdx.game.ecs.component.WorldWrapComponent;
import com.mygdx.game.ecs.component.ZOrderComponent;


public class EntityFactorySystem extends EntitySystem {

  private static final int BACKGROUND_Z_ORDER = 0;
  private static final int SNAKE_Z_ORDER = 1;
  private static final int FLY_Z_ORDER = 2;
  private static final int CANDY_Z_ORDER = 3;
  private static final int FROG_Z_ORDER = 4;

  private final AssetManager assetManager;

  private PooledEngine engine;
  private TextureAtlas gamePlayAtlas;

  public EntityFactorySystem(AssetManager assetManager) {
    this.assetManager = assetManager;
    setProcessing(false); //passive
    init();
  }

  private void init() {
    gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
  }

  @Override
  public void addedToEngine(Engine engine) {
    this.engine = (PooledEngine) engine;
  }


  public Entity createBackground() {
    PositionComponent position = engine.createComponent(PositionComponent.class);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.WIDTH;
    dimension.height = GameConfig.HEIGHT;

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = BACKGROUND_Z_ORDER;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(texture);
    entity.add(zOrder);

    engine.addEntity(entity);

    return entity;
  }


  public Entity createFrog() {
    PositionComponent position = engine.createComponent(PositionComponent.class);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.FROG_WIDTH;
    dimension.height = GameConfig.FROG_HEIGHT;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);


    MovementComponentXYR movement = engine.createComponent(MovementComponentXYR.class);

    FrogComponent frog = engine.createComponent(FrogComponent.class);

    WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.FROG);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = FROG_Z_ORDER;

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movement);
    entity.add(frog);
    entity.add(worldWrap);
    entity.add(texture);
    entity.add(zOrder);

    engine.addEntity(entity);

    return entity;
  }

  public void createSnake() {

    PositionComponent position = engine.createComponent(PositionComponent.class);
    float min = 0;
    float max = GameConfig.WIDTH - GameConfig.SNAKE_WIDTH_MIN;

    position.x = MathUtils.random(min, max);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    //movementComponent.xSpeed = -GameConfig.SNAKE_SPEED_X_MIN;
    movementComponent.ySpeed = -GameConfig.SNAKE_SPEED_X_MIN;
    //movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.SNAKE_WIDTH_MIN ;
    dimension.height = GameConfig.SNAKE_HEIGHT_MIN;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    SnakeComponent snakeComponent = engine.createComponent(SnakeComponent.class);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.SNAKE);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = SNAKE_Z_ORDER;

    //WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(snakeComponent);
    entity.add(texture);
    entity.add(zOrder);
   // entity.add(worldWrap);
    entity.add(engine.createComponent(CleanUpComponent.class));
    engine.addEntity(entity);
  }

  public void createFly() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    float min = 0;
    float max = GameConfig.WIDTH - GameConfig.FLY_SIZE;

    position.x = MathUtils.random(min, max);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    movementComponent.xSpeed = GameConfig.FLY_SPEED_X_MIN * MathUtils.random(-0.5f, 0.5f);
    movementComponent.ySpeed = -GameConfig.FLY_SPEED_X_MIN * MathUtils.random(1f, 2f);
    //movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.FLY_SIZE;
    dimension.height = GameConfig.FLY_SIZE;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.FLY);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = FLY_Z_ORDER;

    WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(engine.createComponent(FlyComponent.class));
    entity.add(texture);
    entity.add(zOrder);
    entity.add(engine.createComponent(CleanUpComponent.class));
    entity.add(worldWrap);
    engine.addEntity(entity);
  }

  public void createCandy() {
    PositionComponent position = engine.createComponent(PositionComponent.class);
    float min = 0;
    float max = GameConfig.WIDTH - GameConfig.CANDY_WIDTH;

    position.x = MathUtils.random(min, max);
    position.y = GameConfig.HEIGHT;

    MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
    //movementComponent.xSpeed = GameConfig.CANDY_SPEED_X_MIN * MathUtils.random(-0.5f, 0.5f);
    movementComponent.ySpeed = -GameConfig.CANDY_SPEED_X_MIN * MathUtils.random(2f, 3f);
    movementComponent.rSpeed = MathUtils.random(-1f, 1f);

    DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
    dimension.width = GameConfig.CANDY_WIDTH;
    dimension.height = GameConfig.CANDY_HEIGHT;

    BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
    bounds.rectangle.setPosition(position.x, position.y);
    bounds.rectangle.setSize(dimension.width, dimension.height);

    TextureComponent texture = engine.createComponent(TextureComponent.class);
    texture.region = gamePlayAtlas.findRegion(RegionNames.CANDY);

    ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
    zOrder.z = CANDY_Z_ORDER;

    WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

    BlueCandyParticleComponent blueCandyParticleComponent = engine.createComponent(BlueCandyParticleComponent.class);
    //PARTICLE EFFECT COMPONENT
    blueCandyParticleComponent.particleEffect = new ParticleEffect();
    blueCandyParticleComponent.particleEffect.load(Gdx.files.internal(AssetPaths.BLUE_CANDY_PARTICLE), Gdx.files.internal("particles"));
    blueCandyParticleComponent.particleEffect.start();

    Entity entity = engine.createEntity();
    entity.add(position);
    entity.add(dimension);
    entity.add(bounds);
    entity.add(movementComponent);
    entity.add(engine.createComponent(CandyComponent.class));
    entity.add(texture);
    entity.add(zOrder);
    entity.add(engine.createComponent(CleanUpComponent.class));
    entity.add(worldWrap);
    entity.add(blueCandyParticleComponent);
    engine.addEntity(entity);
  }
}
