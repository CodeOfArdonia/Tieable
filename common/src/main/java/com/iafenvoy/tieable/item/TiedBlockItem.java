package com.iafenvoy.tieable.item;

import com.iafenvoy.tieable.config.TieableConfig;
import com.iafenvoy.tieable.item.component.TieComponent;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.TieableDataComponents;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;

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
        if (TieableConfig.INSTANCE.shearsUntieOnItems && clickType == ClickType.RIGHT && slot.canTakePartial(player) && otherStack.isIn(TieableItemTags.CUT_ROPE)) {
            otherStack.damage(1, player, EquipmentSlot.MAINHAND);
            ItemStack split = stack.split(1);
            TieComponent component = readStoredBlock(split);
            player.getInventory().offerOrDrop(new ItemStack(component.storedBlock(), 8));
            player.getInventory().offerOrDrop(new ItemStack(component.rope()));
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
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
        stack.set(TieableDataComponents.TIE.get(), data);
        return stack;
    }

    public static TieComponent readStoredBlock(ItemStack stack) {
        TieComponent component = stack.get(TieableDataComponents.TIE.get());
        return component != null ? component : new TieComponent(Blocks.AIR, Items.AIR);
    }
}
