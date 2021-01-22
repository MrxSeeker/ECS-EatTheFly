package com.mygdx.game.screen;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.EatTheFlyGameECS;
import com.mygdx.game.GameManager;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.system.CandiesSpawnSystem;
import com.mygdx.game.ecs.system.ParticleEffectSystem;
import com.mygdx.game.ecs.system.SnakesSpawnSystem;
import com.mygdx.game.ecs.system.FliesSpawnSystem;
import com.mygdx.game.ecs.system.BoundsSystem;
import com.mygdx.game.ecs.system.CleanUpSystem;
import com.mygdx.game.ecs.system.CollisionSystem;
import com.mygdx.game.ecs.system.HUDRenderSystem;
import com.mygdx.game.ecs.system.MovementSystem;
import com.mygdx.game.ecs.system.RenderSystem;
import com.mygdx.game.ecs.system.FrogInputSystem;
import com.mygdx.game.ecs.system.WorldWrapSystem;
import com.mygdx.game.ecs.system.debug.DebugCameraSystem;
import com.mygdx.game.ecs.system.debug.DebugInputSystem;
import com.mygdx.game.ecs.system.debug.DebugRenderSystem;
import com.mygdx.game.ecs.system.debug.GridRenderSystem;
import com.mygdx.game.ecs.system.passive.EntityFactorySystem;
import com.mygdx.game.ecs.system.passive.SoundSystem;
import com.mygdx.game.ecs.system.passive.StartUpSystem;
import com.mygdx.game.util.GdxUtils;

/**
 * Artwork from https://goodstuffnononsense.com/about/
 * https://goodstuffnononsense.com/hand-drawn-icons/space-icons/
 */
public class GameScreen extends ScreenAdapter {

  private static final Logger log = new Logger(GameScreen.class.getSimpleName(), Logger.DEBUG);

  private final AssetManager assetManager;
  private final SpriteBatch batch;
  private final EatTheFlyGameECS game;

  private OrthographicCamera camera;
  private Viewport viewport;
  private Viewport hudViewport;
  private ShapeRenderer renderer;
  private PooledEngine engine; //main ECS class
  private BitmapFont font;
  private boolean debug;

  public GameScreen(EatTheFlyGameECS game) {
    this.game = game;
    assetManager = game.getAssetManager();
    batch = game.getBatch();
    debug = true;
  }

  @Override
  public void show() {
    camera = new OrthographicCamera();
    viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);
    hudViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    renderer = new ShapeRenderer();
    font = assetManager.get(AssetDescriptors.FONT32);
    engine = new PooledEngine();
    engine.addSystem(new EntityFactorySystem(assetManager));
    engine.addSystem(new SoundSystem(assetManager));

    if (debug) {
      engine.addSystem(new GridRenderSystem(viewport, renderer));
      engine.addSystem(new DebugCameraSystem(
              GameConfig.WIDTH / 2, GameConfig.HEIGHT / 2, //center
              camera
      ));
      engine.addSystem(new DebugRenderSystem(viewport, renderer));
      engine.addSystem(new DebugInputSystem());
    }
    engine.addSystem(new BoundsSystem());
    //engine.addSystem(new PlayerControlSystem());
    engine.addSystem(new WorldWrapSystem());
    engine.addSystem(new MovementSystem());
    engine.addSystem(new SnakesSpawnSystem());
    engine.addSystem(new CandiesSpawnSystem());
    engine.addSystem(new FliesSpawnSystem());
    engine.addSystem(new FrogInputSystem());
    engine.addSystem(new RenderSystem(batch, viewport));
    engine.addSystem(new StartUpSystem());
    engine.addSystem(new ParticleEffectSystem(batch));
    engine.addSystem(new CleanUpSystem());
    engine.addSystem(new CollisionSystem());
    engine.addSystem(new HUDRenderSystem(batch, hudViewport, font));
    GameManager.INSTANCE.resetResult();
    printEngine();
  }

  @Override
  public void render(float delta) {
    if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) GameManager.INSTANCE.resetResult();
    GdxUtils.clearScreen();
    if (GameManager.INSTANCE.isGameOver())
      engine.update(0);
    else
      engine.update(delta);

    // if (GameManager.INSTANCE.isGameOver()) {
    //     game.setScreen(new MenuScreen(game));
    // }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
    hudViewport.update(width, height, true);
  }

  @Override
  public void hide() {
    dispose();
  }

  @Override
  public void dispose() {
    renderer.dispose();
    engine.removeAllEntities();
  }

  public void printEngine() {
    ImmutableArray<EntitySystem> systems =  engine.getSystems();
    for (EntitySystem system:systems) {
      System.out.println(system.getClass().getSimpleName());
    }
  }
}
