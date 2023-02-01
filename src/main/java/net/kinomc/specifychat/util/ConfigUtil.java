package net.kinomc.specifychat.util;

import net.kinomc.specifychat.SpecifyChat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

public class ConfigUtil {
    public static Configuration getConfig(String file) {
        return getConfig(new File(SpecifyChat.instance.getDataFolder(), file));
    }

    private static Configuration getConfig(File file) {
        if (!file.exists()) {
            InputStream in = SpecifyChat.instance.getResourceAsStream(file.getName());
            if (in == null) {
                throw new IllegalArgumentException(ChatColor.DARK_RED + "The embedded resource " + ChatColor.YELLOW + "'" + file.getName() + "'" + ChatColor.DARK_RED + " cannot be found in " + ChatColor.YELLOW + SpecifyChat.instance.getFile());
            } else {
                File outDir = SpecifyChat.instance.getDataFolder();
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                try {
                    file.createNewFile();
                    OutputStream out = Files.newOutputStream(file.toPath());
                    byte[] buf = new byte[1024];

                    int len;
                    while((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    out.close();
                    in.close();
                } catch (IOException e) {
                    SpecifyChat.instance.getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "Could not save " + ChatColor.YELLOW + file.getName() + ChatColor.DARK_RED + " to " + ChatColor.YELLOW + file, e);
                }

            }
        }
        try {
            return getProvider().load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ConfigurationProvider getProvider() {
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }
}
