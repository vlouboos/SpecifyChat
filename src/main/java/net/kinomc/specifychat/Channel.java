package net.kinomc.specifychat;

import net.kinomc.specifychat.command.CommandInstance;

import java.util.List;

public class Channel {
    private final String key;
    private final String name;
    private final String format;
    private final boolean enableChatColor;
    private final List<String> commands;
    private final List<String> permissions;
    private final List<String> aliases;

    public Channel(String key, String name, String format, boolean enableChatColor, List<String> commands, List<String> permissions, List<String> aliases) {
        this.key = key;
        this.name = name;
        this.format = format;
        this.enableChatColor = enableChatColor;
        this.commands = commands;
        this.permissions = permissions;
        this.aliases = aliases;
    }

    public void registerCommand() {
        int size = commands.size();
        if (size == 1) {
            SpecifyChat.instance.getProxy().getPluginManager().registerCommand(SpecifyChat.instance, new CommandInstance(commands.get(0), this));
        } else {
            SpecifyChat.instance.getProxy().getPluginManager().registerCommand(SpecifyChat.instance, new CommandInstance(commands.get(0), this, commands.subList(1, size).toArray(new String[0])));
        }
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public boolean enableChatColor() {
        return enableChatColor;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
