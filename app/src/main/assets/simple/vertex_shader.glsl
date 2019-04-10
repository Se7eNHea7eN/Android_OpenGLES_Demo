uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
attribute vec4 aPosition; // 顶点位置
attribute vec4 aColor; // 顶点颜色

// 输出变量，用来输入片段着色器
varying vec4 vColor; // 输出顶点颜色

void
main()
{
    vColor = aColor;//将输入的a_color赋值给输出的v_color
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * aPosition;//gl_Position 是内置的varying变量，不需要声明，顶点着色器必须把变换后的位置赋值给它。
}
