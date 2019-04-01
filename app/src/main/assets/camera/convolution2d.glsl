uniform float kernel[900];
uniform float offset;
uniform int kernelSize;

void mainImage( out vec4 fragColor, in vec2 uv )
{
    vec4 color = vec4(0.0);
    int halfSize = kernelSize / 2;

    int index = 0;
    for(int y=-halfSize;y<=halfSize;y++)
    {
        for(int x = -halfSize;x<=halfSize;x++)
        {
            color += texture2D(iChannel0,uv + vec2(x,y)/iResolution.xy) * kernel[index];
            index++;
        }
    }
    fragColor = vec4(color.xyz +vec3(offset),1.0);
}
