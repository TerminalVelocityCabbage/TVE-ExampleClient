package com.terminalvelocitycabbage.exampleclient.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedCuboid;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.exampleclient.GameClient;
import com.terminalvelocitycabbage.exampleclient.models.loader.DCModelInfo;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.terminalvelocitycabbage.exampleclient.GameResourceHandler.ASSETS_ROOT_RESOURCE_MANAGER;

public class DCModel extends Model {

    private final Map<String, Part> partMap = new HashMap<>();

    public DCModel(DCModelInfo model) {
        super(model.getChildren().stream().map(DcModelPart::new).collect(Collectors.toList()));

        recursiveOnPart(modelParts, p -> partMap.put(p.name, p));
    }

    private void recursiveOnPart(List<Part> modelParts, Consumer<DcModelPart> consumer) {
        for (Part part : modelParts) {
            consumer.accept((DcModelPart) part);
            recursiveOnPart(part.children, consumer);
        }
    }

    public Optional<Part> getPart(String name) {
        return this.partMap.containsKey(name) ? Optional.of(this.partMap.get(name)) : Optional.empty();
    }


    private static class DcModelPart extends Part {

        String name;

        public DcModelPart(DCModelInfo.DCMCube cube) {
            super(
                new TexturedCuboid(
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][0], cube.uvs[0][1]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][0], cube.uvs[0][3]),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][2], cube.uvs[0][3]),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[0][2], cube.uvs[0][1]),

                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][0], cube.uvs[1][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][0], cube.uvs[1][3]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][2], cube.uvs[1][3]),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[1][2], cube.uvs[1][1]),

                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][0], cube.uvs[2][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][0], cube.uvs[2][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][2], cube.uvs[2][3]),
                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[2][2], cube.uvs[2][1]),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][0], cube.uvs[3][1]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][0], cube.uvs[3][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][2], cube.uvs[3][3]),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[3][2], cube.uvs[3][1]),

                    new Vertex().setXYZ(0.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][0], cube.uvs[4][1]),
                    new Vertex().setXYZ(0.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][0], cube.uvs[4][3]),
                    new Vertex().setXYZ(1.0f, 1.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][2], cube.uvs[4][3]),
                    new Vertex().setXYZ(1.0f, 1.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[4][2], cube.uvs[4][1]),

                    new Vertex().setXYZ(1.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][0], cube.uvs[5][1]),
                    new Vertex().setXYZ(1.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][0], cube.uvs[5][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 0.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][2], cube.uvs[5][3]),
                    new Vertex().setXYZ(0.0f, 0.0f, 1.0f).setRGB(255, 255, 255).setUv(cube.uvs[5][2], cube.uvs[5][1]),

                    ASSETS_ROOT_RESOURCE_MANAGER,
                    new Identifier(GameClient.ID, "textures/gerald_base.png")
                ),
                cube.offset,
                cube.rotationPoint,
                cube.rotation,
                new Vector3f(cube.dimensions),
                cube.children.stream().map(DcModelPart::new).collect(Collectors.toList())
            );
            this.name = cube.name;
        }
    }
}
