package com.iafenvoy.tieable.fabric;

import com.iafenvoy.tieable.TieableClient;
import net.fabricmc.api.ClientModInitializer;

public final class TieableFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TieableClient.process();
    }
}
