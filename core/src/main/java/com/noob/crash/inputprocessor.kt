package com.noob.crash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body

class inputprocessor : InputProcessor{
    var playerA : Body
    var playerB : Body
    constructor(playerA : Body, playerB : Body) {
        this.playerA = playerA
        this.playerB = playerB
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }


    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var playerA_right = Rectangle(0f, 0f, (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerA_left = Rectangle(0f, (Gdx.graphics.height / 2).toFloat(), (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerB_left = Rectangle((Gdx.graphics.width / 2).toFloat(), 0f, (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerB_right = Rectangle((Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat(), (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var touchpos = Vector2(screenX.toFloat(), screenY.toFloat())




        if (playerB_left.contains(touchpos)) {
            playerB.applyForceToCenter(0f, 12f, true)
        }
        if (playerB_right.contains(touchpos)) {
            playerB.applyForceToCenter(0f, -12f, true)
        }

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        var playerA_right = Rectangle(0f, 0f, (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerA_left = Rectangle(0f, (Gdx.graphics.height / 2).toFloat(), (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerB_left = Rectangle((Gdx.graphics.width / 2).toFloat(), 0f, (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var playerB_right = Rectangle((Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat(), (Gdx.graphics.width / 2).toFloat(), (Gdx.graphics.height / 2).toFloat())
        var touchpos = Vector2(screenX.toFloat(), screenY.toFloat())




        if (playerB_left.contains(touchpos)) {
            playerB.setLinearVelocity(0f, 0f)
        }
        if (playerB_right.contains(touchpos)) {
            playerB.setLinearVelocity(0f, 0f)
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }


}