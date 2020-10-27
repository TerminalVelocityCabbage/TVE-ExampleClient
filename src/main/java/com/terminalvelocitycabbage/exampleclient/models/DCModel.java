package com.terminalvelocitycabbage.exampleclient.models;

import com.dumbcodemc.studio.ModelLoader;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.TexturedMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.dumbcodemc.studio.ModelCube;
import com.dumbcodemc.studio.ModelInfo;
import org.joml.Vector3f;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DCModel extends Model {

    private final Map<String, Model.Part> partMap = new HashMap<>();

    private DCModel(ModelInfo model) {
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

    public void setTexture(ResourceManager resourceManager, Identifier texture) {
        recursiveOnPart(modelParts, part -> {
            ((TexturedMesh) part.mesh).setTexture(resourceManager, texture);
        });
    }

    private static class Part extends Model.Part {

        String name;

        public Part(ModelCube cube) {
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
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][2], cube.uvs[4][1])
                ),
                new Vector3f(cube.offset),
                new Vector3f(cube.rotationPoint),
                new Vector3f(cube.rotation),
                new Vector3f(cube.dimensions[0], cube.dimensions[1], cube.dimensions[2]),
                cube.children.stream().map(Part::new).collect(Collectors.toList())
            );
            this.name = cube.name;
        }
    }

    public static DCModel load(ResourceManager resourceManager, Identifier model) {
        Optional<Resource> resource = resourceManager.getResource(model);
        if (resource.isPresent()) {
            Optional<DataInputStream> dataInputStream = resource.get().asDataStream();
            if (dataInputStream.isPresent()) {
                return new DCModel(ModelLoader.load(dataInputStream.get()));
            } else {
                throw new RuntimeException("Could not get data stream from resource: " + model.toString());
            }
        } else {
            throw new RuntimeException("No resource found for identifier: " + model.toString());
        }
    }
}
