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
        boolean pass = !(sender instanceof ProxiedPlayer);
        if (!pass) {
            for (String permission : channel.getPermissions()) {
                if (sender.hasPermission(permission)) {
                    pass = true;
                    break;
                }
            }
        }
        if (!pass) {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "No-Permission")));
        } else {
            if (args.length == 0) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /" + channel.getCommands().get(0) + " <Message>"));
            } else {
                StringBuilder sb = new StringBuilder();
                for (String argument : args) {
                    sb.append(" ").append(argument);
                }
                // Format
                String message = channel.getFormat()
                        .replace("%name%", channel.getName())
                        .replace("%player%", PlayerUtil.getSenderName(sender))
                        .replace("%time%", SpecifyChat.instance.dateFormat.format(new Date()));
                // Translate format's color code
                message = ChatColor.translateAlternateColorCodes('&', message);
                // Translate message's color code
                String finalMessage = sb.substring(1);
                message = message.replace("%message%", channel.enableChatColor() ? ChatColor.translateAlternateColorCodes('&', finalMessage) : finalMessage);
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
