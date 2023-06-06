package net.kinomc.specifychat.storage;

import net.kinomc.specifychat.Channel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class Storage {
    private static ArrayList<Channel> channels;
    private static HashMap<UUID, Channel> players;

    public static void clearChannels() {
        channels = new ArrayList<>(0);
        players = new HashMap<>(0);
    }

    public static void addChannels(Channel... channels) {
        Storage.channels.addAll(Arrays.asList(channels));
    }

    public static Channel getChannelByName(String name) {
        for (Channel channel : channels) {
            if (channel.getKey().equalsIgnoreCase(name)) {
                return channel;
            }
            for (String alias : channel.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return channel;
                }
            }
        }
        return null;
    }

    public static Channel getChannelByUUID(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    public static void putChannel(ProxiedPlayer player, Channel channel) {
        players.put(player.getUniqueId(), channel);
    }

    public static List<String> getTabComplete(String name) {
        List<String> suggestion = new ArrayList<>();
        for (Channel channel : channels) {
            if (channel.getKey().toLowerCase().startsWith(name.toLowerCase())) {
                suggestion.add(name);
                continue;
            }
            for (String alias : channel.getAliases()) {
                if (alias.toLowerCase().startsWith(name.toLowerCase())) {
                    suggestion.add(alias);
                    break;
                }
            }
        }
        return suggestion;
    }
}
