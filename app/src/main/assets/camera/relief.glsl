float max3 (vec3 v) {
  return max (max (v.x, v.y), v.z);
}

void mainImage( out vec4 fragColor, in vec2 uv )
{
    vec3 color = texture2D(iChannel0,uv).xyz;
    vec3 offsetColor = texture2D(iChannel0,uv + 1./iResolution.xy).xyz;
    vec3 diffs = offsetColor - color;

    float maxV = max3(diffs);

    float gray = clamp(maxV + 0.3,0.,1.);
    fragColor = vec4(gray,gray,gray,1.0);
}
