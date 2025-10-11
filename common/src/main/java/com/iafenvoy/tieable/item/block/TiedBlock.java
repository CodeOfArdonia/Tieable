package com.iafenvoy.tieable.item.block;

import com.iafenvoy.tieable.config.TieableConfig;
import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.item.component.TieComponent;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.TieableDataComponents;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TiedBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final MapCodec<TiedBlock> CODEC = createCodec(x -> new TiedBlock());

    public TiedBlock() {
        super(Settings.copy(Blocks.DIAMOND_BLOCK).breakInstantly());
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (TieableConfig.INSTANCE.shearsUntieOnBlocks && player.getStackInHand(hand).isIn(TieableItemTags.CUT_ROPE) && world.getBlockEntity(pos) instanceof TiedBlockEntity tied) {
            dropStack(world, pos, new ItemStack(tied.getStoredBlock().asItem(), 8));
            dropStack(world, pos, new ItemStack(Items.LEAD));
            if (!world.isClient) world.breakBlock(pos, false);
            return ItemActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockEntity(pos) instanceof TiedBlockEntity tied) {
            TieComponent component = TiedBlockItem.readStoredBlock(itemStack);
            tied.setStoredBlock(component.storedBlock());
            tied.setRope(component.rope());
        }
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (blockEntity instanceof TiedBlockEntity tied) {
            ItemStack stack = new ItemStack(TieableBlocks.TIED.get());
            tied.setStackNbt(stack, world.getRegistryManager());
            dropStack(world, pos, stack);
        }
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return createCuboidShape(0, 0, 0, 0, 0, 0);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        ItemStack stack = super.getPickStack(world, pos, state);
        if (world.getBlockEntity(pos) instanceof TiedBlockEntity tied) {
            stack.set(TieableDataComponents.TIE.get(), new TieComponent(tied.getStoredBlock(), tied.getRope()));
        }
        return stack;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TiedBlockEntity(pos, state);
    }

    @Override
    public boolean canFillWithFluid(PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return world.getBlockEntity(pos) instanceof TiedBlockEntity tied && tied.getStoredBlock().getDefaultState().contains(WATERLOGGED) && Waterloggable.super.canFillWithFluid(player, world, pos, state, fluid);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }
}
