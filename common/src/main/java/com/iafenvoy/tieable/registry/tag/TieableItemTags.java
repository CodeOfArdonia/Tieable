package com.iafenvoy.tieable.registry.tag;

import com.iafenvoy.tieable.Tieable;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class TieableItemTags {
    public static final TagKey<Item> CUT_ROPE = create("cut_rope");
    public static final TagKey<Item> ROPE = create("rope");

    public static TagKey<Item> create(String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(Tieable.MOD_ID, id));
    }
}
