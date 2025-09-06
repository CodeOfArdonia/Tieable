package com.iafenvoy.tieable.render;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.render.model.CubeModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final CubeModel model = new CubeModel(CubeModel.getTexturedModelData().createModel());

    private static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState from, BlockState to) {
        return to.withIfExists(property, from.get(property));
    }

    @Override
    public void render(TiedBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        BlockState state = entity.getCachedState();
        this.render(state.getProperties().stream().reduce(entity.getStoredBlock().getDefaultState(), (p, c) -> copyProperty(c, state, p), (a, b) -> a), entity.getPos(), matrices, vertexConsumers, light, overlay);
        matrices.pop();
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (this.client.cameraEntity != null)
            this.render(TiedBlockItem.readStoredBlock(stack).storedBlock().getDefaultState(), this.client.cameraEntity.getBlockPos(), matrices, vertexConsumers, light, overlay);
    }

    private void render(BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        this.renderBlock(matrices, vertexConsumers, light, overlay);
        matrices.scale(0.5F, 0.5F, 0.5F);
        for (Vec3d offset : OFFSETS)
            this.renderSingle(state, pos, offset, matrices, vertexConsumers);
        matrices.pop();
    }

    private void renderBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(BASE_TEXTURE)), light, overlay, 1, 1, 1, 1);
        matrices.pop();
    }

    private void renderSingle(BlockState state, BlockPos pos, Vec3d offset, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        matrices.push();
        float f = 0.0005F;
        matrices.translate(offset.x + f, offset.y + f, offset.z + f);
        matrices.scale(1 - f * 2, 1 - f * 2, 1 - f * 2);
        this.client.getBlockRenderManager().renderBlockAsEntity(state, matrices, vertexConsumers, 0xFFFFFF, OverlayTexture.DEFAULT_UV);
        if (state.getBlock() instanceof BlockEntityProvider provider) {
            BlockEntity blockEntity = provider.createBlockEntity(pos, state);
            if (blockEntity != null) {
                blockEntity.setWorld(this.client.world);
                this.client.getBlockEntityRenderDispatcher().render(blockEntity, 1, matrices, vertexConsumers);
            }
        }
        matrices.pop();
    }
}
