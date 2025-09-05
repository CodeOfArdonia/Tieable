package com.iafenvoy.tieable;

import com.iafenvoy.tieable.registry.TieableBlockEntities;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.TieableItems;
import com.iafenvoy.tieable.registry.TieableRecipeSerializers;

public final class Tieable {
    public static final String MOD_ID = "tieable";

    public static void init() {
        TieableBlocks.REGISTRY.register();
        TieableBlockEntities.REGISTRY.register();
        TieableItems.REGISTRY.register();
        TieableRecipeSerializers.REGISTRY.register();
    }
}
