package com.terminalvelocitycabbage.exampleclient.models;

import com.dumbcodemc.studio.animation.instance.AnimatedCube;
import com.dumbcodemc.studio.animation.instance.ModelAnimationHandler;
import com.dumbcodemc.studio.model.CubeInfo;
import com.dumbcodemc.studio.model.ModelInfo;
import com.dumbcodemc.studio.model.ModelLoader;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DCModel extends Model {

    public final ModelAnimationHandler handler;

    private DCModel(ModelInfo model) {
        super(model.getRoots().stream().map(Part::createPart).collect(Collectors.toList()));

        List<Part> partMap = new ArrayList<>();
        recursiveOnPart(modelParts, partMap::add);
        this.handler = new ModelAnimationHandler(partMap);
    }

    private void recursiveOnPart(List<Model.Part> modelParts, Consumer<Part> consumer) {
        for (Model.Part part : modelParts) {
            consumer.accept((Part) part);
            recursiveOnPart(part.children, consumer);
        }
    }

    public static class Part extends Model.Part implements AnimatedCube {

        CubeInfo cube;

        public Part(CubeInfo cube, float[][] uv) {
            super(
                new TexturedCuboid(
                    //1, 3, 4, 5, 0, 2
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setUv(uv[4][2], uv[4][3]).setNormal(0, 0, 1),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setUv(uv[4][2], uv[4][1]).setNormal(0, 0, 1),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setUv(uv[4][0], uv[4][1]).setNormal(0, 0, 1),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setUv(uv[4][0], uv[4][3]).setNormal(0, 0, 1),

                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setUv(uv[0][2], uv[0][3]).setNormal(1, 0, 0),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setUv(uv[0][2], uv[0][1]).setNormal(1, 0, 0),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setUv(uv[0][0], uv[0][1]).setNormal(1, 0, 0),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setUv(uv[0][0], uv[0][3]).setNormal(1, 0, 0),

                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setUv(uv[5][2], uv[5][3]).setNormal(0, 0, -1),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setUv(uv[5][2], uv[5][1]).setNormal(0, 0, -1),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setUv(uv[5][0], uv[5][1]).setNormal(0, 0, -1),
                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setUv(uv[5][0], uv[5][3]).setNormal(0, 0, -1),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setUv(uv[1][2], uv[1][3]).setNormal(-1, 0, 0),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setUv(uv[1][2], uv[1][1]).setNormal(-1, 0, 0),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setUv(uv[1][0], uv[1][1]).setNormal(-1, 0, 0),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setUv(uv[1][0], uv[1][3]).setNormal(-1, 0, 0),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setUv(uv[3][2], uv[3][3]).setNormal(0, 1, 0),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setUv(uv[3][2], uv[3][1]).setNormal(0, 1, 0),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setUv(uv[3][0], uv[3][1]).setNormal(0, 1, 0),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setUv(uv[3][0], uv[3][3]).setNormal(0, 1, 0),

                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setUv(uv[2][0], uv[2][1]).setNormal(0, -1, 0),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setUv(uv[2][0], uv[2][3]).setNormal(0, -1, 0),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setUv(uv[2][2], uv[2][3]).setNormal(0, -1, 0),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setUv(uv[2][2], uv[2][1]).setNormal(0, -1, 0)
                ),
                new Vector3f(cube.getOffset()[0] - cube.getCubeGrow()[0], cube.getOffset()[1] - cube.getCubeGrow()[1], cube.getOffset()[2] - cube.getCubeGrow()[2]),
                new Vector3f(cube.getRotationPoint()),
                new Vector3f(cube.getRotation()),
                new Vector3f(cube.getDimensions()[0] + (2*cube.getCubeGrow()[0]), cube.getDimensions()[1] + (2*cube.getCubeGrow()[1]), cube.getDimensions()[2] + (2*cube.getCubeGrow()[2])),
                cube.getChildren().stream().map(Part::createPart).collect(Collectors.toList())
            );
            this.cube = cube;
        }

        public static Part createPart(CubeInfo info) {
            float[][] uv = info.getGeneratedUVs();
            return new Part(info, uv);
        }

        @Override
        public CubeInfo getInfo() {
            return this.cube;
        }

        @Override
        public void setRotation(float x, float y, float z) {
            this.rotation.set(x, y, z);
        }

        @Override
        public void setPosition(float x, float y, float z) {
            this.position.set(x, y, z);
        }
    }

    public static DCModel load(ResourceManager resourceManager, Identifier model) {
        return resourceManager.getResource(model)
            .map(resource -> {
                try {
                    return resource.openStream();
                } catch (IOException e) {
                    throw new RuntimeException("No resource found for identifier: " + model.toString(), e);
                }
            })
            .map(stream -> {
                try {
                    return ModelLoader.loadModel(stream);
                } catch (IOException e) {
                    throw new RuntimeException("Could not read model: " + model.toString(), e);
                }
            })
            .map(DCModel::new)
            .orElseThrow();
    }
}
