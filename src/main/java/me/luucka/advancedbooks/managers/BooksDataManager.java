package me.luucka.advancedbooks.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.config.BaseConfiguration;
import me.luucka.advancedbooks.config.IConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static me.luucka.advancedbooks.utils.Color.colorize;

public class BooksDataManager implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("AdvancedBooks");

    private final AdvancedBooksPlugin plugin;
    private final File dataFolder;
    private final Map<String, BaseConfiguration> books = new HashMap<>();

    public BooksDataManager(final AdvancedBooksPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(this.plugin.getDataFolder(), "books");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        reloadConfig();
    }

    public List<String> getAllBooksName() {
        return new ArrayList<>(books.keySet());
    }

    public boolean exists(final String bookName) {
        final BaseConfiguration configuration = books.get(bookName.toLowerCase());
        return configuration != null;
    }

    public void createBook(final String bookName, final String bookTitle, final String author) {
        BaseConfiguration configuration = books.get(bookName.toLowerCase());
        if (configuration == null) {
            final File file = new File(dataFolder, bookName.toLowerCase() + ".yml");
            if (file.exists()) return;

            configuration = new BaseConfiguration(file);
            configuration.load();
            configuration.setProperty("title", bookTitle);
            configuration.setProperty("author", author);
            configuration.setProperty("pages." + 0, List.of("row1", "row2", "row3"));
            configuration.save();
            books.put(bookName.toLowerCase(), configuration);
        }
    }

    public void delete(final String bookName) {
        books.remove(bookName.toLowerCase());
        final File file = new File(dataFolder, bookName.toLowerCase() + ".yml");
        file.delete();
    }

    public Component title(final String bookName) {
        return colorize(books.get(bookName.toLowerCase()).getString("title", "title"));
    }

    public Component author(final String bookName) {
        return colorize(books.get(bookName.toLowerCase()).getString("author", "author"));
    }

    public List<Component> pages(final String bookName, final Player player) {
        final BaseConfiguration configuration = books.get(bookName.toLowerCase());
        final List<Component> pages = new ArrayList<>();
        final Set<String> keys = books.get(bookName.toLowerCase()).getKeys("pages");
        for (final String k : keys) {
            String page = String.join("\n", configuration.getList("pages." + k, String.class));
            if (plugin.isUsePlaceholderAPI()) {
                page = PlaceholderAPI.setPlaceholders(player, page);
            }
            pages.add(colorize(page));
        }
        return pages;
    }

    @Override
    public void reloadConfig() {
        books.clear();
        final File[] listOfFiles = dataFolder.listFiles();
        if (listOfFiles.length >= 1) {
            for (final File file : listOfFiles) {
                String fileName = file.getName();
                if (file.isFile() && fileName.endsWith(".yml")) {
                    try {
                        final BaseConfiguration configuration = new BaseConfiguration(file);
                        configuration.load();
                        books.put(fileName.substring(0, fileName.length() - 4), configuration);
                    } catch (final Exception ex) {
                        LOGGER.log(Level.WARNING, "Book file " + fileName + " loading error!");
                    }
                }
            }
        }
    }

}
