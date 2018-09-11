precision mediump float;

varying highp vec2 vTextureCoordinate;

uniform sampler2D uTexture;

uniform gl_LightSourceParameters gl_LightSource[gl_MaxLights];

uniform gl_MaterialParameters gl_FrontMaterial;
uniform gl_MaterialParameters gl_BackMaterial;

varying vec3 vNormal;
varying vec3 vVertex;

void
main(void)
{
    vec3 L = normalize(gl_LightSource[0].position.xyz - vVertex);
    vec4 Idiff = gl_FrontLightProduct[0].diffuse * max(dot(vNormal,L), 0.0);
    Idiff = clamp(Idiff, 0.0, 1.0);
    gl_FragColor = Idiff * texture2D(uTexture, vTextureCoordinate);
}