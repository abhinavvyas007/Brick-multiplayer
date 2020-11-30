package com.noob.crash

import com.badlogic.gdx.physics.box2d.*

class Ball {
    var PPM = 100f
    var world : World
    lateinit var body : Body
    lateinit var fixtureC : Fixture
    constructor(world: World) {
        this.world = world
    }

    fun createBall() : Body {
        var bdef : BodyDef
        var shape = CircleShape()
        shape.radius = 8f/PPM
        bdef = BodyDef()
        bdef.type = BodyDef.BodyType.DynamicBody
        bdef.position.set(200/PPM,140f/PPM)
        bdef.fixedRotation = true
        body = world.createBody(bdef)
        var fdef = FixtureDef()
        fdef.shape = shape
        fdef.density = 0.000001f
        body.createFixture(fdef).setUserData(this)
        body.setLinearVelocity(300f/PPM, 0f)
        return body
    }
}