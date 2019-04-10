precision mediump float;

varying vec3 vPosition;
varying vec3 vNormal;

uniform vec3 objColor;

uniform float ambientStrength;
uniform vec3 lightColor;
uniform vec3 lightPos;

void main()
{
    vec3 ambient = ambientStrength * lightColor;

    vec3 norm = normalize(vNormal);
    vec3 lightDir = normalize(lightPos - vPosition);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    vec3 color =  (ambient + diffuse)  * objColor;

    gl_FragColor = vec4(color,1.0);
}

