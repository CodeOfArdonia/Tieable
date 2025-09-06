package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.render.DynamicItemRenderer;
import com.iafenvoy.tieable.render.TiedBlockRenderer;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.render.RenderLayer;

import java.util.function.Supplier;

public final class TieableRenderers {
    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), TieableBlocks.TIED.get());
    }

    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(TieableBlockEntities.TIED.get(), ctx -> new TiedBlockRenderer());
    }

    public static void registerItemRenderers() {
        TieableBlocks.TIED_BLOCKS.stream().map(Supplier::get).forEach(b -> DynamicItemRenderer.RENDERERS.put(b.asItem(), new TiedBlockRenderer()));
    }
}
