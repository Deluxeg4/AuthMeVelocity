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

package io.github._4drian3d.authmevelocity.api.velocity.placeholder;

import com.velocitypowered.api.proxy.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Manages placeholder expansions
 */
public interface PlaceholderManager {

    /**
     * Registers a new expansion
     * @param expansion the expansion to register
     */
    void registerExpansion(@NotNull PlaceholderExpansion expansion);

    /**
     * Unregisters an expansion
     * @param expansion the expansion to unregister
     */
    void unregisterExpansion(@NotNull PlaceholderExpansion expansion);

    /**
     * Gets all registered expansions
     * @return the expansions
     */
    @NotNull Collection<PlaceholderExpansion> getExpansions();

    /**
     * Replaces placeholders in a string
     * @param player the player
     * @param text the text
     * @return the text with placeholders replaced
     */
    @NotNull String setPlaceholders(@Nullable Player player, @NotNull String text);
}
