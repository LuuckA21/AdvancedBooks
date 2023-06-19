package me.luucka.advancedbooks.managers;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.config.BaseConfiguration;
import me.luucka.advancedbooks.config.IConfig;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookManager implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("AdvancedBooks");

    private final File dataFolder;
    private final Set<ABook> books = new HashSet<>();

    public BookManager(final AdvancedBooksPlugin plugin) {
        this.dataFolder = new File(plugin.getDataFolder(), "books");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
        reloadConfig();
    }

    public List<String> getAllBooksName() {
        return books.stream().map(ABook::getName).toList();
    }

    public Optional<ABook> getBookByName(final String name) {
        return books.stream().filter(book -> book.getName().equalsIgnoreCase(name)).findFirst();
    }

    public boolean createBook(final String name, final String title, final String author) {
        final File file = new File(dataFolder, name + ".yml");
        if (file.exists()) return false;
        return books.add(new ABook(new BaseConfiguration(file), title, author));
    }

    public void deleteBook(final ABook book) {
        books.remove(book);
        book.delete();
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
                        books.add(new ABook(new BaseConfiguration(file)));
                    } catch (final Exception ex) {
                        LOGGER.log(Level.WARNING, "Book file " + fileName + " loading error!");
                    }
                }
            }
        }
    }

}
