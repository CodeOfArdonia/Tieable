package com.iafenvoy.tieable.render;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.render.model.CubeModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class TiedBlockRenderer implements BlockEntityRenderer<TiedBlockEntity>, DynamicItemRenderer {
    private static final Identifier BASE_TEXTURE = Identifier.of(Tieable.MOD_ID, "textures/entity/tied.png");
    private static final Vec3d[] OFFSETS = new Vec3d[]{
            new Vec3d(0, 0, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 1, 0),
            new Vec3d(1, 1, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(1, 0, 1),
            new Vec3d(0, 1, 1),
            new Vec3d(1, 1, 1),
    };
    private final CubeModel model = new CubeModel(CubeModel.getTexturedModelData().createModel());

    @Override
    public void render(TiedBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.contains(Properties.AXIS)) {
            Direction.Axis axis = state.get(Properties.AXIS);
            switch (axis) {
                case X -> {
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
                    matrices.translate(0, -1, 0);
                }
                case Z -> {
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                    matrices.translate(0, 0, -1);
                }
            }
        }
        this.render(entity.getStoredBlock(), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.render(TiedBlockItem.readStoredBlock(stack), matrices, vertexConsumers, light, overlay);
    }

    private void render(Block storedBlock, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        this.renderBlock(matrices, vertexConsumers, light, overlay);
        matrices.scale(2, 2, 2);
        matrices.translate(0.125, -0.0625, 0.125);
        ItemStack stack = new ItemStack(storedBlock);
        for (Vec3d offset : OFFSETS)
            this.renderSingle(stack, offset, matrices, vertexConsumers, MinecraftClient.getInstance().world);
        matrices.pop();
    }

    private void renderBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(BASE_TEXTURE)), light, overlay, 1, 1, 1, 1);
        matrices.pop();
    }

    private void renderSingle(ItemStack stack, Vec3d offset, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world) {
        matrices.push();
        matrices.translate(offset.x * 0.25, offset.y * 0.25, offset.z * 0.25);
        matrices.scale(0.999F, 0.999F, 0.999F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, 0xFFFFFF, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, world, 0);
        matrices.pop();
    }
}
