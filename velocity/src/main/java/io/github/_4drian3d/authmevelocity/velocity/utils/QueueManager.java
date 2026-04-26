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

package io.github._4drian3d.authmevelocity.velocity.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github._4drian3d.authmevelocity.api.velocity.event.ProxyLoginEvent;
import io.github._4drian3d.authmevelocity.common.configuration.ProxyConfiguration;
import io.github._4drian3d.authmevelocity.velocity.AuthMeVelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public final class QueueManager {
    private final List<UUID> priorityQueue = new LinkedList<>();
    private final List<UUID> regularQueue = new LinkedList<>();
    
    @Inject
    private AuthMeVelocityPlugin plugin;
    @Inject
    private ProxyServer proxy;

    public void start() {
        proxy.getScheduler().buildTask(plugin, this::processQueue)
                .repeat(1, TimeUnit.SECONDS)
                .schedule();

        proxy.getEventManager().register(plugin, this);
    }

    @Subscribe
    public void onLogin(ProxyLoginEvent event) {
        if (plugin.config().get().queue().enabled()) {
            Player player = event.player();
            if (isInQueue(player)) {
                sendQueueMessage(player, plugin.config().get().queue().positionMessage());
            }
        }
    }

    public boolean joinQueue(Player player) {
        if (isInQueue(player)) {
            return false;
        }
        if (player.hasPermission("authmevelocity.queue.priority")) {
            priorityQueue.add(player.getUniqueId());
        } else {
            regularQueue.add(player.getUniqueId());
        }
        sendQueueMessage(player, plugin.config().get().queue().joinMessage());
        return true;
    }

    public boolean leaveQueue(Player player) {
        boolean removed = priorityQueue.remove(player.getUniqueId()) || regularQueue.remove(player.getUniqueId());
        if (removed) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(plugin.config().get().queue().leaveMessage()));
        }
        return removed;
    }

    public void removePlayer(UUID uuid) {
        priorityQueue.remove(uuid);
        regularQueue.remove(uuid);
    }

    public int getPosition(Player player) {
        int priorityPos = priorityQueue.indexOf(player.getUniqueId());
        if (priorityPos != -1) {
            return priorityPos + 1;
        }
        int regularPos = regularQueue.indexOf(player.getUniqueId());
        if (regularPos != -1) {
            return priorityQueue.size() + regularPos + 1;
        }
        return -1;
    }

    public boolean isInQueue(Player player) {
        return priorityQueue.contains(player.getUniqueId()) || regularQueue.contains(player.getUniqueId());
    }

    public int getPriorityQueueSize() {
        return priorityQueue.size();
    }

    public int getRegularQueueSize() {
        return regularQueue.size();
    }

    public int getTotalQueueSize() {
        return priorityQueue.size() + regularQueue.size();
    }

    public void sendQueueMessage(Player player, String message) {
        int pos = getPosition(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize(message, 
                Placeholder.unparsed("pos", String.valueOf(pos))));
    }

    private void processQueue() {
        ProxyConfiguration.Queue config = plugin.config().get().queue();
        if (!config.enabled()) return;

        proxy.getServer(config.targetServer()).ifPresent(target -> {
            UUID toSend = null;
            for (UUID uuid : priorityQueue) {
                if (isLogged(uuid)) {
                    toSend = uuid;
                    break;
                }
            }
            if (toSend == null) {
                for (UUID uuid : regularQueue) {
                    if (isLogged(uuid)) {
                        toSend = uuid;
                        break;
                    }
                }
            }

            if (toSend != null) {
                final UUID finalUuid = toSend;
                proxy.getPlayer(finalUuid).ifPresent(player -> {
                    player.createConnectionRequest(target).connect().thenAccept(result -> {
                        if (result.isSuccessful()) {
                            removePlayer(finalUuid);
                        }
                    });
                });
            }
        });
    }

    private boolean isLogged(UUID uuid) {
        return proxy.getPlayer(uuid).map(plugin::isLogged).orElse(false);
    }
}
