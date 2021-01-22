package com.mygdx.game.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class CandyComponent implements Component, Pool.Poolable {
    public boolean hit=false;
    public boolean isBounced=false;
    public int bounceCounter=0;

    @Override
    public void reset() {
        hit = false;
        isBounced=false;
        bounceCounter=0;
    }
}
