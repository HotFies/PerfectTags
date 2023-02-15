package org.aneras.mc.perfecttags.PlaceHolders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.aneras.mc.perfecttags.PerfectTags;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegisterPlaceHolder extends PlaceholderExpansion {
    private final PerfectTags plugin;
    public RegisterPlaceHolder (PerfectTags plugin){
        this.plugin = plugin;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "PerfectTags";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    @Override
    public String onPlaceholderRequest(Player p, String param) {
        if (param.equalsIgnoreCase("Tag")) {
            return plugin.data.getTag(p.getUniqueId());
        }
        return null;
    }
}
