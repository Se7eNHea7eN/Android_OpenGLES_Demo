package com.opengles_demo.gl

object Cube {
    // X, Y, Z
    val vertices = floatArrayOf(
            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f
    )

    val cubeNormalData = floatArrayOf(
            // Front face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            // Right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            // Back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            // Left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            // Top face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            // Bottom face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f
    )

    val textureCoordinate = floatArrayOf(
            // Front face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,

            // Right face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,

            // Back face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,

            // Left face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,

            // Top face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f,

            // Bottom face
            0.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f
    )


    val colors = floatArrayOf(// Colors of the 6 faces
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.5f, 0.0f, 1.0f, // 0. orange
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            1.0f, 0.0f, 1.0f, 1.0f, // 1. violet
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 1.0f, 0.0f, 1.0f, // 2. green
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            0.0f, 0.0f, 1.0f, 1.0f, // 3. blue
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 0.0f, 0.0f, 1.0f, // 4. red
            1.0f, 1.0f, 0.0f, 1.0f,  // 5. yellow
            1.0f, 1.0f, 0.0f, 1.0f,  // 5. yellow
            1.0f, 1.0f, 0.0f, 1.0f,  // 5. yellow
            1.0f, 1.0f, 0.0f, 1.0f,  // 5. yellow
            1.0f, 1.0f, 0.0f, 1.0f,  // 5. yellow
            1.0f, 1.0f, 0.0f, 1.0f  // 5. yellow
    )

}