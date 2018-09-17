precision mediump float;

uniform vec3 uLightPos;
uniform sampler2D uTexture;

varying vec3 vPosition;
varying vec3 vNormal;
varying vec2 vTextureCoordinate;


void main()
{
	//光源距离当前点的距离
    float distance = length(uLightPos - vPosition);

	//从当前点指向光源的单位向量，记为光照方向
    vec3 lightVector = normalize(uLightPos - vPosition);

	// 计算光照向量和法线向量的点积。两个向量现在都是单位向量
	// 如果它们指向相同方向，那么返回最大值1。如果是钝角则置为0。
    float diffuse = max(dot(vNormal, lightVector), 0.0);

	// 计算发散。距离越远漫反射越低
    diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance)));

    // 添加环境光
    diffuse = diffuse + 0.2;

    gl_FragColor = (diffuse * texture2D(uTexture, vTextureCoordinate));
  }

