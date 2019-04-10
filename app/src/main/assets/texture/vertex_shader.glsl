uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
attribute vec4 aPosition;
attribute vec4 aTextureCoordinate;

varying vec2 vTextureCoordinate;

void
main()
{
    vTextureCoordinate = aTextureCoordinate.xy;

    gl_Position = projectionMatrix * viewMatrix * modelMatrix * aPosition;
}
