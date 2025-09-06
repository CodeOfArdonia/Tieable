package com.iafenvoy.tieable.forge;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.TieableCommand;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Tieable.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TieableForge {
    @SuppressWarnings("removal")
    public TieableForge() {
        EventBuses.registerModEventBus(Tieable.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Tieable.init();
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        TieableCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
