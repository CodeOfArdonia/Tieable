package com.iafenvoy.tieable.fabric;

import com.iafenvoy.tieable.Tieable;
import net.fabricmc.api.ModInitializer;

public final class TieableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Tieable.init();
    }
}
