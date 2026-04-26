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

package io.github._4drian3d.authmevelocity.velocity.placeholder;

import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.authmevelocity.api.velocity.placeholder.PlaceholderExpansion;
import io.github._4drian3d.authmevelocity.velocity.AuthMeVelocityPlugin;
import io.github._4drian3d.authmevelocity.velocity.utils.QueueManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AuthMeExpansion extends PlaceholderExpansion {
    private final AuthMeVelocityPlugin plugin;
    private final QueueManager queueManager;

    public AuthMeExpansion(AuthMeVelocityPlugin plugin, QueueManager queueManager) {
        this.plugin = plugin;
        this.queueManager = queueManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "authmevelocity";
    }

    @Override
    public @NotNull String getAuthor() {
        return "4drian3d";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
        if (player == null) return null;

        return switch (params.toLowerCase()) {
            case "is_logged" -> String.valueOf(plugin.isLogged(player));
            case "in_auth_server" -> String.valueOf(plugin.isInAuthServer(player));
            case "queue" -> String.valueOf(queueManager.getPosition(player));
            case "total_queue" -> String.valueOf(queueManager.getTotalQueueSize());
            case "priority_queue" -> String.valueOf(queueManager.getPriorityQueueSize());
            case "regular_queue" -> String.valueOf(queueManager.getRegularQueueSize());
            case "estimatedtime" -> "Not supported yet";
            default -> null;
        };
    }
}
