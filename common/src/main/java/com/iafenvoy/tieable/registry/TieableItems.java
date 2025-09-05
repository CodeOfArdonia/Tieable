package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.Tieable;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class TieableItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Tieable.MOD_ID, RegistryKeys.ITEM);

    public static <T extends Item> RegistrySupplier<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
