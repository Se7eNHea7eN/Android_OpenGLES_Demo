precision mediump float;

struct Light
{
    vec3 position;
    vec3 color;
    float intensive;
};

uniform Light lights[3];

uniform sampler2D uTexture;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTextureCoordinate;

void main()
{
    vec3 diffuse = vec3(0.2,0.2,0.2);  // 环境光默认0.2
    for(int i = 0 ; i < 3 ; i++ ){
        //光源距离当前点的距离
        float distance = length(lights[i].position - vPosition);

        //从当前点指向光源的单位向量，记为光照方向
        vec3 lightVector = normalize(lights[i].position - vPosition);

        // 计算光照向量和法线向量的点积。两个向量现在都是单位向量
        // 如果它们指向相同方向，那么返回最大值1。如果是钝角则置为0。
        vec3 currentDiffuse = max(dot(vNormal, lightVector), 0.0) * lights[i].intensive * lights[i].color;

        // 计算发散。距离越远漫反射越低
        currentDiffuse = currentDiffuse * (1.0 / (1.0 + (0.25 * distance)));

        diffuse = diffuse + currentDiffuse;
    }

    gl_FragColor =texture2D(uTexture, vTextureCoordinate) * vec4(diffuse , 0) ;
}

