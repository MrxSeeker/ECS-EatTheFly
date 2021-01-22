package com.mygdx.game.ecs.component;

import com.badlogic.ashley.core.ComponentMapper;
//TODO Explain how Mappers work (see ComponentMapper and Entity implementation)
public final class Mappers {

  public static final ComponentMapper<BoundsComponent> BOUNDS =
          ComponentMapper.getFor(BoundsComponent.class);
  public static final ComponentMapper<DimensionComponent> DIMENSION =
          ComponentMapper.getFor(DimensionComponent.class);

  public static final ComponentMapper<PositionComponent> POSITION =
          ComponentMapper.getFor(PositionComponent.class);
  public static final ComponentMapper<SnakeComponent> SNAKE =
          ComponentMapper.getFor(SnakeComponent.class);
  public static final ComponentMapper<FrogComponent> FROG =
          ComponentMapper.getFor(FrogComponent.class);
  public static final ComponentMapper<MovementComponentXYR> MOVEMENT =
          ComponentMapper.getFor(MovementComponentXYR.class);

  public static final ComponentMapper<TextureComponent> TEXTURE =
          ComponentMapper.getFor(TextureComponent.class);

  public static final ComponentMapper<ZOrderComponent> Z_ORDER =
          ComponentMapper.getFor(ZOrderComponent.class);

  public static final ComponentMapper<FlyComponent> FLIES =
          ComponentMapper.getFor(FlyComponent.class);
  public static final ComponentMapper<CandyComponent> CANDIES =
          ComponentMapper.getFor(CandyComponent.class);
  public static final ComponentMapper<BlueCandyParticleComponent> BLUE_CANDY_PARTICLE_COMPONENT =
          ComponentMapper.getFor(BlueCandyParticleComponent.class);

  private Mappers() {
  }
}
