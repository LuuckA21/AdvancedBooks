package me.luucka.advancedbooks;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.luucka.advancedbooks.command.AdvancedBooksCommand;
import me.luucka.advancedbooks.command.CommandArgument;
import me.luucka.advancedbooks.command.OpenBookCommand;
import me.luucka.advancedbooks.listener.PlayerListener;
import me.luucka.advancedbooks.manager.BookManager;
import me.luucka.extendlibrary.message.Message;
import me.luucka.extendlibrary.util.IReload;
import me.luucka.extendlibrary.util.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class AdvancedBooksPlugin extends JavaPlugin {

    private Setting setting;

    private BookManager bookManager;

    private Message messages;

    private final List<IReload> reloadList = new ArrayList<>();

    private static boolean usePlaceholderAPI = false;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
        );
    }

    @Override
    public void onEnable() {
        if (!VersionUtil.isServerVersionSupported(VersionUtil.v1_18_2_R01)) {
            Bukkit.getLogger().log(Level.WARNING, "Version " + VersionUtil.ServerVersion.fromString(Bukkit.getServer().getBukkitVersion()) + " is not supported!");
            Bukkit.getLogger().log(Level.WARNING, "Please use one of these versions: " + String.join(", ", VersionUtil.getSupportedVersions().toString()));
            Bukkit.getLogger().log(Level.WARNING, "Continue at own risk!!!");
        }

        CommandAPI.onEnable();

        this.setting = new Setting(this);
        this.reloadList.add(setting);

        this.bookManager = new BookManager(this);
        this.reloadList.add(bookManager);

        this.messages = new Message(this, "messages");
        this.reloadList.add(this.messages);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderAPI = true;
            Bukkit.getLogger().log(Level.INFO, "Find PlaceholderAPI...");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        CommandArgument.set(this);
        new AdvancedBooksCommand(this);
        new OpenBookCommand(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void reload() {
        for (final IReload iReload : reloadList) {
            iReload.reload();
        }
    }

    public Setting getSettings() {
        return setting;
    }

    public BookManager getBookManager() {
        return bookManager;
    }

    public Message getMessages() {
        return messages;
    }

    public static boolean isUsePlaceholderAPI() {
        return usePlaceholderAPI;
    }
}
