package com.iafenvoy.tieable.fabric;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.TieableCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class TieableFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Tieable.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, access, env) -> TieableCommand.register(dispatcher, access));
    }
}
