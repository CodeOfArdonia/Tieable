package com.iafenvoy.tieable.neoforge;

import com.iafenvoy.tieable.TieableClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(Dist.CLIENT)
public class TieableNeoForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(TieableClient::process);
    }
}
