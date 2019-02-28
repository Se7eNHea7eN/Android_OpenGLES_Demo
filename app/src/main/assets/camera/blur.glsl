void mainImage( out vec4 fragColor, in vec2 uv )
{
    vec4 color = vec4(0.0);
    float kernel[9];
    kernel[6] = 0.0947416; kernel[7] = 0.118318; kernel[8] = 0.0947416;
    kernel[3] = 0.118318;  kernel[4] = 0.147761; kernel[5] = 0.118318;
    kernel[0] = 0.0947416; kernel[1] = 0.118318; kernel[2] = 0.0947416;
    int index = 0;
    for(int y=-1;y<=1;y++)
    {
      for(int x = -1;x<=1;x++)
      {
          vec4 currentColor = texture2D(iChannel0,uv + vec2(x,y)/iResolution.xy);
          color += currentColor*kernel[index];
          index++;
      }
    }
    fragColor = color;
}
