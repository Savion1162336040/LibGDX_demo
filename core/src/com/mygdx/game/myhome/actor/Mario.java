package com.mygdx.game.myhome.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.myhome.GameManager;
import com.mygdx.game.myhome.HomeScreen;

import java.util.ArrayList;

import javafx.stage.Stage;

public class Mario extends SenceBody {

    public enum State {
        STATE_RUNNING,
        STATE_STANDING,
        STATE_JUMPING,
    }

    State currentState = State.STATE_STANDING;
    private final float radius = 6.8f / GameManager.UNIT_SCALE;

    //站立时状态
    private TextureRegion standing;
    //行走时状态
    private Animation<TextureRegion> running;


    public Mario(HomeScreen screen, float x, float y) {
        super(screen, x, y);
        TextureAtlas atlas = screen.getTextureAtlas();

        standing = new TextureRegion(atlas.findRegion("Mario_big"), 0, 0, 16, 32);

        Array<TextureRegion> keyframe = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++) {
            keyframe.add(new TextureRegion(atlas.findRegion("Mario_big"), 16 * i, 0, 16, 32));
        }
        if (keyframe.size > 0) {
            running = new Animation<>(0.1f, keyframe);
        }

        setRegion(standing);
        setBounds(getX(), getY(), 16F / GameManager.UNIT_SCALE, 16F / GameManager.UNIT_SCALE);

        currentState = State.STATE_STANDING;

    }

    @Override
    protected Body createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX(),getY());

        this.body = screen.getWorld().createBody(bodyDef);

        // Mario's body
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(new Vector2(0, 0));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameManager.MARIO_HIT;
        fixtureDef.filter.maskBits = GameManager.WALL_HIT | GameManager.CRATE_HIT;

        body.createFixture(fixtureDef).setUserData(this);

        shape.setPosition(new Vector2(0, radius * 2));
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        // Mario's feet
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(new Vector2(-radius, -radius), new Vector2(radius, -radius));
        fixtureDef.shape = edgeShape;
        body.createFixture(fixtureDef).setUserData(this);

        // Mario's head
        edgeShape.set(new Vector2(-radius / 6, radius * 3), new Vector2(radius / 6, radius * 3));
        fixtureDef.shape = edgeShape;
        fixtureDef.filter.categoryBits = com.mygdx.game.supmario.gamesys.GameManager.MARIO_HEAD_BIT;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        edgeShape.dispose();

        return body;
    }

    @Override
    public void update(float deltaTime) {
        setRegion(standing);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - radius);
    }
}
