package com.iafenvoy.tieable.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public record TieComponent(Block storedBlock, Item rope) {
    public static final Codec<TieComponent> CODEC = RecordCodecBuilder.create(i -> i.group(
            Registries.BLOCK.getCodec().fieldOf("storedBlock").forGetter(TieComponent::storedBlock),
            Registries.ITEM.getCodec().fieldOf("rope").forGetter(TieComponent::rope)
    ).apply(i, TieComponent::new));
}
