package com.iafenvoy.tieable;

import com.iafenvoy.tieable.registry.*;

public final class Tieable {
    public static final String MOD_ID = "tieable";

    public static void init() {
        TieableBlocks.REGISTRY.register();
        TieableBlockEntities.REGISTRY.register();
        TieableDataComponents.REGISTRY.register();
        TieableItems.REGISTRY.register();
        TieableRecipeSerializers.REGISTRY.register();
    }
}
