package com.mycompany.a3.gameObjects.interfaces;

import com.mycompany.a3.game.GameWorld;

public interface ICollider {
	public boolean collidesWith(Object otherObj);
    public void handleCollision(Object otherObj, GameWorld gw);
}