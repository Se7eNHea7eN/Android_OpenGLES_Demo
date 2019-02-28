package com.opengles_demo.fragments

import android.os.Bundle
import javax.microedition.khronos.opengles.GL10

class ConvolutionFragment : CameraFilterFragment() {
    companion object {
        fun newInstance(kernel: FloatArray, kernelSize: Int,offset:Float = 0f): ConvolutionFragment {
            return ConvolutionFragment().apply {
                arguments = Bundle().apply {
                    putString("ShaderName", "convolution2d")
                    putFloatArray("kernel", kernel)
                    putInt("kernelSize", kernelSize)
                    putFloat("offset",offset)
                }
            }
        }
    }
    private var kernel:FloatArray = FloatArray(0)
    private var kernelSize = 0
    private var offset = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kernel = arguments!!.getFloatArray("kernel")!!
        kernelSize = arguments!!.getInt("kernelSize")
        offset = arguments!!.getFloat("offset")
    }

    override fun onDrawFrame(gl: GL10?) {
        shader.setUniformFloatArray("kernel",kernel)
        shader.setUniformInt("kernelSize",kernelSize)
        shader.setUniformFloat("offset",offset)
        super.onDrawFrame(gl)
    }
}