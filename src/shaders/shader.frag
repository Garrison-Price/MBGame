uniform int numLights;
uniform vec3 lightPositions[256];
uniform vec3 lightValues[256];

varying vec4 diffuses[256];
varying vec3 normal;
varying vec3 lightDirs[256];
varying vec3 halfVectors[256];
varying float distances[256];
varying float passNumLights;

void main()
{
    vec3 n,halfV;
    float NdotL,NdotHV;
    vec4 color = vec4(.2,.2,.2,1);

    float att;
    float constantAttenuation=1.0;
    float linearAttenuation=0.22;
    float quadraticAttenuation=0.20; 

    n = normalize(normal);

    for(int i = 0; i < passNumLights; i++)
    {
        NdotL = max(dot(n,normalize(lightDirs[0])),0.0);
        if (NdotL > 0.0) 
        {
            att = 1.0 / (constantAttenuation +
                            linearAttenuation * distances[0] +
                            quadraticAttenuation * distances[0] * distances[0]);

            color += att * (diffuses[0] * NdotL);
            halfV = normalize(halfVectors[0]);

            NdotHV = max(dot(n,halfV),0.0);
            color += att * gl_FrontMaterial.specular * pow(NdotHV,gl_FrontMaterial.shininess);
        }
    }
    gl_FragColor = color;
}