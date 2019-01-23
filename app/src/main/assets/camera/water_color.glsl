void mainImage( out vec4 fragColor, in vec2 fragCoord )
 {
     int radius = int(iResolution.x/80.);
     vec2 src_size = 1. / iResolution.xy;
     vec2 uv = fragCoord;
     float n = float((radius + 1) * (radius + 1));
     int i; int j;

     vec3 m0 = vec3(0.0); vec3 m1 = vec3(0.0); vec3 m2 = vec3(0.0); vec3 m3 = vec3(0.0);
     vec3 s0 = vec3(0.0); vec3 s1 = vec3(0.0); vec3 s2 = vec3(0.0); vec3 s3 = vec3(0.0);
     vec3 c;

     for (j = -radius; j <= 0; ++j)  {
         for (i = -radius; i <= 0; ++i)  {
             c = texture2D(iChannel0, uv + vec2(i,j) * src_size).xyz;
             m0 += c;
             s0 += c * c;
         }
     }

     for (j = -radius; j <= 0; ++j)  {
         for (i = 0; i <= radius; ++i)  {
             c = texture2D(iChannel0, uv + vec2(i,j) * src_size).xyz;
             m1 += c;
             s1 += c * c;
         }
     }

     for (j = 0; j <= radius; ++j)  {
         for (i = 0; i <= radius; ++i)  {
             c = texture2D(iChannel0, uv + vec2(i,j) * src_size).xyz;
             m2 += c;
             s2 += c * c;
         }
     }

     for (j = 0; j <= radius; ++j)  {
         for (i = -radius; i <= 0; ++i)  {
             c = texture2D(iChannel0, uv + vec2(i,j) * src_size).xyz;
             m3 += c;
             s3 += c * c;
         }
     }


     float min_sigma2 = 1e+2;
     m0 /= n;
     s0 = abs(s0 / n - m0 * m0);

     float sigma2 = s0.x + s0.y + s0.z;
     if (sigma2 < min_sigma2) {
         min_sigma2 = sigma2;
         fragColor = vec4(m0, 1.0);
     }

     m1 /= n;
     s1 = abs(s1 / n - m1 * m1);

     sigma2 = s1.x + s1.y + s1.z;
     if (sigma2 < min_sigma2) {
         min_sigma2 = sigma2;
         fragColor = vec4(m1, 1.0);
     }

     m2 /= n;
     s2 = abs(s2 / n - m2 * m2);

     sigma2 = s2.x + s2.y + s2.z;
     if (sigma2 < min_sigma2) {
         min_sigma2 = sigma2;
         fragColor = vec4(m2, 1.0);
     }

     m3 /= n;
     s3 = abs(s3 / n - m3 * m3);

     sigma2 = s3.x + s3.y + s3.z;
     if (sigma2 < min_sigma2) {
         min_sigma2 = sigma2;
         fragColor = vec4(m3, 1.0);
     }
 }
