package me.luucka.advancedbooks;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.luucka.advancedbooks.command.AdvancedBooksCommand;
import me.luucka.advancedbooks.command.OpenBookCommand;
import me.luucka.advancedbooks.config.IConfig;
import me.luucka.advancedbooks.listener.PlayerListener;
import me.luucka.advancedbooks.manager.BookManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AdvancedBooksPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("AdvancedBooks");

    private Settings settings;

    private BookManager bookManager;

    private final List<IConfig> configList = new ArrayList<>();

    private static boolean usePlaceholderAPI = false;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
        );
    }

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());
        
        CommandAPI.onEnable();

        settings = new Settings(this);
        configList.add(settings);

        this.bookManager = new BookManager(this);
        configList.add(bookManager);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderAPI = true;
            LOGGER.log(Level.INFO, "Find PlaceholderAPI...");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        new AdvancedBooksCommand(this);
        new OpenBookCommand(this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    public BookManager getBookManager() {
        return bookManager;
    }

    public static boolean isUsePlaceholderAPI() {
        return usePlaceholderAPI;
    }
}
