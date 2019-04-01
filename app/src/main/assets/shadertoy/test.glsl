
void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
    vec2 p = (2.0*fragCoord - iResolution.xy)/iResolution.x;
    vec3 bgColor = vec3(1.0,0.8,0.5);
    vec3 fgColor = vec3(1.0,0.0,0.0);

    float tt = mod(iTime,1.5)/1.5;
    float scale = 1.+0.5*(0.5*pow(tt,0.2)+0.5)*sin(18.8493*tt)*exp(-4.*tt);
    p *=vec2(0.5,1.5) + scale * vec2(0.5,-0.5);

    p.y-=0.25;
  
    float a = abs(atan(p.x,p.y)/3.141593);
    float d = (13.*a - 22.*a*a + 10.*a*a*a)/(6.-5.*a);
    float r = length(p);
    vec3 col = mix(fgColor,bgColor,smoothstep(-0.01,0.01,r-d));
    fragColor = vec4(col,1.0);
}