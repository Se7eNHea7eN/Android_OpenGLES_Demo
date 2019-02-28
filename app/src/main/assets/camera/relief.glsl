
void mainImage( out vec4 fragColor, in vec2 uv )
{
    vec4 color = vec4(0.0);
    float kernel[9];
    kernel[6] = -1.; kernel[7] = -1.; kernel[8] = 0.;
    kernel[3] = -1.; kernel[4] = 0.; kernel[5] = 1.;
    kernel[0] = 0.; kernel[1] = 1.; kernel[2] = 1.;
    int index = 0;
    for(int y=-1;y<=1;y++)
    {
        for(int x = -1;x<=1;x++)
        {
          color += texture2D(iChannel0,uv + vec2(x,y)/iResolution.xy)*kernel[index];
          index++;
        }
    }
    fragColor = vec4(color.xyz +vec3(.5),1.0);

}
