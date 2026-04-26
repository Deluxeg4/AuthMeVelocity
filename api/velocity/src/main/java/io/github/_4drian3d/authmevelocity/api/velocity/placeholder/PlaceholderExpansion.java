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

/**
 * Represents a placeholder expansion
 */
public abstract class PlaceholderExpansion {

    /**
     * The identifier of this expansion
     * @return the identifier
     */
    public abstract @NotNull String getIdentifier();

    /**
     * The author of this expansion
     * @return the author
     */
    public abstract @NotNull String getAuthor();

    /**
     * The version of this expansion
     * @return the version
     */
    public abstract @NotNull String getVersion();

    /**
     * On request of a placeholder
     * @param player the player
     * @param params the parameters (without identifier)
     * @return the value or null if not found
     */
    public abstract @Nullable String onPlaceholderRequest(@Nullable Player player, @NotNull String params);
}
