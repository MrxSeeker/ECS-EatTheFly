package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ecs.component.BlueCandyParticleComponent;
import com.mygdx.game.ecs.component.DimensionComponent;
import com.mygdx.game.ecs.component.Mappers;
import com.mygdx.game.ecs.component.PositionComponent;

public class ParticleEffectSystem extends IteratingSystem {

    private final SpriteBatch batch;

    private static final Family FAMILY = Family.all(
            BlueCandyParticleComponent.class, // ne e potrebno
            PositionComponent.class,
            DimensionComponent.class,
            BlueCandyParticleComponent.class
    ).get();

    public ParticleEffectSystem(SpriteBatch batch) {
        super(FAMILY);
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.POSITION.get(entity);
        DimensionComponent dimension = Mappers.DIMENSION.get(entity);
        BlueCandyParticleComponent particle = Mappers.BLUE_CANDY_PARTICLE_COMPONENT.get(entity);

        particle.particleEffect.setPosition(position.x + dimension.height/2, position.y + dimension.width / 2);
        particle.particleEffect.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        particle.particleEffect.draw(batch);
        if (particle.particleEffect.isComplete()) {
            particle.particleEffect.reset();
        }
        batch.end();
    }
}
