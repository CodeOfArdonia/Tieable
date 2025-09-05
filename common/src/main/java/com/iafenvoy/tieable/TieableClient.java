package com.iafenvoy.tieable;

import com.iafenvoy.tieable.registry.TieableRenderers;

public final class TieableClient {
    public static void process() {
        TieableRenderers.registerRenderLayers();
        TieableRenderers.registerBlockEntityRenderers();
        TieableRenderers.registerItemRenderers();
    }
}
