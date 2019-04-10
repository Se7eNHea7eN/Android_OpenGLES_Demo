uniform mat4 projectionMatrix;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;

attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTextureCoordinate;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTextureCoordinate;

void main()
{
	vPosition = vec3( viewMatrix * modelMatrix  * aPosition);

	vTextureCoordinate = aTextureCoordinate;

    vNormal = vec3(viewMatrix * modelMatrix  * vec4(aNormal, 0.0));

	gl_Position = projectionMatrix * viewMatrix * modelMatrix  * aPosition;
}