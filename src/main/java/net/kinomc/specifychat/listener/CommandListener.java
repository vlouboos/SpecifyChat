package net.kinomc.specifychat.listener;

import net.kinomc.specifychat.storage.Storage;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class CommandListener implements Listener {
    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        if (e.getCursor().toLowerCase().startsWith("/chat ")) {
            String channelNameIn = e.getCursor().substring(5);
            List<String> suggestion = Storage.getTabComplete(channelNameIn);
            e.getSuggestions().addAll(suggestion);
        }
    }
}
