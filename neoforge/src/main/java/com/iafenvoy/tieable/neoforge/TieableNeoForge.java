package com.iafenvoy.tieable.neoforge;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.TieableCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(Tieable.MOD_ID)
@EventBusSubscriber
public final class TieableNeoForge {
    public TieableNeoForge() {
        Tieable.init();
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        TieableCommand.register(event.getDispatcher(), event.getBuildContext());
    }
}
