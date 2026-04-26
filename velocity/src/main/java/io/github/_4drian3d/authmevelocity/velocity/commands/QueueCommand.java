/*
 * Copyright (C) 2026 AuthMeVelocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github._4drian3d.authmevelocity.velocity.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.authmevelocity.velocity.AuthMeVelocityPlugin;
import io.github._4drian3d.authmevelocity.velocity.utils.QueueManager;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class QueueCommand {
    @Inject
    private AuthMeVelocityPlugin plugin;
    @Inject
    private CommandManager manager;
    @Inject
    private QueueManager queueManager;

    public void register() {
        registerJoin();
        registerQueue();
        registerLeave();
    }

    private void registerJoin() {
        final var command = LiteralArgumentBuilder.<CommandSource>literal("join")
                .executes(cmd -> {
                    if (!(cmd.getSource() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }
                    if (!plugin.config().get().queue().enabled()) {
                        return Command.SINGLE_SUCCESS;
                    }
                    queueManager.joinQueue(player);
                    return Command.SINGLE_SUCCESS;
                }).build();
        manager.register(new BrigadierCommand(command));
    }

    private void registerQueue() {
        final var command = LiteralArgumentBuilder.<CommandSource>literal("queue")
                .executes(cmd -> {
                    if (!(cmd.getSource() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }
                    if (!plugin.config().get().queue().enabled()) {
                        return Command.SINGLE_SUCCESS;
                    }
                    if (queueManager.isInQueue(player)) {
                        queueManager.sendQueueMessage(player, plugin.config().get().queue().positionMessage());
                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are not in the queue. Type /join to join."));
                    }
                    return Command.SINGLE_SUCCESS;
                }).build();
        manager.register(new BrigadierCommand(command));
    }

    private void registerLeave() {
        final var command = LiteralArgumentBuilder.<CommandSource>literal("leave")
                .executes(cmd -> {
                    if (!(cmd.getSource() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }
                    if (!plugin.config().get().queue().enabled()) {
                        return Command.SINGLE_SUCCESS;
                    }
                    queueManager.leaveQueue(player);
                    return Command.SINGLE_SUCCESS;
                }).build();
        manager.register(new BrigadierCommand(command));
    }
}
