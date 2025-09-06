package com.iafenvoy.tieable.item;

import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.item.component.TieComponent;
import com.iafenvoy.tieable.registry.TieableBlockEntities;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiedBlockItem extends BlockItem {
    private static final Map<Property<?>, ItemConvertible> PROPERTY_BLOCK_MAP = new HashMap<>();

    public TiedBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player) && otherStack.isIn(TieableItemTags.CUT_ROPE)) {
            otherStack.damage(1, player, p -> p.sendToolBreakStatus(Hand.MAIN_HAND));
            ItemStack split = stack.split(1);
            TieComponent component = readStoredBlock(split);
            player.getInventory().offerOrDrop(new ItemStack(component.storedBlock(), 8));
            player.getInventory().offerOrDrop(new ItemStack(component.rope()));
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        TieComponent component = readStoredBlock(stack);
        tooltip.add(Text.translatable("item.tieable.tied.tooltip", Text.translatable(component.storedBlock().getTranslationKey())));
    }

    public static void register(Property<?> property, ItemConvertible item) {
        PROPERTY_BLOCK_MAP.put(property, item);
    }

    public static ItemStack createStack(TieComponent data) {
        BlockState state = data.storedBlock().getDefaultState();
        ItemConvertible target = PROPERTY_BLOCK_MAP.entrySet().stream().filter(p -> state.contains(p.getKey())).findFirst().map(Map.Entry::getValue).orElse(TieableBlocks.TIED.get());
        return writeDataToStack(new ItemStack(target), data);
    }

    public static ItemStack writeDataToStack(ItemStack stack, TieComponent data) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString(TiedBlockEntity.STORED_BLOCK_KEY, Registries.BLOCK.getId(data.storedBlock()).toString());
        nbt.putString(TiedBlockEntity.ROPE_KEY, Registries.ITEM.getId(data.rope()).toString());
        setBlockEntityNbt(stack, TieableBlockEntities.TIED.get(), nbt);
        return stack;
    }

    public static TieComponent readStoredBlock(ItemStack stack) {
        NbtCompound nbt = getBlockEntityNbt(stack);
        if (nbt == null) return new TieComponent(Blocks.AIR, Items.AIR);
        Block storedBlock = Registries.BLOCK.get(Identifier.tryParse(nbt.getString(TiedBlockEntity.STORED_BLOCK_KEY)));
        Item rope = Registries.ITEM.get(Identifier.tryParse(nbt.getString(TiedBlockEntity.ROPE_KEY)));
        return new TieComponent(storedBlock, rope);
    }
}
