package com.example.app.jason.ragerelease.app.Framework;

/**
 * Created by Win8 on 24/07/2015.
 */
public class ObjectID
{
    public static final int PLAYER = 0;                         // The ID number for the player.
    public static final int ANIMATEDGROUND = PLAYER + 1;        // The ID number for the animated ground.
    public static final int GROUND = ANIMATEDGROUND + 1;        // The ID number for the static ground.
    public static final int WALL = GROUND + 1;                  // The ID number for the walls.
    public static final int ENEMY = WALL + 1;                   // The ID number for the enemy.
    public static final int SPRITE = ENEMY + 1;                 // The ID number for a standard sprite.
    public static final int ANIMATEDSPRITE = SPRITE + 1;        // The ID number for an animated sprite.
    public static final int OBSTACLE = ANIMATEDSPRITE + 1;      // The ID number for the obstacles.
}
