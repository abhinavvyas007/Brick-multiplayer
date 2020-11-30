package com.noob.crash

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import java.awt.Rectangle
import kotlin.system.exitProcess

class MenuScreen : Screen {
    var batch : SpriteBatch
    var play : Texture
    var game : Game
    var texture : Texture
    var music : Music
    constructor(batch: SpriteBatch, game : Game) {
        this.batch= batch
        this.game = game
        texture = Texture("iron.jpg")
        play = Texture("play.png")
        music = Gdx.audio.newMusic(Gdx.files.internal("back_audio.mp3"))
        //music.play()
    }
    override fun show() {

    }
    fun handleinput() {
        if (Gdx.input.isTouched) {
            var button : com.badlogic.gdx.math.Rectangle = com.badlogic.gdx.math.Rectangle((Gdx.graphics.width/2 - 125).toFloat(), (Gdx.graphics.height/2 - 125).toFloat(), 250f, 250f)
            var touchpos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())

            if (button.contains(touchpos)) {
                game.screen = FirstScreen(batch, game)
                music.pause()
            }
        }
        if (backpressed == true) {
            music.stop()
            backpressed = false
            this.dispose()
            exitProcess(0)

        }
    }

    override fun render(delta: Float) {
        handleinput()
        batch.begin()
        batch.draw(texture, 0f,0f , Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.draw(play, (Gdx.graphics.width/2 - 125).toFloat(), (Gdx.graphics.height/2 - 125).toFloat(), 250f, 250f)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        texture.dispose()
        play.dispose()
    }
}