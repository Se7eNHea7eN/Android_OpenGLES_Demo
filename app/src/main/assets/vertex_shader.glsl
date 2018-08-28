uniform mat4 uMVPMatrix; //常量MVP变换矩阵。mat4表示4×4浮点数矩阵，该变量存储了组合模型视图和投影矩阵。
//输入顶点着色器的属性。vec4表示包含了4个浮点数的向量。
attribute vec4 aPosition; // 顶点位置
uniform vec4 uColor; // 顶点颜色

// 输出变量，用来输入片段着色器
varying vec4 vColor; // 输出顶点颜色

void
main()
{
    vColor = uColor;//将输入的a_color赋值给输出的v_color
    gl_Position = uMVPMatrix * aPosition;//gl_Position 是内置的varying变量，不需要声明，顶点着色器必须把变换后的位置赋值给它。
}
