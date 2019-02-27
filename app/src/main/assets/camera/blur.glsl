void mainImage( out vec4 fragColor, in vec2 uv )
{
//    float kernal[9] = {1.,2.,1.,2.,4.,2.,1.,2.,1.};
    vec4 color = vec4(0.0);
    int coreSize=3;
    int halfSize=coreSize/2;
    float texelOffset = 100.0;
    float kernel[9];
    kernel[6] = 1.; kernel[7] = 2.; kernel[8] = 1.;
    kernel[3] = 2.; kernel[4] = 4.; kernel[5] = 2.;
    kernel[0] = 1.; kernel[1] = 2.; kernel[2] = 1.;
    int index = 0;
    for(int y=-1;y<=1;y++)
    {
      for(int x = -1;x<=1;x++)
      {
          vec4 currentColor = texture2D(iChannel0,uv + vec2(x,y)/texelOffset);
          color += currentColor*kernel[index];
          index++;
      }
    }
    color/=16.0;
    fragColor = color;
}
