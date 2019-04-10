package com.opengles_demo

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class ObjLoader(context: Context, file: String) {

    val numFaces: Int

    val normals: FloatArray
    val textureCoordinates: FloatArray
    val vertices: FloatArray

    init {

        val vertices = Vector<Float>()
        val normals = Vector<Float>()
        val textures = Vector<Float>()
        val faces = Vector<String>()

        var reader: BufferedReader? = null
        try {
            val `in` = InputStreamReader(context.assets.open(file))
            reader = BufferedReader(`in`)
            // read file until EOF
            reader.useLines {
                it.forEach { line ->
                    val parts = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    when (parts[0]) {
                        "v" -> {
                            // vertices
                            vertices.add(java.lang.Float.valueOf(parts[1]))
                            vertices.add(java.lang.Float.valueOf(parts[2]))
                            vertices.add(java.lang.Float.valueOf(parts[3]))
                        }
                        "vt" -> {
                            // textures
                            textures.add(java.lang.Float.valueOf(parts[1]))
                            textures.add(java.lang.Float.valueOf(parts[2]))
                        }
                        "vn" -> {
                            // normals
                            normals.add(java.lang.Float.valueOf(parts[1]))
                            normals.add(java.lang.Float.valueOf(parts[2]))
                            normals.add(java.lang.Float.valueOf(parts[3]))
                        }
                        "f" -> {
                            // faces: vertex/texture/normal
                            faces.add(parts[1])
                            faces.add(parts[2])
                            faces.add(parts[3])
                        }
                    }
                }
            }

        } catch (e: IOException) {
            // cannot load or read file
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                //log the exception
            }
        }

        numFaces = faces.size
        this.normals = FloatArray(numFaces * 3)
        textureCoordinates = FloatArray(numFaces * 2)
        this.vertices = FloatArray(numFaces * 3)
        var positionIndex = 0
        var normalIndex = 0
        var textureIndex = 0
        for (face in faces) {
            val parts = face.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            var index = 3 * (java.lang.Short.valueOf(parts[0])!! - 1)
            this.vertices[positionIndex++] = vertices[index++]
            this.vertices[positionIndex++] = vertices[index++]
            this.vertices[positionIndex++] = vertices[index]

            index = 2 * (java.lang.Short.valueOf(parts[1])!! - 1)
            textureCoordinates[normalIndex++] = textures[index++]
            // NOTE: Bitmap gets y-inverted
            textureCoordinates[normalIndex++] = 1 - textures[index]

            index = 3 * (java.lang.Short.valueOf(parts[2])!! - 1)
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index++]
            this.normals[textureIndex++] = normals[index]
        }
    }
}