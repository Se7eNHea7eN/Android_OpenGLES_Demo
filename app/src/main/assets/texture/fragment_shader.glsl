precision mediump float;

varying highp vec2 vTextureCoordinate;

uniform sampler2D uTexture;

void
main(void)
{
    gl_FragColor = texture2D(uTexture, vTextureCoordinate);
}