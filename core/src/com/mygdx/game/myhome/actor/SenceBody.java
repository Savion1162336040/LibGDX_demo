package com.mygdx.game.myhome.actor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.myhome.HomeScreen;

/**
 * 场景角色
 */
public abstract class SenceBody extends Sprite {

    protected HomeScreen screen;

    public Body body;

    public SenceBody(HomeScreen screen,float x,float y){
        this.screen = screen;
        setPosition(x,y);
        createBody();
    }

    public Vector2 getPosition(){
        if (body!=null){
            return body.getPosition();
        }
        return new Vector2();
    }

    /**
     * 创建body
     * @return
     */
    protected abstract Body createBody();

    /**
     * 更新对象参数属性
     * @param deltaTime
     */
    protected abstract void update(float deltaTime);

}
