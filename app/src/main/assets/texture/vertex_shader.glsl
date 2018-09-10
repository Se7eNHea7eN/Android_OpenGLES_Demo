uniform mat4 uMVPMatrix;
attribute vec4 aPosition;
attribute vec4 aTextureCoordinate;

varying vec2 vTextureCoordinate;

void
main()
{
    vTextureCoordinate = aTextureCoordinate.xy;

    gl_Position = uMVPMatrix * aPosition;
}
