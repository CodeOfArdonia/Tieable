package com.iafenvoy.tieable.recipe;

import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.component.TieComponent;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public enum TiedBlockRecipe implements CraftingRecipe {
    INSTANCE;

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        if (!this.fits(inventory.getWidth(), inventory.getHeight())) return false;
        ItemStack stack = inventory.getStackInSlot(0);
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++) {
                    ItemStack inInv = inventory.getStackInSlot(i * 3 + j);
                    if (i == 1 && j == 1) {
                        if (!inInv.isIn(TieableItemTags.ROPE)) return false;
                    } else if (!inInv.isOf(stack.getItem())) return false;
                }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup registryManager) {
        if (!this.fits(inventory.getWidth(), inventory.getHeight())) return ItemStack.EMPTY;
        ItemStack stored = inventory.getStackInSlot(0), rope = inventory.getStackInSlot(4);
        if (stored.getItem() instanceof BlockItem blockItem)
            return TiedBlockItem.createStack(new TieComponent(blockItem.getBlock(), rope.getItem()));
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Deprecated
    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registryManager) {
        return new ItemStack(TieableBlocks.TIED.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return CraftingRecipeCategory.BUILDING;
    }

    public enum Serializer implements RecipeSerializer<TiedBlockRecipe> {
        INSTANCE;

        @Override
        public MapCodec<TiedBlockRecipe> codec() {
            return MapCodec.unit(TiedBlockRecipe.INSTANCE);
        }

        @Override
        public PacketCodec<RegistryByteBuf, TiedBlockRecipe> packetCodec() {
            return PacketCodec.unit(TiedBlockRecipe.INSTANCE);
        }
    }
}
