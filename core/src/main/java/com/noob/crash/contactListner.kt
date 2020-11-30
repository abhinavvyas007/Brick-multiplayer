package com.noob.crash

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array


class contactListner : ContactListener {
    var world : World
    var touch : Sound
    var lost : Sound

    constructor(world: World, batch: SpriteBatch) : super() {
        this.world = world
        touch = Gdx.audio.newSound(Gdx.files.internal("touch.mp3"))
        lost = Gdx.audio.newSound(Gdx.files.internal("lost_sound.mp3"))
    }
    override fun beginContact(contact: Contact) {
        var fixa : Fixture = contact.fixtureA
        var fixb: Fixture = contact.fixtureB
        var init_velo : Vector2
        var init_velo_tile : Vector2



        if (fixa.userData == null || fixb.userData == null) {return}


        if (fixa.userData is Ball && fixb.userData is Tile) {
            init_velo_tile = fixb.body.linearVelocity
            init_velo = fixa.body.linearVelocity
            fixa.body.setLinearVelocity(-init_velo.x, init_velo_tile.y / 5 + init_velo.y)
            touch.play()
        }
        else if (fixa.userData is Ball && fixb.userData is Bounds) {
            init_velo = fixa.body.linearVelocity
            if (init_velo.x > 0) {
                fixa.body.setLinearVelocity(init_velo.x + 0.1f, -init_velo.y)
            }
            else{
                fixa.body.setLinearVelocity(init_velo.x - 0.1f, -init_velo.y)
            }
            touch.play()
        }
        else if (fixa.userData is Ball && fixb.userData is Checker) {
            init_velo = fixa.body.linearVelocity
            if (init_velo.x > 0) {
                fixa.body.setLinearVelocity(-(init_velo.x + 0.1f), init_velo.y)
            }
            else { fixa.body.setLinearVelocity(-(init_velo.x - 1.2f), init_velo.y) }
            touch.play()
        }
        else if (fixa.userData is Ball && fixb.userData == "A"){
            gameover = true
            scoreB++
            lost.play()
        }
        else if (fixa.userData is Ball && fixb.userData == "B"){
            gameover = true
            scoreA++
            lost.play()
        }


        if (fixb.userData is Ball && fixa.userData is Tile) {
            init_velo = fixb.body.linearVelocity
            init_velo_tile = fixa.body.linearVelocity

            fixb.body.setLinearVelocity(-init_velo.x, init_velo.y + init_velo_tile.y / 5)
            touch.play()
        }
        else if (fixb.userData is Ball && fixa.userData is Bounds) {
            init_velo = fixb.body.linearVelocity
            if (init_velo.x > 0) {
                fixb.body.setLinearVelocity(init_velo.x + 0.1f, -init_velo.y)
            }
            else{
                fixb.body.setLinearVelocity(init_velo.x - 0.1f, -init_velo.y)
            }
            touch.play()
        }
        else if (fixb.userData is Ball && fixa.userData is Checker) {
            init_velo = fixb.body.linearVelocity
            if (init_velo.x > 0) {
                fixb.body.setLinearVelocity(-(init_velo.x + 0.1f), init_velo.y)
            } else {fixb.body.setLinearVelocity(-(init_velo.x - 0.1f), init_velo.y)}
            touch.play()
        }
        else if (fixb.userData is Ball && fixa.userData == "A") {
            gameover = true
            scoreB++
            lost.play()
        }
        else if (fixb.userData is Ball && fixa.userData == "B") {
            gameover = true
            scoreA++
            lost.play()
        }
    }

    override fun endContact(contact: Contact?) {

    }


    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {

    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {

    }
    fun dispose() {
        lost.dispose()
        touch.dispose()
    }

}