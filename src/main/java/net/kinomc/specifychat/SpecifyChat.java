package net.kinomc.specifychat;

import net.kinomc.specifychat.command.PluginCommand;
import net.kinomc.specifychat.util.ConfigUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SpecifyChat extends Plugin {
    public static SpecifyChat instance;
    public SimpleDateFormat dateFormat;
    public boolean hasLuckPerms;

    @Override
    public void onLoad() {
        // Plugin load logic
        instance = this;
        dateFormat = new SimpleDateFormat("[HH:mm:ss]");
        hasLuckPerms = false;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        reloadConfig(null, false);
    }

    public void reloadConfig(@Nullable CommandSender sender, boolean reload) {
        if (reload) {
            getProxy().getPluginManager().unregisterCommands(this);
        }
        getProxy().getPluginManager().registerCommand(this, new PluginCommand());
        Configuration config = ConfigUtil.getConfig("config.yml");
        for (String key : config.getKeys()) {
            Configuration channel = config.getSection(key);
            AtomicBoolean illegal = new AtomicBoolean(false);
            channel.getStringList("commands").forEach(s -> {
                if (s.equalsIgnoreCase("specifyChat")) {
                    getLogger().warning(ChatColor.GOLD + "Illegal command specified: " + ChatColor.YELLOW + s);
                    if (sender instanceof ProxiedPlayer) {
                        sender.sendMessage(new TextComponent(ChatColor.GOLD + "Illegal command specified: " + ChatColor.YELLOW + s));
                    }
                    illegal.set(true);
                }
            });
            if (illegal.get()) {
                getLogger().severe(ChatColor.DARK_RED + "Illegal command detected, skipping channel: " + ChatColor.YELLOW + key);
                if (sender instanceof ProxiedPlayer) {
                    sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Illegal command detected, skipping channel: " + ChatColor.YELLOW + key));
                }
                continue;
            }
            Channel channelInstance = new Channel(channel.getString("name"), channel.getString("format"), channel.getBoolean("enableChatColor"), channel.getStringList("commands"), channel.getStringList("permissions"));
            channelInstance.registerCommand();
            getLogger().info(ChatColor.GREEN + "Loaded channel: " + ChatColor.YELLOW + key);
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(new TextComponent(ChatColor.GREEN + "Loaded channel: " + ChatColor.YELLOW + key));
            }
        }
        try {
            Class.forName("net.luckperms.api.LuckPerms");
            hasLuckPerms = true;
            getLogger().info(ChatColor.GREEN + "LuckPerms found! " + ChatColor.YELLOW + "Placeholders " + ChatColor.GREEN + "registered.");
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(new TextComponent(ChatColor.GREEN + "LuckPerms found! " + ChatColor.YELLOW + "Placeholders " + ChatColor.GREEN + "registered."));
            }
        } catch (ClassNotFoundException e) {
            getLogger().warning(ChatColor.GOLD + "LuckPerms not found!");
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(new TextComponent(ChatColor.GOLD + "LuckPerms not found!"));
            }
        }
        if (reload) {
            getLogger().info(ChatColor.GREEN + "Plugin reloaded.");
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(new TextComponent(ChatColor.GREEN + "Plugin reloaded."));
            }
        }
    }
}
