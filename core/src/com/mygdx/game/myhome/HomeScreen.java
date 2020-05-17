package com.mygdx.game.myhome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.myhome.actor.Mario;
import com.mygdx.game.supmario.actors.effects.Effect;
import com.mygdx.game.supmario.actors.enemies.Enemy;
import com.mygdx.game.supmario.actors.items.Item;
import com.mygdx.game.supmario.actors.maptiles.MapTileObject;
import com.mygdx.game.supmario.actors.weapons.Fireball;
import com.mygdx.game.supmario.screens.GameOverScreen;

public class HomeScreen implements Screen {
    TiledMap map;
    private OrthographicCamera camera;
    private Viewport fitViewport;
    OrthogonalTiledMapRenderer mapRenderer;

    private TextureAtlas textureAtlas;

    private World world;

    private Body ground;


    private InitEntry entry;

    private Mario mario;
    private float mapWidth;


    private Box2DDebugRenderer debugRenderer;

    public HomeScreen(InitEntry initEntry) {
        this.entry = initEntry;
    }

    @Override
    public void show() {
        //初始化像机
        camera = new OrthographicCamera();
        fitViewport = new FitViewport(GameManager.V_WIDTH, GameManager.V_HEIGHT);
        fitViewport.setCamera(camera);

        camera.position.set(GameManager.V_WIDTH / 2f, GameManager.V_HEIGHT / 2f, 0);

        textureAtlas = new TextureAtlas("home/actors/actors.atlas");

        world = new World(GameManager.GRAVITY, true);

        //createGound
        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;
        fixtureDef.filter.categoryBits = GameManager.GROUND_HIT;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(camera.viewportWidth, 0);
        fixtureDef.shape = polygonShape;

        ground = world.createBody(groundDef);
        ground.setTransform(0, 0, 0);

        Vector2 startVector = new Vector2();
        //初始化地图
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("home/maps/home_map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / GameManager.UNIT_SCALE);
        mapWidth = ((TiledMapTileLayer) map.getLayers().get(0)).getWidth();

        MapLayer startLayer = map.getLayers().get("start");
        if (startLayer != null && startLayer.getObjects().getCount() > 0) {
            TiledMapTileMapObject object = ((TiledMapTileMapObject) startLayer.getObjects().get("start"));
            startVector.x = object.getX();
            startVector.y = object.getY();
        }
        mario = new Mario(this, startVector.x / GameManager.UNIT_SCALE, startVector.y / GameManager.UNIT_SCALE);

        cameraLeftLimit = com.mygdx.game.supmario.gamesys.GameManager.V_WIDTH / 2;
        cameraRightLimit =  mapWidth - com.mygdx.game.supmario.gamesys.GameManager.V_WIDTH / 2;

        debugRenderer = new Box2DDebugRenderer();

    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render(new int[]{0, 1});
        entry.spriteBatch.setProjectionMatrix(camera.combined);
        entry.spriteBatch.begin();


        mario.draw(entry.spriteBatch);

        entry.spriteBatch.end();
        entry.gameManager.playMusic("main_bg.mp3");

        debugRenderer.render(world, camera.combined);
        update(delta);
    }

    private float accumulator;

    private float cameraLeftLimit;
    private float cameraRightLimit;
    public void update(float delta) {
        delta *= GameManager.timeScale;
        float step = GameManager.STEP * GameManager.timeScale;

        // Box2D world step
        accumulator += delta;
        if (accumulator > step) {
            world.step(step, 8, 3);
            accumulator -= step;
        }
        mario.update(delta);

        // camera control
//        float targetX = camera.position.x;
//        targetX = MathUtils.clamp(mario.getPosition().x, cameraLeftLimit, cameraRightLimit);
//
//        camera.position.x = MathUtils.lerp(camera.position.x, targetX, 0.1f);
//        if (Math.abs(camera.position.x - targetX) < 0.1f) {
//            camera.position.x = targetX;
//        }
        camera.update();

        mapRenderer.setView(camera);
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        map.dispose();
        mapRenderer.dispose();
        entry.dispose();
        debugRenderer.dispose();
    }
}
