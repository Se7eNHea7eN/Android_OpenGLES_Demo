uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

attribute vec4 aPosition;
attribute vec3 aNormal;

varying vec3 vPosition;
varying vec3 vNormal;

void main()
{
	vPosition = vec3(modelMatrix  * aPosition);
    vNormal = vec3(modelMatrix  * vec4(aNormal, 0.0));

	gl_Position = projectionMatrix * viewMatrix * modelMatrix * aPosition;
}