package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.codeandweb.physicseditor.PhysicsShapeCache;

import java.util.HashMap;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    World world;
    PhysicsShapeCache physicsShapeCache;
//    Body banana;
    OrthographicCamera camera;
    ExtendViewport viewport;
    Box2DDebugRenderer debugRenderer;
    Body ground;

    private void createGround() {
        if (ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 1;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 0);
        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);
        ground.setTransform(0, 10, 0);

        shape.dispose();
    }

    float accumulator = 0;
    //60帧
    static final float STEP_TIME = 1f / 60f;
    static final int VELOCITY_ITERATIONS = 6;
    static final int POSITION_ITERATIONS = 2;
    static final float SCALE = 0.05F;

    private void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    HashMap<String, Sprite> spriteHashMap = new HashMap<>();

    private void addSprite() {
        Array<TextureAtlas.AtlasRegion> atlasRegions = textureAtlas.getRegions();

        for (int i = 0; atlasRegions != null && i < atlasRegions.size; i++) {
            Sprite sprite = textureAtlas.createSprite(atlasRegions.get(i).name);
            if (sprite != null) {
                sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
                sprite.setOrigin(0,0);
                spriteHashMap.put(atlasRegions.get(i).name, sprite);
            }
        }
    }

    private void drawSprite(String spriteName, float x, float y, float degree) {
        if (spriteHashMap.containsKey(spriteName)) {
            spriteHashMap.get(spriteName).setPosition(x, y);
            spriteHashMap.get(spriteName).setRotation(degree);
            spriteHashMap.get(spriteName).draw(batch);
        }
    }

    private Body createBody(String bodyName, float x, float y, float rotate) {
        Body body = physicsShapeCache.createBody(bodyName, world, SCALE, SCALE);
        body.setTransform(x, y, rotate);
        return body;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
        createGround();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(50, 50, camera);
        textureAtlas = new TextureAtlas("sprites.txt");
        addSprite();

        //初始化物理引擎
        Box2D.init();
        world = new World(new Vector2(0, -100), true);
        physicsShapeCache = new PhysicsShapeCache("physics.xml");

        generateFruit();
        debugRenderer = new Box2DDebugRenderer();
    }
    Body[] fruitBodies = new Body[10];
    String[] names = new String[10];
    private void generateFruit() {
        String[] fruitNames = new String[]{"banana", "cherries", "orange"};

        Random random = new Random();

        for (int i = 0; i < fruitBodies.length; i++) {
            String name = fruitNames[random.nextInt(fruitNames.length)];

            float x = random.nextFloat() * 50;
            float y = random.nextFloat() * 50 + 50;

            names[i] = name;
            fruitBodies[i] = createBody(name, x, y, 0);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.57f, 0.77f, 0.85f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stepWorld();
        batch.begin();
//
//        Vector2 position = banana.getPosition();
//        float degrees = (float) Math.toDegrees(banana.getAngle());
//        drawSprite("banana", position.x, position.y, degrees);

        for (int i = 0; i < fruitBodies.length; i++) {
            Body body = fruitBodies[i];
            String name = names[i];

            Vector2 position = body.getPosition();
            float degrees = (float) Math.toDegrees(body.getAngle());
            drawSprite(name, position.x, position.y, degrees);
        }
        batch.end();

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        batch.dispose();
        textureAtlas.dispose();

        world.dispose();
        debugRenderer.dispose();
        spriteHashMap.clear();
    }
}
