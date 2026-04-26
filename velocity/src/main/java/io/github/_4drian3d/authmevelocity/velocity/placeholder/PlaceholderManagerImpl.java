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

import com.google.inject.Singleton;
import com.velocitypowered.api.proxy.Player;
import io.github._4drian3d.authmevelocity.api.velocity.placeholder.PlaceholderExpansion;
import io.github._4drian3d.authmevelocity.api.velocity.placeholder.PlaceholderManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public final class PlaceholderManagerImpl implements PlaceholderManager {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([a-zA-Z0-9]+)_([a-zA-Z0-9_]+)%");
    private final Map<String, PlaceholderExpansion> expansions = new ConcurrentHashMap<>();

    @Override
    public void registerExpansion(@NotNull PlaceholderExpansion expansion) {
        expansions.put(expansion.getIdentifier().toLowerCase(), expansion);
    }

    @Override
    public void unregisterExpansion(@NotNull PlaceholderExpansion expansion) {
        expansions.remove(expansion.getIdentifier().toLowerCase());
    }

    @Override
    public @NotNull Collection<PlaceholderExpansion> getExpansions() {
        return expansions.values();
    }

    @Override
    public @NotNull String setPlaceholders(@Nullable Player player, @NotNull String text) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            builder.append(text, lastEnd, matcher.start());
            String identifier = matcher.group(1).toLowerCase();
            String params = matcher.group(2);
            
            PlaceholderExpansion expansion = expansions.get(identifier);
            if (expansion != null) {
                String result = expansion.onPlaceholderRequest(player, params);
                builder.append(result != null ? result : matcher.group(0));
            } else {
                builder.append(matcher.group(0));
            }
            lastEnd = matcher.end();
        }
        builder.append(text.substring(lastEnd));
        return builder.toString();
    }
}
