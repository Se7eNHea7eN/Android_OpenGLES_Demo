void mainImage( out vec4 fragColor, in vec2 fragCoord ) {
   float time = iGlobalTime;
   vec2 uv = (fragCoord.xy / iResolution.xx - 0.5) * 8.0;
   vec2 uv0 = uv;
   float factor = 1.0;
   float cycle = 1.0;
   float scale = 1.0;
   float offset = 0.0;
   for (int s = 0; s < 1; s++) {
     vec2 r;
     r = vec2(cos(uv.y * factor - offset + time / cycle), sin(uv.x * factor - offset + time / cycle)) / scale;
     r += vec2(-r.y, r.x) * 0.3;
     uv.xy += r;

     factor *= 1.93;
     cycle *= 1.15;
     scale *= 1.7;
     offset += 0.05 + 0.1 * time * cycle;
   }
   vec3 color;
   color.r = sin(uv.x - time) * 0.5 + 0.5;
   color.b = sin(uv.y + time) * 0.5 + 0.5;
   color.g = sin((uv.x + uv.y + sin(time * 0.5)) * 0.5) * 0.5 + 0.5;
   fragColor = vec4(color, 1.0);
}