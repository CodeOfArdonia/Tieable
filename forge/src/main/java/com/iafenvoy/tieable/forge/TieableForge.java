package com.iafenvoy.tieable.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.iafenvoy.tieable.Tieable;

@Mod(Tieable.MOD_ID)
public final class TieableForge {
    @SuppressWarnings("removal")
    public TieableForge() {
        EventBuses.registerModEventBus(Tieable.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Tieable.init();
    }
}
