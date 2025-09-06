package com.iafenvoy.tieable;

import com.iafenvoy.tieable.item.TiedBlockItem;
import com.iafenvoy.tieable.item.component.TieComponent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class TieableCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(literal("tieable")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(argument("block", RegistryEntryArgumentType.registryEntry(access, RegistryKeys.BLOCK))
                        .executes(ctx -> {
                            ServerCommandSource source = ctx.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();
                            if (player.isCreative()) {
                                RegistryEntry<Block> block = RegistryEntryArgumentType.getRegistryEntry(ctx, "block", RegistryKeys.BLOCK);
                                player.getInventory().offerOrDrop(TiedBlockItem.createStack(new TieComponent(block.value(), Items.LEAD)));
                                return 1;
                            } else {
                                source.sendMessage(Text.translatable("command.tieable.need_creative"));
                                return 0;
                            }
                        })
                ));
    }
}
