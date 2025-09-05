package com.iafenvoy.tieable.item;

import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.registry.TieableBlockEntities;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class TiedBlockItem extends BlockItem {
    public TiedBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player) && otherStack.isIn(TieableItemTags.CUT_ROPE)) {
            otherStack.damage(1, player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
            ItemStack split = stack.split(1);
            player.getInventory().offerOrDrop(new ItemStack(readStoredBlock(split).asItem(), 8));
            player.getInventory().offerOrDrop(new ItemStack(Items.LEAD));
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        Block block = readStoredBlock(stack);
        tooltip.add(Text.translatable("item.tieable.tied.tooltip", Text.translatable(block.getTranslationKey())));
    }

    public static ItemStack writeBlockToNbt(ItemStack stack, Block storedBlock) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(TiedBlockEntity.STORED_BLOCK_KEY, Registries.BLOCK.getId(storedBlock).toString());
        setBlockEntityNbt(stack, TieableBlockEntities.TIED.get(), nbt);
        return stack;
    }

    public static Block readStoredBlock(ItemStack stack) {
        NbtCompound nbt = getBlockEntityNbt(stack);
        if (nbt == null) return Blocks.AIR;
        return Registries.BLOCK.get(Identifier.tryParse(nbt.getString(TiedBlockEntity.STORED_BLOCK_KEY)));
    }
}
