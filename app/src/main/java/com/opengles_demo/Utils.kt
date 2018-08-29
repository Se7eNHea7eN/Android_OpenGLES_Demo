package asiainnovations.com.opengles_demo

import android.content.res.Resources

//通过路径加载Assets中的文本内容
fun getAssetAsString(mRes: Resources, path: String): String? {
    val result = StringBuilder()
    try {
        val `is` = mRes.assets.open(path)
        var ch: Int = 0
        val buffer = ByteArray(1024)
        while (-1 != (ch.let {
                    ch = `is`.read(buffer)
                    ch
                })) {
            result.append(String(buffer, 0, ch))
        }
    } catch (e: Exception) {
        return null
    }

    return result.toString().replace("\\r\\n".toRegex(), "\n")
}