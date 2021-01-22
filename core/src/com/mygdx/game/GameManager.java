package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {
  private static final String RESULT_BEST = "BEST_RESULT";
  public String userID;
  private Preferences PREFS;
  public static final GameManager INSTANCE = new GameManager();
  int result;
  int health;

  public void resetResult() {
    result = 0;
    health = 100;
  }

  public boolean isGameOver() {
    return getHealth() <= 0;
  }

  public void damage() {
    health-=2;
    if (health == 0) {
      if (result > getBestResult()) setBestResult(result);
    }
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    if (health>100)
      this.health=100;
    else
      this.health = health;
  }

  public void incResult() {
    result++;
  }

  public int getResult() {
    return result;
  }

  private GameManager() {
    PREFS = Gdx.app.getPreferences(EatTheFlyGameECS.class.getSimpleName());
    userID = "goran";
  }


  public void setBestResult(int result) {
    PREFS.putInteger(RESULT_BEST, result);
    PREFS.flush();
  }

  public int getBestResult() {
    return PREFS.getInteger(RESULT_BEST, 0);
  }
}
