


void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord.xy;

	float offset = 0.0;

	offset = (1.0 + sin(iGlobalTime*6.0)) * 0.5;
	offset *= 1.0 + sin(iGlobalTime*16.0) * 0.5;
	offset *= 1.0 + sin(iGlobalTime*19.0) * 0.5;
	offset *= 1.0 + sin(iGlobalTime*27.0) * 0.5;
	offset = pow(offset, 3.0);

	offset *= 0.05;

    vec3 col;
    col.r = texture2D( iChannel0, vec2(uv.x+offset,uv.y) ).r;
    col.g = texture2D( iChannel0, uv ).g;
    col.b = texture2D( iChannel0, vec2(uv.x-offset,uv.y) ).b;

	col *= (1.0 - offset * 0.5);

    fragColor = vec4(col,1.0);
}

