package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class TieableBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Tieable.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<TiedBlockEntity>> TIED = register("tied", () -> BlockEntityType.Builder.create(TiedBlockEntity::new, TieableBlocks.TIED_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new)).build(null));

    public static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
