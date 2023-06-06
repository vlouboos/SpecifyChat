package net.kinomc.specifychat;

import net.kinomc.specifychat.command.ChatCommand;
import net.kinomc.specifychat.command.PluginCommand;
import net.kinomc.specifychat.listener.CommandListener;
import net.kinomc.specifychat.listener.PlayerListener;
import net.kinomc.specifychat.storage.Storage;
import net.kinomc.specifychat.util.ConfigUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.List;
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
        getProxy().getPluginManager().registerListener(this, new CommandListener());
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
    }

    public void reloadConfig(@Nullable CommandSender sender, boolean reload) {
        if (reload) {
            getProxy().getPluginManager().unregisterCommands(this);
        }
        Storage.clearChannels();
        getProxy().getPluginManager().registerCommand(this, new PluginCommand());
        getProxy().getPluginManager().registerCommand(this, new ChatCommand());
        Configuration config = ConfigUtil.getConfig("channels.yml");
        for (String key : config.getKeys()) {
            Configuration channel = config.getSection(key);

            AtomicBoolean illegal = new AtomicBoolean(false);
            // Commands filter
            List<String> commands = channel.getStringList("commands");
            if (commands.isEmpty()) {
                getLogger().severe(ChatColor.DARK_RED + "No command specified, skipping channel: " + ChatColor.YELLOW + key);
                continue;
            }
            for (String command : commands) {
                if (getProxy().getPluginManager().isExecutableCommand(command, getProxy().getConsole())) {
                    getLogger().warning(ChatColor.YELLOW + "[" + key + "]" + ChatColor.GOLD + ": Conflicted command specified: " + ChatColor.YELLOW + command);
                    if (sender instanceof ProxiedPlayer) {
                        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "[" + key + "]" + ChatColor.GOLD + ": Conflicted command specified: " + ChatColor.YELLOW + command));
                    }
                    illegal.set(true);
                    break;
                }
            }
            if (illegal.get()) {
                getLogger().severe(ChatColor.DARK_RED + "Conflicted command detected, skipping channel: " + ChatColor.YELLOW + key);
                if (sender instanceof ProxiedPlayer) {
                    sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Conflicted command detected, skipping channel: " + ChatColor.YELLOW + key));
                }
                continue;
            }
            // Aliases filter
            illegal.set(false);
            List<String> aliases = channel.getStringList("aliases");
            if (!aliases.isEmpty()) {
                for (String alias : aliases) {
                    if (alias.equalsIgnoreCase("a") || alias.equalsIgnoreCase("all") || alias.equalsIgnoreCase("general")) {
                        getLogger().warning(ChatColor.YELLOW + "[" + key + "]" + ChatColor.GOLD + ": Conflicted alias specified: " + ChatColor.YELLOW + alias);
                        if (sender instanceof ProxiedPlayer) {
                            sender.sendMessage(new TextComponent(ChatColor.YELLOW + "[" + key + "]" + ChatColor.GOLD + ": Conflicted alias specified: " + ChatColor.YELLOW + alias));
                        }
                        illegal.set(true);
                        break;
                    }
                }
            }
            if (illegal.get()) {
                getLogger().severe(ChatColor.DARK_RED + "Conflicted alias detected, skipping channel: " + ChatColor.YELLOW + key);
                if (sender instanceof ProxiedPlayer) {
                    sender.sendMessage(new TextComponent(ChatColor.DARK_RED + "Conflicted alias detected, skipping channel: " + ChatColor.YELLOW + key));
                }
                continue;
            }
            // Add chat channel
            Channel channelInstance = new Channel(key, channel.getString("name"), channel.getString("format"), channel.getBoolean("enableChatColor"), commands, channel.getStringList("permissions"), aliases);
            Storage.addChannels(channelInstance);
            channelInstance.registerCommand();
            getLogger().info(ChatColor.GREEN + "Loaded channel: " + ChatColor.YELLOW + key);
            if (sender instanceof ProxiedPlayer) {
                sender.sendMessage(new TextComponent(ChatColor.GREEN + "Loaded channel: " + ChatColor.YELLOW + key));
            }
        }
        // Support for LuckPerms
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
                sender.sendMessage(new TextComponent(ChatColor.GOLD + "[Warning] LuckPerms not found!"));
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
