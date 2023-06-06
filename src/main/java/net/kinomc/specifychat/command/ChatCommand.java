package net.kinomc.specifychat.command;

import net.kinomc.specifychat.Channel;
import net.kinomc.specifychat.storage.Storage;
import net.kinomc.specifychat.util.ConfigUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {
    public ChatCommand() {
        super("chat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length != 1) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Usage: /chat <channel>"));
            } else {
                String channelNameIn = args[0];
                if (channelNameIn.equalsIgnoreCase("a") || channelNameIn.equalsIgnoreCase("all") || channelNameIn.equalsIgnoreCase("general")) {
                    Storage.putChannel((ProxiedPlayer) sender, null);
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', ConfigUtil.getMessage(sender, "Join-Channel").replace("%name%", ConfigUtil.getMessage(sender, "General-Channel-Name")))));
                    return;
                }
                Channel channel = Storage.getChannelByName(channelNameIn);
                if (channel == null) {
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', ConfigUtil.getMessage(sender, "Channel-Not-Found").replace("%name", channelNameIn))));
                } else {
                    Storage.putChannel((ProxiedPlayer) sender, channel);
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', ConfigUtil.getMessage(sender, "Join-Channel").replace("%name%", channel.getName()))));
                }
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', ConfigUtil.getMessage(sender, "Command-Executed-By-Player-Only"))));
        }
    }
}
