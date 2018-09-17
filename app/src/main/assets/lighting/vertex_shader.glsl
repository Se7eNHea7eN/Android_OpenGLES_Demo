uniform mat4 uMVPMatrix;
uniform mat4 uMVMatrix;

attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTextureCoordinate;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTextureCoordinate;

void main()
{
	vPosition = vec3(uMVMatrix * aPosition);

	vTextureCoordinate = aTextureCoordinate;

    vNormal = vec3(uMVMatrix * vec4(aNormal, 0.0));

	gl_Position = uMVPMatrix * aPosition;
}