package me.luucka.advancedbooks;

import lombok.Getter;
import me.luucka.advancedbooks.commands.AdvancedBooksCommand;
import me.luucka.advancedbooks.commands.OpenBookCommand;
import me.luucka.advancedbooks.config.IConfig;
import me.luucka.advancedbooks.listeners.PlayerListeners;
import me.luucka.advancedbooks.managers.BooksDataManager;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
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
    private BooksDataManager booksDataManager;

    private final List<IConfig> configList = new ArrayList<>();

    @Getter
    private boolean usePlaceholderAPI = false;

    @Override
    public void onEnable() {
        if (LOGGER != this.getLogger()) LOGGER.setParent(this.getLogger());

        settings = new Settings(this);
        configList.add(settings);

        this.booksDataManager = new BooksDataManager(this);
        configList.add(booksDataManager);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            usePlaceholderAPI = true;
            LOGGER.log(Level.INFO, "Find PlaceholderAPI...");
        }

        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        new AdvancedBooksCommand(this);
        new OpenBookCommand(this);
    }

    @Override
    public void onDisable() {
    }

    public void openBook(final Player player, final String bookName) {
        final Component title = booksDataManager.title(bookName);
        final Component author = booksDataManager.author(bookName);
        final List<Component> pages = booksDataManager.pages(bookName, player);
        Book book = Book.book(title, author, pages);
        player.openBook(book);
    }

    public void reload() {
        for (final IConfig iConfig : configList) {
            iConfig.reloadConfig();
        }
    }
}
