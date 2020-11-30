package com.noob.crash

import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.*

class Checker {
    var map : TiledMap
    var world : World
    var PPM = 100f
    constructor(world: World, map: TiledMap) {
        this.map = map
        this.world = world
    }
    fun getchecker(x : Float, y : Float) {
        var body: Body
        var bdef = BodyDef()
        var fdef = FixtureDef()
        for (obj in map.layers.get("Object Layer 2").objects.getByType(PolylineMapObject::class.java)) {
            var shape = createchecker(obj)
            bdef.type = BodyDef.BodyType.StaticBody
            bdef.position.set(x / PPM, y / PPM)
            fdef.shape = shape
            body = world.createBody(bdef)
            body.createFixture(fdef).setUserData(this)
        }
    }
    fun createchecker(obj : PolylineMapObject) : Shape {
        var cs = ChainShape()
        var worldvertices = FloatArray(obj.polyline.vertices.lastIndex + 1)
        for (i in 0..worldvertices.lastIndex) {
            worldvertices[i] = obj.polyline.vertices[i]/PPM
        }
        cs.createChain(worldvertices)
        return cs
    }
}