package com.noob.crash

import com.badlogic.gdx.physics.box2d.*

class Tile {
    var PPM = 100f
    var world: World

    constructor(world: World) {
        this.world = world
    }

    fun createBox(x: Int, y: Int, width: Int, height: Int): Body {
        var body: Body
        var bdef = BodyDef()

        bdef.type = BodyDef.BodyType.DynamicBody
        bdef.position.set(x.toFloat() / PPM, y.toFloat() / PPM)
        bdef.fixedRotation = true
        body = world.createBody(bdef)
        var shape = PolygonShape()
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM)
        var fdef = FixtureDef()
        fdef.shape = shape
        fdef.density = 1f
        body.createFixture(shape, 1f).setUserData(this)
        shape.dispose()
        return body
    }
}
