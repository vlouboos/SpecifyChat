package net.kinomc.specifychat.command;

import net.kinomc.specifychat.SpecifyChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PluginCommand extends Command {
    public PluginCommand() {
        super("SpecifyChat");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) || sender.hasPermission("sc.all")) {
            if (args.length == 0) {
                showInfo(sender);
            } else {
                if (args[0].toLowerCase().contains("info")) {
                    showInfo(sender);
                } else if (args[0].toLowerCase().contains("reload")) {
                    SpecifyChat.instance.reloadConfig(sender, true);
                } else {
                    sender.sendMessage(new TextComponent("" + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "---------------------------------------------"));
                    sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Running" + ChatColor.DARK_AQUA + " SpecifyChat-v1.0-SNAPSHOT" + ChatColor.YELLOW + " by vlouboos licenced to mc.kinomc.net"));
                    sender.sendMessage(new TextComponent());
                    sender.sendMessage(new TextComponent(ChatColor.YELLOW + "/specifychat info" + ChatColor.GRAY + " - Show plugin info."));
                    sender.sendMessage(new TextComponent(ChatColor.YELLOW + "/specifychat help" + ChatColor.GRAY + " - Display this screen."));
                    sender.sendMessage(new TextComponent(ChatColor.YELLOW + "/specifychat reload" + ChatColor.GRAY + " - Reload plugin."));
                    sender.sendMessage(new TextComponent("" + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "---------------------------------------------"));
                }
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.WHITE + "Unknown command. Type \"/help\" for help."));
        }
    }

    private void showInfo(CommandSender sender) {
        sender.sendMessage(new TextComponent("" + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "---------------------------------------------"));
        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Running" + ChatColor.DARK_AQUA + " SpecifyChat-v1.0-SNAPSHOT" + ChatColor.YELLOW + " by vlouboos licenced to mc.kinomc.net"));
        sender.sendMessage(new TextComponent(ChatColor.YELLOW + "Use" + ChatColor.DARK_AQUA + " /specifychat help" + ChatColor.YELLOW + " to get help"));
        sender.sendMessage(new TextComponent("" + ChatColor.AQUA + ChatColor.STRIKETHROUGH + "---------------------------------------------"));
    }
}
