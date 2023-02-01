package net.kinomc.specifychat.util;

import net.kinomc.specifychat.SpecifyChat;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerUtil {
    public static String getSenderName(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return getPlayerName((ProxiedPlayer) sender);
        }
        return "&fConsole";
    }

    private static String getPlayerName(ProxiedPlayer player) {
        if (SpecifyChat.instance.hasLuckPerms) {
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
            if (user == null) {
                return null;
            }
            CachedMetaData metaData = user.getCachedData().getMetaData();
            String prefix = metaData.getPrefix();
            String suffix = metaData.getSuffix();
            return (prefix == null ? "" : prefix) + player.getName() + (suffix == null ? "" : suffix);
        }
        return player.getName();
    }
}
