package com.noob.crash

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
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
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.viewport.FitViewport
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

var gameover : Boolean = false
var scoreA = 0
var scoreB = 0
var backpressed : Boolean = false

class FirstScreen : Screen {
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
    var previousPosition : Vector2
    var hasMoved : Boolean
    lateinit var socket : Socket

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

        connectSocket()
        configsocketevents()
        previousPosition = playerB.position
        hasMoved = false



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

        if (previousPosition != playerB.position) {
            hasMoved = true
            previousPosition = playerB.position
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

    fun connectSocket() {
        try {
            socket = IO.socket("http://localhost:8080")
            socket.connect()
        } catch (e : Exception) {
            println(e)
        }
    }
    fun configsocketevents() {
        socket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            println("connected")
        }).on("socketID", Emitter.Listener {
            var data : JSONObject = it[0] as JSONObject
            try {
                var id: String = data.getString("id")
                println("Your ID is $id")
            } catch (e : JSONException) {
                println(e)
            }
        }).on("newPlayer", Emitter.Listener {
            var data : JSONObject = it[0] as JSONObject
            try {
                var id: String = data.getString("id")
                println("New player connecter with ID: $id")
            } catch (e : JSONException) {
                println(e)
            }
        }).on("playerDisconnected", Emitter.Listener {
            var data : JSONObject = it[0] as JSONObject
            try {
                var id: String = data.getString("id")
                println("player disconnected")
            } catch (e : JSONException) {
                println(e)
            }
        }).on("getPlayers", Emitter.Listener {
            var obj : JSONArray = it[0] as JSONArray
            try {
                for(i in 0..obj.length()){
                    playerA.position.x = obj.getJSONObject(i).getDouble("x").toFloat()
                    playerA.position.y = obj.getJSONObject(i).getDouble("y").toFloat()
                }
            } catch (e : JSONException) {

            }
        })
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