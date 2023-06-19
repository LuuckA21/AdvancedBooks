package me.luucka.advancedbooks;

import lombok.Getter;
import me.luucka.advancedbooks.commands.AdvancedBooksCommand;
import me.luucka.advancedbooks.commands.OpenBookCommand;
import me.luucka.advancedbooks.config.IConfig;
import me.luucka.advancedbooks.listeners.PlayerListeners;
import me.luucka.advancedbooks.managers.BookManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class AdvancedBooksPlugin extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("AdvancedBooks");

    @Getter
    private Settings settings;

    @Getter
    private BookManager BookManager;

    private final List<IConfig> configList = new ArrayList<>();

    @Getter
    private static boolean usePlaceholderAPI = false;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        settings = new Settings(this);
        configList.add(settings);

        this.BookManager = new BookManager(this);
        configList.add(BookManager);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderAPI = true;
            LOGGER.log(Level.INFO, "Find PlaceholderAPI...");
        }

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        new AdvancedBooksCommand(this);
        new OpenBookCommand(this);
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }
}
