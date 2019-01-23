attribute vec2 position;
attribute vec2 inputTextureCoordinate;

varying vec2 textureCoordinate;

void main()
{
    textureCoordinate = inputTextureCoordinate.xy;
    gl_Position = vec4 ( position.xy, 0., 1. );
}