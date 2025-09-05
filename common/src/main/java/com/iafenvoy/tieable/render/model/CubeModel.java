package com.iafenvoy.tieable.render.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class CubeModel extends EntityModel<Entity> {
    private final ModelPart part;

    public CubeModel(ModelPart root) {
        this.part = root.getChild("main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("main", ModelPartBuilder.create().uv(0, 0)
                        .cuboid(0, 0, 0, 0, 16, 16, new Dilation(0))
                        .cuboid(16, 0, 0, 0, 16, 16, new Dilation(0))
                        .cuboid(0, 0, 0, 16, 0, 16, new Dilation(0))
                        .cuboid(0, 16, 0, 16, 0, 16, new Dilation(0))
                        .cuboid(0, 0, 0, 16, 16, 0, new Dilation(0))
                        .cuboid(0, 0, 16, 16, 16, 0, new Dilation(0)),
                ModelTransform.pivot(0, 0, 0));
        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.part.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}