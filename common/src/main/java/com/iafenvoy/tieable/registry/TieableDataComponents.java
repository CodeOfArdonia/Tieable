package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.component.TieComponent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class TieableDataComponents {
    public static final DeferredRegister<ComponentType<?>> REGISTRY = DeferredRegister.create(Tieable.MOD_ID, RegistryKeys.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<ComponentType<TieComponent>> TIE = register("tie", () -> ComponentType.<TieComponent>builder().codec(TieComponent.CODEC).build());

    public static <T> RegistrySupplier<ComponentType<T>> register(String id, Supplier<ComponentType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
