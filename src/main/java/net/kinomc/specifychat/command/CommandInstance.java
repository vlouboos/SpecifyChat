package net.kinomc.specifychat.command;

import net.kinomc.specifychat.Channel;
import net.kinomc.specifychat.SpecifyChat;
import net.kinomc.specifychat.util.PlayerUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Date;

public class CommandInstance extends Command {
    private final Channel channel;

    public CommandInstance(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    public CommandInstance(String name, Channel channel, String[] aliases) {
        super(name, "", aliases);
        this.channel = channel;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean pass = sender instanceof ProxiedPlayer;
        if (!pass) {
            for (String permission : channel.getPermissions()) {
                if (sender.hasPermission(permission)) {
                    pass = true;
                    break;
                }
            }
        }
        if (!pass) {
            sender.sendMessage(new TextComponent(ChatColor.WHITE + "Unknown command. Type \"/help\" for help."));
        } else {
            if (args.length == 0) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /" + channel.getCommands().get(0) + " <Message>"));
            } else {
                StringBuilder sb = new StringBuilder();
                for (String argument : args) {
                    sb.append(" ").append(argument);
                }
                String message = channel.getFormat()
                        .replace("%name%", channel.getName())
                        .replace("%player%", PlayerUtil.getSenderName(sender))
                        .replace("%time%", SpecifyChat.instance.dateFormat.format(new Date()))
                        .replace("&r", "\247r")
                        .replace("&0", "\2470")
                        .replace("&1", "\2471")
                        .replace("&2", "\2472")
                        .replace("&3", "\2473")
                        .replace("&4", "\2474")
                        .replace("&5", "\2475")
                        .replace("&6", "\2476")
                        .replace("&7", "\2477")
                        .replace("&8", "\2478")
                        .replace("&9", "\2479")
                        .replace("&a", "\247a")
                        .replace("&b", "\247b")
                        .replace("&c", "\247c")
                        .replace("&d", "\247d")
                        .replace("&e", "\247e")
                        .replace("&f", "\247f")
                        .replace("&l", "\247l")
                        .replace("&n", "\247n")
                        .replace("&o", "\247o")
                        .replace("&k", "\247k")
                        .replace("&m", "\247m")
                        .replace("%message%", sb.substring(1));
                if (channel.enableChatColor()) {
                    message = message.replace("&r", "\247r")
                            .replace("&0", "\2470")
                            .replace("&1", "\2471")
                            .replace("&2", "\2472")
                            .replace("&3", "\2473")
                            .replace("&4", "\2474")
                            .replace("&5", "\2475")
                            .replace("&6", "\2476")
                            .replace("&7", "\2477")
                            .replace("&8", "\2478")
                            .replace("&9", "\2479")
                            .replace("&a", "\247a")
                            .replace("&b", "\247b")
                            .replace("&c", "\247c")
                            .replace("&d", "\247d")
                            .replace("&e", "\247e")
                            .replace("&f", "\247f")
                            .replace("&l", "\247l")
                            .replace("&n", "\247n")
                            .replace("&o", "\247o")
                            .replace("&k", "\247k")
                            .replace("&m", "\247m");
                }
                for (ProxiedPlayer player : SpecifyChat.instance.getProxy().getPlayers()) {
                    if (player == sender) continue;
                    boolean hasPermission = false;
                    for (String permission : channel.getPermissions()) {
                        if (player.hasPermission(permission)) {
                            hasPermission = true;
                            break;
                        }
                    }
                    if (hasPermission) {
                        player.sendMessage(new TextComponent(message));
                    }
                }
                sender.sendMessage(new TextComponent(message));
            }
        }
    }
}
