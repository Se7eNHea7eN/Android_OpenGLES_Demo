package com.opengles_demo

object KernalProvider {
    fun normalized(size: Int): FloatArray {
        return size.let {
            FloatArray(it * it).apply {
                fill(1f / (it * it))
            }
        }
    }

    fun gauss(size: Int, sigma: Float): FloatArray {
        var kernel = FloatArray(size * size)

        val sigmaX : Double = if (sigma > 0) sigma.toDouble() else ((size - 1) * 0.5 - 1) * 0.3 + 0.8
        val scale2X = - 0.5 / (sigmaX * sigmaX)
        var sum: Double = 0.0

        for( i in 0 until size){
            val x = i -(size - 1) * 0.5
            val t =  Math.exp(scale2X*x*x)
            kernel[i] = t.toFloat()
            sum += t
        }

        sum = 1f/ sum
        for( i in 0 until size) {
            kernel[i] *= sum.toFloat()
        }

        return kernel
    }


    fun laplace(): FloatArray {
        return floatArrayOf(
                1f, 1f, 1f
                , 1f, -8f, 1f,
                1f, 1f, 1f)
    }

    fun relief(): FloatArray {
        return floatArrayOf(
                1f, 1f, 0f,
                1f, 0f, -1f,
                0f, -1f, -1f)
    }
}