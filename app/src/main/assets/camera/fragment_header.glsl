#extension GL_OES_EGL_image_external : require
#extension GL_OES_standard_derivatives : enable

precision mediump float;
varying highp vec2 textureCoordinate;

uniform samplerExternalOES iChannel0;