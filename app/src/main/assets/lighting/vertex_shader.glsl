uniform mat4 uMVPMatrix;
attribute vec4 aVertex;
attribute vec4 aNormal;
attribute vec4 aTextureCoordinate;

varying vec2 vTextureCoordinate;
varying vec3 vNormal;
varying vec3 vVertex;

void
main()
{
    vVertex = vec3(uMVPMatrix * aVertex);
    vNormal = normalize(uMVPMatrix * aNormal);

    vTextureCoordinate = aTextureCoordinate.xy;

    gl_Position = uMVPMatrix * aVertex;
}
