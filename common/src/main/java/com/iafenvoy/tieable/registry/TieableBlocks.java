package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.block.TiedBlock;
import com.iafenvoy.tieable.item.block.TiedPillarBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;
import java.util.function.Supplier;

public final class TieableBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Tieable.MOD_ID, RegistryKeys.BLOCK);

    public static final RegistrySupplier<Block> TIED = register("tied", TiedBlock::new, block -> new TiedBlockItem(block, new Item.Settings()));
    public static final RegistrySupplier<Block> TIED_PILLAR = register("tied_pillar", TiedPillarBlock::new, block -> new TiedBlockItem(block, new Item.Settings()));

    public static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> obj, Function<Block, Item> itemBuilder) {
        RegistrySupplier<T> r = REGISTRY.register(id, obj);
        TieableItems.register(id, () -> itemBuilder.apply(r.get()));
        return r;
    }
}
