uniform mat4 uMVPMatrix;
attribute vec4 aPosition;

void main()                    
{                              
	gl_Position = uMVPMatrix * aPosition;
    gl_PointSize = 5.0;         
}                              