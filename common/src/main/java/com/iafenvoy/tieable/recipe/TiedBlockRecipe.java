package com.iafenvoy.tieable.recipe;

import com.google.gson.JsonObject;
import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TiedBlockRecipe implements CraftingRecipe {
    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack stack = inventory.getStack(0);
        if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++) {
                    ItemStack inInv = inventory.getStack(i * 3 + j);
                    if (i == 1 && j == 1) {
                        if (!inInv.isIn(TieableItemTags.ROPE)) return false;
                    } else if (!inInv.isOf(stack.getItem())) return false;
                }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack stack = inventory.getStack(0);
        if (stack.getItem() instanceof BlockItem blockItem)
            return TiedBlockItem.writeBlockToNbt(new ItemStack(selectBlock(blockItem.getBlock())), blockItem.getBlock());
        return ItemStack.EMPTY;
    }

    private static Block selectBlock(Block original) {
        BlockState state = original.getDefaultState();
        if (state.contains(Properties.AXIS)) return TieableBlocks.TIED_PILLAR.get();
        return TieableBlocks.TIED.get();
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Deprecated
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return new ItemStack(TieableBlocks.TIED.get());
    }

    @Override
    public Identifier getId() {
        return Identifier.of(Tieable.MOD_ID, "tied_block");
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
        public TiedBlockRecipe read(Identifier id, JsonObject json) {
            return new TiedBlockRecipe();
        }

        @Override
        public TiedBlockRecipe read(Identifier id, PacketByteBuf buf) {
            return new TiedBlockRecipe();
        }

        @Override
        public void write(PacketByteBuf buf, TiedBlockRecipe recipe) {
        }
    }
}
