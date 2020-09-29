package com.terminalvelocitycabbage.exampleclient.shapes;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.terminalvelocitycabbage.exampleclient.shapes.TexturedVertex.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class TexturedRectangle extends ShapeTextured {

	public static final int TOP_LEFT = 0;
	public static final int BOTTOM_LEFT = 1;
	public static final int BOTTOM_RIGHT = 2;
	public static final int TOP_RIGHT = 3;

	TexturedVertex[] vertices = new TexturedVertex[4];
	public static final int RECTANGLE_SIZE = 4;
	public static final byte[] RECTANGLE_INDEX_ORDER = { 0, 1, 2, 2, 3, 0 };
	public static final int INDEX_LENGTH = RECTANGLE_INDEX_ORDER.length;

	private int vaoID;
	private int vboID;
	private int eboID;

	private Identifier texture;
	private int textureID;

	public TexturedRectangle(TexturedVertex topLeft, TexturedVertex bottomLeft, TexturedVertex bottomRight, TexturedVertex topRight, Identifier texture) {
		vertices[TOP_LEFT] = topLeft;
		vertices[BOTTOM_LEFT] = bottomLeft;
		vertices[BOTTOM_RIGHT] = bottomRight;
		vertices[TOP_RIGHT] = topRight;
		this.texture = texture;
	}

	public void bind() {
		//Create the VAO and bind it
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Create the VBO and bind it
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, getCombinedVertices(), GL_STATIC_DRAW);

		//Define vertex data for shader
		glVertexAttribPointer(0, POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, POSITION_OFFSET);
		glVertexAttribPointer(1, COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, COLOR_OFFSET);
		glVertexAttribPointer(2, TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, TEXTURE_OFFSET);

		//Create EBO for connected tris
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, getIndicesBuffer(), GL_STATIC_DRAW);

		//Load texture
		textureID = loadPNGTexture(texture, GL_TEXTURE0);
	}

	public void draw() {
		// Bind the texture
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureID);

		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		// Bind to the index VBO/EBO that has all the information about the order of the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

		// Draw the vertices
		glDrawElements(GL_TRIANGLES, INDEX_LENGTH, GL_UNSIGNED_BYTE, 0);

		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}

	public void destroy() {
		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
		glDeleteVertexArrays(vaoID);
		glDeleteTextures(textureID);
	}

	public void update() {
		// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
		FloatBuffer vertexFloatBuffer = getCombinedVertices();
		for (int i = 0; i < RECTANGLE_SIZE; i++) {
			vertexFloatBuffer.rewind();
			vertexFloatBuffer.put(vertices[i].getElements());
			vertexFloatBuffer.flip();
			glBufferSubData(GL_ARRAY_BUFFER, i * STRIDE, vertexFloatBuffer);
		}
	}

	public TexturedVertex[] getVertices() {
		return vertices;
	}

	public TexturedVertex getVertex(int index) {
		return vertices[index];
	}

	public FloatBuffer getCombinedVertices() {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * TexturedVertex.ELEMENT_COUNT);
		for (TexturedVertex vertex : vertices) {
			verticesBuffer.put(vertex.getElements());
		}
		return verticesBuffer.flip();
	}

	private ByteBuffer getIndicesBuffer() {
		return BufferUtils.createByteBuffer(INDEX_LENGTH).put(RECTANGLE_INDEX_ORDER).flip();
	}
}
