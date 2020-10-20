package com.terminalvelocitycabbage.exampleclient.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DCModel extends Model {

    private final Map<String, Model.Part> partMap = new HashMap<>();

    private DCModel(Info model) {
        super(model.getChildren().stream().map(Part::new).collect(Collectors.toList()));
        recursiveOnPart(modelParts, p -> partMap.put(p.name, p));
    }

    private void recursiveOnPart(List<Model.Part> modelParts, Consumer<Part> consumer) {
        for (Model.Part part : modelParts) {
            consumer.accept((Part) part);
            recursiveOnPart(part.children, consumer);
        }
    }

    public Optional<Model.Part> getPart(String name) {
        return this.partMap.containsKey(name) ? Optional.of(this.partMap.get(name)) : Optional.empty();
    }

    private static class Part extends Model.Part {

        String name;

        public Part(Cube cube) {
            super(
                new TexturedCuboid(
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][2], cube.uvs[0][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][2], cube.uvs[0][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][0], cube.uvs[0][1]),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][0], cube.uvs[0][3]),

                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][2], cube.uvs[1][3]),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][2], cube.uvs[1][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][0], cube.uvs[1][1]),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][0], cube.uvs[1][3]),

                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][2], cube.uvs[2][3]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][2], cube.uvs[2][1]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][0], cube.uvs[2][1]),
                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][0], cube.uvs[2][3]),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][2], cube.uvs[3][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][2], cube.uvs[3][1]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][0], cube.uvs[3][1]),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][0], cube.uvs[3][3]),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][2], cube.uvs[5][3]),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][2], cube.uvs[5][1]),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][0], cube.uvs[5][1]),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][0], cube.uvs[5][3]),

                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][0], cube.uvs[4][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][0], cube.uvs[4][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][2], cube.uvs[4][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][2], cube.uvs[4][1]),

                    new Material(new Texture(cube.model.resourceManager, cube.model.textureIdentifier))
                ),
                cube.offset,
                cube.rotationPoint,
                cube.rotation,
                new Vector3f(cube.dimensions),
                cube.children.stream().map(Part::new).collect(Collectors.toList())
            );
            this.name = cube.name;
        }
    }

    public static class Loader {

        private static ArrayList<Cube> readCubes(DataInputStream data, DCModel.Info model) throws IOException {
            ArrayList<Cube> cubes = new ArrayList<>();
            int amount = (int) data.readFloat();
            for (int i = 0; i < amount; i++) {
                cubes.add(new Cube(
                        data.readUTF(),
                        new Vector3i((int) data.readFloat(), (int) data.readFloat(), (int) data.readFloat()), //Dimensions
                        new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation Point
                        new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Offset
                        new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Rotation
                        new Vector2i((int) data.readFloat(), (int) data.readFloat()), //Texture Offset
                        data.readBoolean(), //Texture Mirrored
                        new Vector3f(data.readFloat(), data.readFloat(), data.readFloat()), //Cube Grow
                        readCubes(data, model), //Children
                        model));
                //Create the array for this child's location
            }
            return cubes;
        }

        public static DCModel load(ResourceManager resourceManager, Identifier modelIdentifier, Identifier textureIdentifier) {
            Optional<DataInputStream> optData = resourceManager.getResource(modelIdentifier).flatMap(Resource::asDataStream);
            DCModel.Info model = new DCModel.Info(resourceManager, textureIdentifier);
            if (optData.isPresent()) {
                try {
                    DataInputStream data = optData.get();
                    model.version = data.readFloat();
                    model.author = data.readUTF();
                    model.textureWidth = (int) data.readFloat();
                    model.textureHeight = (int) data.readFloat();
                    model.children = readCubes(data, model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.error("No resource found for model " + modelIdentifier.toString());
            }
            return new DCModel(model);
        }
    }

    public static class Cube {

        public String name;
        public Vector3i dimensions;
        public Vector3f rotationPoint;
        public Vector3f offset;
        public Vector3f rotation;
        public Vector2i textureOffset;
        public boolean textureMirrored;
        public Vector3f scale;
        public ArrayList<Cube> children;
        public DCModel.Info model;
        public float[][] uvs;

        public Cube(String name, Vector3i dimensions, Vector3f rotationPoint, Vector3f offset, Vector3f rotation,
                    Vector2i textureOffset, boolean textureMirrored, Vector3f scale, ArrayList<Cube> children,
                    DCModel.Info model) {
            this.name = name;
            this.dimensions = dimensions;
            this.rotationPoint = rotationPoint;
            this.offset = offset;
            this.rotation = rotation;
            this.textureOffset = textureOffset;
            this.textureMirrored = textureMirrored;
            this.scale = scale;
            this.children = children;
            this.model = model;
            this.uvs = setUVs();
        }

        private float[][] setUVs() {

            float[][] pixelUvs = new float[6][4];
            //South
            pixelUvs[0][0] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
            pixelUvs[0][1] = textureOffset.y + dimensions.z;
            pixelUvs[0][2] = textureOffset.x + (2 * dimensions.z) + (2 * dimensions.x);
            pixelUvs[0][3] = textureOffset.y + dimensions.z + dimensions.y;
            //East
            pixelUvs[1][0] = textureOffset.x + dimensions.z + dimensions.x;
            pixelUvs[1][1] = textureOffset.y + dimensions.z;
            pixelUvs[1][2] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
            pixelUvs[1][3] = textureOffset.y + dimensions.z + dimensions.y;
            //North
            pixelUvs[2][0] = textureOffset.x + dimensions.z;
            pixelUvs[2][1] = textureOffset.y + dimensions.z;
            pixelUvs[2][2] = textureOffset.x + dimensions.z + dimensions.x;
            pixelUvs[2][3] = textureOffset.y + dimensions.z + dimensions.y;
            //West
            pixelUvs[3][0] = textureOffset.x;
            pixelUvs[3][1] = textureOffset.y + dimensions.z;
            pixelUvs[3][2] = textureOffset.x + dimensions.z;
            pixelUvs[3][3] = textureOffset.y + dimensions.z + dimensions.y;
            //Top
            pixelUvs[4][0] = textureOffset.x + dimensions.z;
            pixelUvs[4][1] = textureOffset.y;
            pixelUvs[4][2] = textureOffset.x + dimensions.z + dimensions.x;
            pixelUvs[4][3] = textureOffset.y + dimensions.z;
            //Bottom
            pixelUvs[5][0] = textureOffset.x + dimensions.z + dimensions.x;
            pixelUvs[5][1] = textureOffset.y;
            pixelUvs[5][2] = textureOffset.x + (2 * dimensions.z) + dimensions.x;
            pixelUvs[5][3] = textureOffset.y + dimensions.z;

            //clamp the values from 0 to 1
            for (int i = 0; i < pixelUvs.length; i++) {
                pixelUvs[i][0] /= model.textureWidth;
                pixelUvs[i][1] /= model.textureHeight;
                pixelUvs[i][2] /= model.textureWidth;
                pixelUvs[i][3] /= model.textureHeight;
            }
            //Account for rounding errors in the clamping
            for (int i = 0; i < pixelUvs.length; i++) {
                pixelUvs[i][0] += 0.01 / model.textureWidth;
                pixelUvs[i][1] += 0.01 / model.textureHeight;
                pixelUvs[i][2] -= 0.01 / model.textureWidth;
                pixelUvs[i][3] -= 0.01 / model.textureHeight;
            }

            return pixelUvs;
        }
    }

    public static class Info {

        protected float version;
        protected String author;
        protected int textureWidth;
        protected int textureHeight;
        protected ArrayList<Cube> children;

        protected ResourceManager resourceManager;
        protected Identifier textureIdentifier;

        private Info(ResourceManager resourceManager, Identifier identifier) {
            this.textureIdentifier = identifier;
            this.resourceManager = resourceManager;
        }

        public List<Cube> getChildren() {
            return children;
        }

        private String printCubes(Cube cube) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Cube cubeChild : cube.children) {
                stringBuilder.append("\t");
                stringBuilder.append(cubeChild.toString());
                stringBuilder.append("\n\t").append(printCubes(cubeChild));
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Cube cube : children) {
                stringBuilder.append("\n").append(cube.toString());
                stringBuilder.append(printCubes(cube));
            }
            return "DCMModel{" +
                    "version=" + version +
                    ", author='" + author + '\'' +
                    ", textureWidth=" + textureWidth +
                    ", textureHeight=" + textureHeight +
                    ", children=" + stringBuilder.toString() +
                    '}';
        }
    }
}
