uniform mat4 uMVPMatrix;
uniform mat4 uMVMatrix;

attribute vec4 aPosition;
attribute vec3 aNormal;

varying vec3 vPosition;
varying vec3 vNormal;

void main()
{
	vPosition = vec3(uMVMatrix * aPosition);
    vNormal = vec3(uMVMatrix * vec4(aNormal, 0.0));

	gl_Position = uMVPMatrix * aPosition;
}