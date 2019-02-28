package com.opengles_demo.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.WindowManager
import androidx.core.content.ContextCompat
import asiainnovations.com.opengles_demo.GlShader
import asiainnovations.com.opengles_demo.fragments.BaseGLFragment
import asiainnovations.com.opengles_demo.getAssetAsString
import com.asiainnovations.onlyu.video.gl.GlUtil
import com.asiainnovations.onlyu.video.gl.TextureRotationUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10



open class CameraFilterFragment : BaseGLFragment() {
    companion object {
        fun newInstance(shaderName: String): CameraFilterFragment {
            return CameraFilterFragment().apply {
                arguments = Bundle().apply {
                    putString("ShaderName", shaderName)
                }
            }
        }
    }

    protected lateinit var shader: GlShader

    private var cameraId: String? = null
    private val mCameraOpenCloseLock = Semaphore(1)
    private var cameraDevice: CameraDevice? = null
    private var cameraTexture = -1
    private var surfaceTexture: SurfaceTexture? = null

    private var previewSize: Size = Size(0, 0)
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    private var captureSession: CameraCaptureSession? = null
    private var captureRequest: CaptureRequest? = null
    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    private var startRenterTime = 0L
    /**
     * Compares two `Size`s based on their areas.
     */
    internal class CompareSizesByArea : Comparator<Size> {

        override fun compare(lhs: Size, rhs: Size): Int {
            // We cast here to ensure the multiplications won't overflow
            return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
        }

    }

    /**
     * Given `choices` of `Size`s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     * class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal `Size`, or an arbitrary one if none were big enough
     */
    private fun chooseOptimalSize(choices: Array<Size>, textureViewWidth: Int,
                                  textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size): Size {

        // Collect the supported resolutions that are at least as big as the preview Surface
        val bigEnough = ArrayList<Size>()
        // Collect the supported resolutions that are smaller than the preview Surface
        val notBigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                    option.height == option.width * h / w) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size > 0) {
            return Collections.min(bigEnough, CompareSizesByArea())
        } else if (notBigEnough.size > 0) {
            return Collections.max(notBigEnough, CompareSizesByArea())
        } else {
            //Log.e(TAG, "Couldn't find any suitable preview size")
            return choices[0]
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        mBackgroundThread = HandlerThread("CameraBackground")
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.getLooper())

        if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw RuntimeException("Time out waiting to lock camera opening.")
        }

        (context!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager).apply {
            cameraId = cameraIdList
                    .find {
                        getCameraCharacteristics(it)
                                .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
                    }
            assert(cameraId != null)
            val cameraCharacteristic = getCameraCharacteristics(cameraId!!)
            val map = cameraCharacteristic.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            val displaySize = Point()

            val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getRealSize(displaySize)

            //activity!!.windowManager.defaultDisplay.getSize(displaySize)

            val rotatedPreviewWidth = 720 * displaySize.y / displaySize.x
            val rotatedPreviewHeight = 720
            val maxPreviewWidth = displaySize.y
            val maxPreviewHeight = displaySize.x

//            val largest = Collections.max(
//                    Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
//                    CompareSizesByArea())

            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                    rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                    maxPreviewHeight, Size(displaySize.y,displaySize.x))
            Log.d("debug","previewSize = ${previewSize.width} , ${previewSize.height}")

            openCamera(cameraId!!, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraOpenCloseLock.release()
                    cameraDevice = camera

                    glSurfaceView?.queueEvent {
                        if (cameraTexture == -1) {
                            cameraTexture = GlUtil.generateTexture(GL_TEXTURE_EXTERNAL_OES)
                        }
                        surfaceTexture = SurfaceTexture(cameraTexture)
                        surfaceTexture!!.setDefaultBufferSize(previewSize.width, previewSize.height)
                        surfaceTexture!!.setOnFrameAvailableListener {
                            glSurfaceView!!.requestRender()
                        }

                        activity!!.runOnUiThread {
                            val surface = Surface(surfaceTexture)
                            previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            previewRequestBuilder!!.addTarget(surface)
                            cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                                override fun onConfigureFailed(session: CameraCaptureSession) {
                                }

                                override fun onConfigured(session: CameraCaptureSession) {
                                    captureSession = session
                                    try {
                                        previewRequestBuilder!!.set(CaptureRequest.CONTROL_AF_MODE,
                                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                        captureRequest = previewRequestBuilder!!.build()
                                        captureSession!!.setRepeatingRequest(captureRequest!!, null, mBackgroundHandler)
                                    } catch (e: CameraAccessException) {
                                        e.printStackTrace()
                                    }

                                }
                            }, null)
                        }
                    }

                }

                override fun onDisconnected(camera: CameraDevice) {
                    mCameraOpenCloseLock.release()
                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    mCameraOpenCloseLock.release()
                    cameraDevice?.close()
                    cameraDevice = null
                }
            }, mBackgroundHandler)
        }
    }

    private fun closeCamera() {
        try {
            mCameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            mCameraOpenCloseLock.release()
        }

        if (mBackgroundThread == null)
            return
        mBackgroundThread?.quitSafely()
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
        }
        glSurfaceView!!.queueEvent {
            GLES20.glDeleteTextures(1, intArrayOf(cameraTexture), 0)
            cameraTexture = -1
            surfaceTexture?.release()
            surfaceTexture = null
        }
    }

    override fun onResume() {
        super.onResume()
        openCamera()
    }

    override fun onPause() {
        super.onPause()
        closeCamera()
    }

    private var vertexBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.CUBE.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(TextureRotationUtil.CUBE)
                position(0)
            }

    private val textureMappingBuffer: FloatBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_ROTATED_270.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(TextureRotationUtil.getRotation(270,false,true))
                position(0)
            }

    override fun onDrawFrame(gl: GL10?) {
        surfaceTexture?.updateTexImage()
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (cameraTexture != -1) {
            //激活纹理单元0
            glActiveTexture(GL_TEXTURE0)
            //绑定纹理
            glBindTexture(GL_TEXTURE_EXTERNAL_OES, cameraTexture)
            //设置到0号纹理单元
            glUniform1i(shader.getUniformLocation("iChannel0"), 0)
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)
        }

        glGetUniformLocation(shader.program, "iGlobalTime").apply {
            if(this >=0 ){
                glUniform1f(this,(System.currentTimeMillis() - startRenterTime)/1000f)
            }
        }

        shader.setVertexAttribArray("position", 2, vertexBuffer)
        shader.setVertexAttribArray("inputTextureCoordinate", 2, textureMappingBuffer)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        val ratio1 = width.toFloat() / previewSize.height
        val ratio2 = height.toFloat() / previewSize.width
        val ratioMax = Math.min(ratio1, ratio2)
        val imageWidthNew = Math.round(previewSize.height * ratioMax)
        val imageHeightNew = Math.round(previewSize.width * ratioMax)

        val ratioWidth = imageWidthNew / width.toFloat()
        val ratioHeight = imageHeightNew / height.toFloat()

        val cube = floatArrayOf(TextureRotationUtil.CUBE[0] / ratioHeight, TextureRotationUtil.CUBE[1] / ratioWidth, TextureRotationUtil.CUBE[2] / ratioHeight, TextureRotationUtil.CUBE[3] / ratioWidth, TextureRotationUtil.CUBE[4] / ratioHeight, TextureRotationUtil.CUBE[5] / ratioWidth, TextureRotationUtil.CUBE[6] / ratioHeight, TextureRotationUtil.CUBE[7] / ratioWidth)

        vertexBuffer = ByteBuffer.allocateDirect(cube.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()

        vertexBuffer.clear()
        vertexBuffer.put(cube).position(0)

        glGetUniformLocation(shader.program, "iResolution").apply {
            if(this >=0 ){
                val param = floatArrayOf(previewSize.height.toFloat(), previewSize.width.toFloat(), 1f)
                glUniform3fv(this,1,param,0)
            }
        }

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        shader = GlShader(getAssetAsString(resources, "camera/vertex_shader.glsl")!!,
                getAssetAsString(resources, "camera/fragment_header.glsl")!! +
                        "\n" +
                        getAssetAsString(resources, "camera/${arguments!!.getString("ShaderName")}.glsl")!! +
                        "\n" +
                        getAssetAsString(resources, "camera/fragment_footer.glsl")!!
        )
        shader.useProgram()
        startRenterTime = System.currentTimeMillis()
    }

}