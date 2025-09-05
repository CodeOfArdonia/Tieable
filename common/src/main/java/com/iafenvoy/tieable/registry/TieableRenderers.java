package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.render.DynamicItemRenderer;
import com.iafenvoy.tieable.render.TiedBlockRenderer;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.render.RenderLayer;

public final class TieableRenderers {
    public static void registerRenderLayers() {
        RenderTypeRegistry.register(RenderLayer.getCutout(), TieableBlocks.TIED.get());
    }

    public static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(TieableBlockEntities.TIED.get(), ctx -> new TiedBlockRenderer());
    }

    public static void registerItemRenderers() {
        DynamicItemRenderer.RENDERERS.put(TieableBlocks.TIED.get().asItem(), new TiedBlockRenderer());
        DynamicItemRenderer.RENDERERS.put(TieableBlocks.TIED_PILLAR.get().asItem(), new TiedBlockRenderer());
    }
}
