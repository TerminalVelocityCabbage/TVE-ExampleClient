package com.terminalvelocitycabbage.exampleclient.shapes;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.Resource;
import com.terminalvelocitycabbage.engine.util.Color;
import com.terminalvelocitycabbage.exampleclient.GameClient;
import com.terminalvelocitycabbage.exampleclient.GameResourceHandler;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Rectangle {

    float[] vertices;
    float[] colors;
    float[] textures;
    short[] indexes;

    public Rectangle(
            Vector3f topLeftPos, Vector3f topRightPos, Vector3f bottomLeftPos, Vector3f bottomRightPos,
            Vector4i topLeftColor, Vector4i topRightColor, Vector4i bottomLeftColor, Vector4i bottomRightColor,
            Vector2f topLeftTexture, Vector2f topRightTexture, Vector2f bottomLeftTexture, Vector2f bottomRightTexture
    ) {
        this.vertices = new float[] {
                topLeftPos.x,       topLeftPos.y,       topLeftPos.z,
                topRightPos.x,      topRightPos.y,      topRightPos.z,
                bottomLeftPos.x,    bottomLeftPos.y,    bottomLeftPos.z,
                bottomRightPos.x,   bottomRightPos.y,   bottomRightPos.z,
        };
        Vector4f tlC = Color.clampColor(topLeftColor);
        Vector4f trC = Color.clampColor(topRightColor);
        Vector4f blC = Color.clampColor(bottomLeftColor);
        Vector4f brC = Color.clampColor(bottomRightColor);
        this.colors = new float[] {
                tlC.x, tlC.y, tlC.z, tlC.w,
                trC.x, trC.y, trC.z, trC.w,
                blC.x, blC.y, blC.z, blC.w,
                brC.x, brC.y, brC.z, brC.w,
        };
        this.textures = new float[] {
                topLeftTexture.x, topLeftTexture.y,
                topRightTexture.x, topRightTexture.y,
                bottomLeftTexture.x, bottomLeftTexture.y,
                bottomRightTexture.x, bottomRightTexture.y
        };
        this.indexes = new short[] {
                0, 1, 2,	//left tri
                1, 2, 3		//right tri
        };
    }

    public float[] getFormattedVertices() {
        return new float[] {
                vertices[0], vertices[1],  vertices[2],      colors[0],  colors[1],  colors[2],  colors[3],       textures[0], textures[1],
                vertices[3], vertices[4],  vertices[5],      colors[4],  colors[5],  colors[6],  colors[7],       textures[2], textures[3],
                vertices[6], vertices[7],  vertices[8],      colors[8],  colors[9],  colors[10], colors[11],      textures[3], textures[4],
                vertices[9], vertices[10], vertices[11],     colors[12], colors[13], colors[14], colors[15],      textures[4], textures[5],
        };
    }

    public void bind() {
        //Create vertices buffer I guess
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        int vboVertID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboVertID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        //Create Color buffer I guess
        FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(colors.length);
        colorsBuffer.put(colors).flip();

        int vboColID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboColID);
        glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        //Create indices buffer
        ShortBuffer indicesBuffer = BufferUtils.createShortBuffer(indexes.length);
        indicesBuffer.put(indexes).flip();

        //Create element buffer I guess
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexes, GL_STATIC_DRAW);

        //Create texture stuff
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        //Configure wrapping parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // set texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        //Load image, create texture, and generate mipmaps
        Optional<Resource> resource = GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER.getResource(new Identifier(GameClient.ID, "textures/kyle.png"));
        Optional<ByteBuffer> image = resource.flatMap(Resource::getBytes);
        if (image.isPresent()) {
            //glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 99, 99, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.get());
            glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            Log.error("Could not bind texture: ");
        }
    }

    public void draw(int vao) {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
        glBindVertexArray(0);
    }
}
