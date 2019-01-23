#extension GL_OES_EGL_image_external : require
#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform vec3 iResolution;
varying vec2 textureCoordinate;
uniform float iGlobalTime;

uniform samplerExternalOES iChannel0;