package com.mygdx.game.myhome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
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

        positionX = GameManager.V_WIDTH / 2f;
        camera.position.set(positionX, GameManager.V_HEIGHT / 2f, 0);

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

        cameraLeftLimit = GameManager.V_WIDTH / 2;
        cameraRightLimit = mapWidth - GameManager.V_WIDTH / 2;

        debugRenderer = new Box2DDebugRenderer();

        Gdx.input.setInputProcessor(new GestureDetector(new MyGestureListener()) {
            @Override
            public boolean touchUp(float x, float y, int pointer, int button) {
                System.out.println(String.format("手势结束:%s=%s=%s=%s", x, y, pointer, button));
                currentGestureMode = GestureMode.GESTURE_IDLE;
                return super.touchUp(x, y, pointer, button);
            }
        });
    }

    float positionX = 0;
    float dragSpeed = 0.1f;

    private GestureMode currentGestureMode = GestureMode.GESTURE_IDLE;

    public enum GestureMode {
        //无手势
        GESTURE_IDLE,
        //拖动
        GESTURE_MOVE,
        //手势拖动
        GESTURE_PAN,
        //手势点击
        GESURE_TAP,
        //手势缩放
        GESTURE_ZOOM,
    }

    class MyGestureListener implements GestureDetector.GestureListener {


        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            Gdx.input.vibrate(100);

            float viewRatio = Math.max(Gdx.graphics.getWidth() / GameManager.V_WIDTH, Gdx.graphics.getHeight() / GameManager.V_HEIGHT);

            float marioStartX = (mario.getPosition().x - (camera.position.x - GameManager.V_WIDTH / 2f)) * viewRatio;
            //box2d的坐标系是从左下角开始，所以mario的y坐标要用height减一下，以适配到左上角坐标系中
            float marioStartY = ((GameManager.V_HEIGHT - mario.getPosition().y) - (camera.position.y - GameManager.V_HEIGHT / 2f)) * viewRatio;
            float marioEndX = marioStartX + mario.getWidth() * viewRatio;
            float marioEndY = marioStartY + mario.getHeight() * viewRatio;

            if (x <= marioEndX && x >= marioStartX
                    && y <= marioEndY && y >= marioStartY) {
                currentGestureMode = GestureMode.GESTURE_MOVE;
            }
            System.out.println(String.format("marioRealRect:%s=%s=%s=%s,longPress:%s==%s,viewSize:%s==%s,mapSize:%s==%s,marioSize:%s==%s,marioRecSize:%s==%s,position:%s", marioStartX, marioStartY, marioEndX, marioEndY, x, y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), GameManager.V_WIDTH, GameManager.V_HEIGHT, mario.getWidth(), mario.getHeight(), mario.getRegionWidth(), mario.getRegionHeight(), mario.getPosition()));
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            System.out.println(String.format("fling:%s==%s==%s", velocityX, velocityY, button));
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if (currentGestureMode == GestureMode.GESTURE_IDLE || currentGestureMode == GestureMode.GESTURE_PAN) {
                currentGestureMode = GestureMode.GESTURE_PAN;
                positionX -= deltaX / GameManager.UNIT_SCALE * dragSpeed;
                positionX = MathUtils.clamp(positionX, cameraLeftLimit, cameraRightLimit);
                camera.position.x = positionX;
                System.out.println(String.format("pan : %s==%s==%s==%s==%s", positionX, x, y, deltaX, deltaY));
                camera.update();
            } else if (currentGestureMode == GestureMode.GESTURE_MOVE) {
                mario.getPosition().x += deltaX / GameManager.UNIT_SCALE;
                mario.getPosition().y += deltaY / GameManager.UNIT_SCALE;
            }
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            currentGestureMode = GestureMode.GESTURE_IDLE;
            return false;
        }

        /**
         * 双指缩放距离，与{@link #pinch(Vector2, Vector2, Vector2, Vector2)}相对应
         *
         * @param initialDistance 双指按下时距离
         * @param distance        当前双指距离
         * @return 是否消费当前事件
         */
        @Override
        public boolean zoom(float initialDistance, float distance) {
            //
            System.out.println(String.format("zoom:%s==%s", initialDistance, distance));
            return false;
        }

        /**
         * 双指收缩时点坐标,与{@link #zoom(float, float)}相对应
         *
         * @param initialPointer1 双指按下时点1坐标
         * @param initialPointer2 双指按下时点2坐标
         * @param pointer1        当前点1坐标
         * @param pointer2        当前点2坐标
         * @return 是否消费当前事件
         */
        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            System.out.println(String.format("pinch:%s==%s==%s==%s", initialPointer1.toString(), initialPointer2.toString(), pointer1.toString(), pointer2.toString()));
            return false;
        }

        @Override
        public void pinchStop() {
            System.out.println(("pinchStop:"));
        }
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

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && mario.body.getLinearVelocity().x <= 2) {
            mario.body.applyLinearImpulse(new Vector2(0.1f, 0), mario.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && mario.body.getLinearVelocity().x >= -2) {
            mario.body.applyLinearImpulse(new Vector2(-0.1f,0),mario.body.getWorldCenter(),true);
        }
    }

    private float accumulator;

    private float cameraLeftLimit;
    private float cameraRightLimit;

    public void update(float delta) {
        delta *= GameManager.timeScale;
        float step = GameManager.STEP * GameManager.timeScale;

        handleInput();

        // Box2D world step
        accumulator += delta;
        if (accumulator > step) {
            world.step(step, 8, 3);
            accumulator -= step;
        }
        mario.update(delta);

        // camera control
        float targetX = camera.position.x;
        if (currentGestureMode == GestureMode.GESTURE_IDLE) {
            targetX = MathUtils.clamp(mario.getPosition().x, cameraLeftLimit, cameraRightLimit);
            positionX = targetX;
        }
        camera.position.x = MathUtils.lerp(camera.position.x, targetX, 0.1f);
        if (Math.abs(camera.position.x - targetX) < 0.1f) {
            camera.position.x = targetX;
        }
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
