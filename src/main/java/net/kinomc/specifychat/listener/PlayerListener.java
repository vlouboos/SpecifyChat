package net.kinomc.specifychat.listener;

import net.kinomc.specifychat.Channel;
import net.kinomc.specifychat.storage.Storage;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerChat(ChatEvent e) {
        if (e.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) e.getSender();
            Channel channel = Storage.getChannelByUUID(player.getUniqueId());
            if (channel != null) {
                // Every command should not be empty
                e.setMessage("/" + channel.getCommands().get(0) + " " + e.getMessage());
            }
        }
    }
}
