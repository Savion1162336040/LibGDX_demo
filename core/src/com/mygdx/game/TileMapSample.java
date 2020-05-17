package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TileMapSample extends ApplicationAdapter {

    float CAMERA_SPEED = 100;

    OrthographicCamera camera;
    Viewport viewport;

    TmxMapLoader tmxMapLoader;
    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    Vector2 direction;

    Stage stage;


    @Override
    public void create() {
        super.create();
        camera = new OrthographicCamera();
        viewport = new FitViewport(360, 640, camera);

        tmxMapLoader = new TmxMapLoader();
        map = tmxMapLoader.load("map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        direction = new Vector2();

        stage = new Stage();


    }

    @Override
    public void render() {
        super.render();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.input.setInputProcessor(stage);


        System.out.println("DELTA:" + Gdx.graphics.getDeltaTime() + ">" + Gdx.graphics.getRawDeltaTime() + ">>" + Gdx.graphics.getFramesPerSecond());

        updateCamera();

        check();

        mapRenderer.setView(camera);
        mapRenderer.render();

    }

    private void check() {

        float x = Gdx.input.getX();
        float y = Gdx.input.getY();

    }


    private void updateCamera() {
        direction.set(0.0f, 0.0f);

        int mouseX = Gdx.input.getX();
        int mouseY = Gdx.input.getY();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isTouched() && mouseX < width * 0.25f)) {
            direction.x = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isTouched() && mouseX > width * 0.75f)) {
            direction.x = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isTouched() && mouseY < height * 0.25f)) {
            direction.y = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isTouched() && mouseY > height * 0.75f)) {
            direction.y = -1;
        }

        direction.nor().scl(CAMERA_SPEED * Gdx.graphics.getDeltaTime());
        ;

        camera.position.x += direction.x;
        camera.position.y += direction.y;

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        float cameraMinX = viewport.getWorldWidth() * 0.5f;
        float cameraMinY = viewport.getWorldHeight() * 0.5f;
        float cameraMaxX = layer.getWidth() * layer.getTileWidth() - cameraMinX;
        float cameraMaxY = layer.getHeight() * layer.getTileHeight() - cameraMinY;

        camera.position.x = MathUtils.clamp(camera.position.x, cameraMinX, cameraMaxX);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraMinY, cameraMaxY);

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        mapRenderer.dispose();
        map.dispose();
    }
}
