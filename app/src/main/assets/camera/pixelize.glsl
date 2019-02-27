
void mainImage(out vec4 fragColor, vec2 textureCoordinate)
{
    float S = iResolution.x / 6e1;
    fragColor = texture2D(iChannel0, floor((textureCoordinate*iResolution.xy + .5) / S) * S / iResolution.xy);
}

