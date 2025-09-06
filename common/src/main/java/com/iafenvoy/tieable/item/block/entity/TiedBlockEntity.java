package com.iafenvoy.tieable.item.block.entity;

import com.iafenvoy.tieable.registry.TieableBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TiedBlockEntity extends BlockEntity {
    public static final String STORED_BLOCK_KEY = "stored_block";
    public static final String ROPE_KEY = "rope";
    private Block storedBlock = Blocks.AIR;
    private Item rope = Items.AIR;

    public TiedBlockEntity(BlockPos pos, BlockState state) {
        super(TieableBlockEntities.TIED.get(), pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.setStoredBlock(Registries.BLOCK.get(Identifier.tryParse(nbt.getString(STORED_BLOCK_KEY))));
        this.setRope(Registries.ITEM.get(Identifier.tryParse(nbt.getString(ROPE_KEY))));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString(STORED_BLOCK_KEY, Registries.BLOCK.getId(this.getStoredBlock()).toString());
        nbt.putString(ROPE_KEY, Registries.ITEM.getId(this.getRope()).toString());
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public Block getStoredBlock() {
        return this.storedBlock;
    }

    public void setStoredBlock(Block storedBlock) {
        this.storedBlock = storedBlock;
    }

    public Item getRope() {
        return this.rope;
    }

    public void setRope(Item rope) {
        this.rope = rope;
    }
}
