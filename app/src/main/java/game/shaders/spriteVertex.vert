#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

out vec2 outTextCoord;

uniform mat4 ortho;
uniform mat4 view;

void main()
{
    gl_Position = ortho * view * vec4(position, 1.0);
    outTextCoord = texCoord;
}