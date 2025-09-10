package com.iafenvoy.tieable.item.block;

import com.iafenvoy.tieable.item.block.entity.TiedBlockEntity;
import com.iafenvoy.tieable.registry.TieableBlocks;
import com.iafenvoy.tieable.registry.tag.TieableItemTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TiedBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).isIn(TieableItemTags.CUT_ROPE) && world.getBlockEntity(pos) instanceof TiedBlockEntity tied) {
            dropStack(world, pos, new ItemStack(tied.getStoredBlock().asItem(), 8));
            dropStack(world, pos, new ItemStack(Items.LEAD));
            if (!world.isClient) world.breakBlock(pos, false);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if (blockEntity instanceof TiedBlockEntity tied) {
            ItemStack stack = new ItemStack(TieableBlocks.TIED.get());
            tied.setStackNbt(stack);
            dropStack(world, pos, stack);
        }
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return createCuboidShape(0, 0, 0, 0, 0, 0);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack stack = super.getPickStack(world, pos, state);
        if (world.getBlockEntity(pos) instanceof TiedBlockEntity tied) tied.setStackNbt(stack);
        return stack;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TiedBlockEntity(pos, state);
    }

    @Override
    public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return world.getBlockEntity(pos) instanceof TiedBlockEntity tied && tied.getStoredBlock().getDefaultState().contains(WATERLOGGED) && Waterloggable.super.canFillWithFluid(world, pos, state, fluid);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
