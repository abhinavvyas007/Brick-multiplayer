package com.noob.crash

import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.physics.box2d.*

class Bounds {
    var map : TiledMap
    var world : World
    var PPM = 100f
    constructor(map : TiledMap, world: World) {
        this.map = map
        this.world = world
    }

    fun getobject(){
        var body: Body
        var bdef = BodyDef()
        var fdef = FixtureDef()
        for (obj in map.layers.get("Object Layer 1").objects.getByType(PolygonMapObject::class.java)) {
            var shape = createShape(obj)
            bdef.type = BodyDef.BodyType.StaticBody
            bdef.position.set(obj.properties.get("x") as Float / PPM, obj.properties.get("y") as Float / PPM)
            fdef.shape = shape
            body = world.createBody(bdef)
            body.createFixture(fdef).setUserData(this)
        }
    }
    fun createShape(obj: PolygonMapObject): Shape {
        var cs = ChainShape()
        var worldvertices = FloatArray(obj.polygon.vertices.lastIndex + 1)
        for (i in 0..worldvertices.lastIndex) {
            worldvertices[i] = obj.polygon.vertices[i]/PPM
        }
        cs.createChain(worldvertices)
        return cs
    }
}