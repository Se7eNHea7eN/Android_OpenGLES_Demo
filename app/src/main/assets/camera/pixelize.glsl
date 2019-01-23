
void mainImage(out vec4 c, vec2 p)
{
    float S = iResolution.x / 6e1;
    c = texture2D(iChannel0, floor((textureCoordinate*iResolution.xy + .5) / S) * S / iResolution.xy);
}

