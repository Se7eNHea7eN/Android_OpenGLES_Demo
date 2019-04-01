void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
   float time = iGlobalTime;
   float screenScale = 4.0;
   vec2 p = (fragCoord.xy / iResolution.xx - 0.5) * 2.0 * screenScale;

   float factor = 1.;
   float offset = 0.;
   float scale = 1.;
   float cycle = 1.;
   for(int i = 0 ;i< 7 ; i++){
      vec2 r;
      r = vec2(cos(p.y + time), sin(p.x + time ));
      r += vec2(-r.y, r.x) * 0.3;
      p += r;
      factor *= 1.93;
   }
   
   vec3 color;
   color.r = sin(p.x + time)* 0.5 + 0.5;
   color.g = sin(p.y + time)* 0.5 + 0.5;
   color.b = sin((p.x+p.y + time )* 0.5)* 0.5 + 0.5;
   fragColor = vec4(color, 1.0);
}