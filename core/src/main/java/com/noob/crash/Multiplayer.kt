package com.noob.crash

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.FitViewport
import java.util.*
import kotlin.concurrent.schedule

class Multiplayer : Screen {
    var batch : SpriteBatch
    var camera : OrthographicCamera
    var viewport : FitViewport
    var map : TiledMap
    var rendrer : OrthogonalTiledMapRenderer
    var loader : TmxMapLoader
    var world : World
    var b2dr : Box2DDebugRenderer
    var PPM = 100f
    var checker : Checker
    var tile : Tile
    var ball : Ball
    var bounds : Bounds
    var playerA : Body
    var playerB : Body
    var winchecker : winChecker
    var goti : Body
    var ball_image : Texture
    var tile_image_red : Texture
    var tile_image_green : Texture
    var contactlistner : contactListner
    var font : BitmapFont
    var font1 : BitmapFont
    var scale = 3.7f
    var inputprocessor : inputprocessor
    var game : Game

    constructor(batch : SpriteBatch, game : Game) {
        this.game = game
        world = World(Vector2(0f,0f), false)
        contactlistner = contactListner(world, batch)
        b2dr = Box2DDebugRenderer()
        this.batch = batch
        camera = OrthographicCamera()
        viewport = FitViewport(Gdx.graphics.width.toFloat()/PPM/scale, Gdx.graphics.height.toFloat()/PPM/scale, camera)
        camera.setToOrtho(false, viewport.worldWidth, viewport.worldHeight)
        loader = TmxMapLoader()
        map = loader.load("map.tmx")
        rendrer = OrthogonalTiledMapRenderer(map, 1/PPM)

        tile = Tile(world)
        ball = Ball(world)
        bounds = Bounds(map, world)
        checker = Checker(world, map)
        winchecker = winChecker(world, map)
        playerA = tile.createBox(80,140,15,60)
        playerB = tile.createBox(430,140,15,60)
        goti = ball.createBall()
        bounds.getobject()
        checker.getchecker(32f, 287f)
        checker.getchecker(32f, 64f)
        checker.getchecker(480f, 287f)
        checker.getchecker(480f, 65f)
        winchecker.getchecker(5f, 223f)
        winchecker.getchecker(508f, 223f)
        world.setContactListener(contactListner(world, batch))
        ball_image = Texture("ball.png")
        tile_image_red = Texture("back.png")
        tile_image_green = Texture("back.png")
        gameover = false
        font = BitmapFont()
        font.setColor(Color.DARK_GRAY)
        font.data.setScale(4f)
        font1 = BitmapFont()
        font1.setColor(Color.DARK_GRAY)
        font1.data.setScale(4f)

        inputprocessor = inputprocessor(playerA, playerB)
        Gdx.input.setInputProcessor(inputprocessor)


    }
    override fun show() {

    }

    fun handleInput() {

        if (backpressed == true) {
            game.screen = MenuScreen(batch, game)
            this.dispose()
            backpressed = false
            scoreA = 0
            scoreB = 0
        }
    }
    fun update(delta : Float) {
        if (scoreA >= 7 || scoreB >= 7) {
            scoreA = 0
            scoreB = 0
            game.screen = MenuScreen(batch, game)
            this.dispose()
        }
        world.step(1/60f, 6,2)
        if (gameover) {
            world.destroyBody(goti)
            gameover = false
            Timer().schedule(2000){
                goti = ball.createBall()
                playerA.setTransform(80/PPM,140/PPM, 0f)
                playerB.setTransform(430/PPM,140/PPM, 0f)
            }

        }
    }

    override fun render(delta: Float) {
        update(delta)
        handleInput()
        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        rendrer.setView(camera)
        rendrer.render()
        batch.begin()
        batch.draw(ball_image, goti.position.x*PPM*scale - 20f*scale/2, goti.position.y*PPM*scale - 20f*scale/2, 20f*scale, 20f*scale)
        batch.draw(tile_image_green, playerA.position.x*PPM*scale - 15f*scale/2, playerA.position.y*PPM*scale - 60f*scale/2, 15f*scale, 60f*scale)
        batch.draw(tile_image_red, playerB.position.x*PPM*scale - 15f*scale/2, playerB.position.y*PPM*scale - 60f*scale/2, 15f*scale, 60f*scale)
        font.draw(batch, "$scoreA", 200*scale, 280*scale)
        font1.draw(batch, "$scoreB", 275*scale, 280*scale)
        batch.end()
        //b2dr.render(world, camera.combined)
    }






    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        ball_image.dispose()
        tile_image_green.dispose()
        tile_image_red.dispose()
        contactlistner.dispose()
        println("Disposed")
    }
}