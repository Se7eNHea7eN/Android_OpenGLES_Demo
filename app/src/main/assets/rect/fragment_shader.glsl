precision mediump float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

uniform lowp float saturation;

const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);

/*
    mix(): mix 函数将两个值 (例如颜色值) 混合为一个变量。
    如果我们有红和绿两个颜色，我们可以用 mix() 函数线性插值。
    这在图像处理中很常用，比如在应用程序中通过一组独特的设定来控制效果的强度等。
*/

void main()
{
    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    lowp float luminance = dot(textureColor.rgb, luminanceWeighting);
    lowp vec3 greyScaleColor = vec3(luminance);

    gl_FragColor = vec4(mix(greyScaleColor, textureColor.rgb, saturation), textureColor.w);
}