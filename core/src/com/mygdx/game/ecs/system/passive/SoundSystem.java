package com.mygdx.game.ecs.system.passive;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.assets.AssetDescriptors;

public class SoundSystem extends EntitySystem {

  private final AssetManager assetManager;

  private Sound pickSound;
  private Sound frogCroakingSound;

  public SoundSystem(AssetManager assetManager) {
    this.assetManager = assetManager;
    setProcessing(false); //passive
    init();
  }

  private void init() {
    pickSound = assetManager.get(AssetDescriptors.PICK_SOUND);
    frogCroakingSound = assetManager.get(AssetDescriptors.FROG_CROAKING_SOUND);
  }

  public void pick() {
    pickSound.play();
  }

  public void frogCroakingSound() {
    frogCroakingSound.play();
  }

}
