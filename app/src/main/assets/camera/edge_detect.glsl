
void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;
    vec4 color =  texture2D(iChannel0, fragCoord);
    float gray = length(color.rgb);
    //genType step (float edge, genType x) 如果x < edge，返回0.0，否则返回1.0
    fragColor = vec4(vec3(step(0.06, length(vec2(dFdx(gray), dFdy(gray))))), 1.0);
}