#version 330

in vec4 gl_FragCoord;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 screenRes;
uniform mat3 cornerStuff;

void main() {

    //Make radius variable from uniform
    float radius = cornerStuff[2][2];

    //Create pixel coordinates from vertex positions
    vec2 tlP = ((vec2(cornerStuff[0][0], cornerStuff[0][1]) + 1) / 2) * screenRes;
    vec2 blP = ((vec2(cornerStuff[0][2], cornerStuff[1][0]) + 1) / 2) * screenRes;
    vec2 brP = ((vec2(cornerStuff[1][1], cornerStuff[1][2]) + 1) / 2) * screenRes;
    vec2 trP = ((vec2(cornerStuff[2][0], cornerStuff[2][1]) + 1) / 2) * screenRes;

    //Create reference coordinates for the center of the border radius circle
    vec2 tlR = vec2(tlP.x + radius, tlP.y - radius);
    vec2 blR = vec2(blP.x + radius, blP.y + radius);
    vec2 brR = vec2(brP.x - radius, brP.y + radius);
    vec2 trR = vec2(trP.x - radius, trP.y - radius);

    //Set the frag color to the element's background color
    fragColor = color;

    //Cull corners
    //Top Left
    if (distance(tlR, gl_FragCoord.xy) > radius) {
        if (tlR.x > gl_FragCoord.x && tlR.y < gl_FragCoord.y) {
            fragColor = vec4(0, 0, 0, 0);
        }
    }
    if (distance(blR, gl_FragCoord.xy) > radius) {
        if (blR.x > gl_FragCoord.x && blR.y > gl_FragCoord.y) {
            fragColor = vec4(0, 0, 0, 0);
        }
    }
    if (distance(brR, gl_FragCoord.xy) > radius) {
        if (brR.x < gl_FragCoord.x && brR.y > gl_FragCoord.y) {
            fragColor = vec4(0, 0, 0, 0);
        }
    }
    if (distance(trR, gl_FragCoord.xy) > radius) {
        if (trR.x < gl_FragCoord.x && trR.y < gl_FragCoord.y) {
            fragColor = vec4(0, 0, 0, 0);
        }
    }

}
