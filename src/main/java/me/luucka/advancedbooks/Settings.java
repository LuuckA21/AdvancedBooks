package me.luucka.advancedbooks;

import me.luucka.advancedbooks.config.BaseConfiguration;
import me.luucka.advancedbooks.config.IConfig;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings implements IConfig {

    private static final Logger LOGGER = Logger.getLogger("AdvancedBooks");

    private final AdvancedBooksPlugin plugin;

    private final BaseConfiguration config;

    private boolean perBookPermission;

    private String openOnJoin;

    private String prefix;

    private String noPermission;

    private String noConsole;

    private String reload;

    private String commandUsage;

    private String notExists;

    private String alreadyExists;

    private String createdBook;

    private String deletedBook;

    public boolean perBookPermission() {
        return perBookPermission;
    }

    public String openOnJoin() {
        return openOnJoin;
    }

    public String noPermission() {
        return prefix + noPermission;
    }

    public String noConsole() {
        return prefix + noConsole;
    }

    public String reload() {
        return prefix + reload;
    }

    public String commandUsage(final String usage) {
        return prefix + commandUsage.replace("{COMMAND_USAGE}", usage);
    }

    public String notExists(final String book) {
        return prefix + notExists.replace("{BOOK}", book);
    }

    public String alreadyExists(final String book) {
        return prefix + alreadyExists.replace("{BOOK}", book);
    }

    public String createdBook(final String book) {
        return prefix + createdBook.replace("{BOOK}", book);
    }

    public String deletedBook(final String book) {
        return prefix + deletedBook.replace("{BOOK}", book);
    }

    public Settings(final AdvancedBooksPlugin plugin) {
        this.plugin = plugin;
        this.config = new BaseConfiguration(new File(plugin.getDataFolder(), "config.yml"), "/config.yml");
        reloadConfig();
        if (perBookPermission) LOGGER.log(Level.INFO, "Using Per-Book-Permission");
    }

    @Override
    public void reloadConfig() {
        config.load();
        perBookPermission = config.getBoolean("per-book-permission", false);
        openOnJoin = config.getString("open-on-join", "");
        prefix = _getPrefix();
        noPermission = config.getString("no-permission", "");
        noConsole = config.getString("no-console", "");
        reload = config.getString("reload", "");
        commandUsage = config.getString("command-usage", "");
        notExists = config.getString("not-exists", "");
        alreadyExists = config.getString("already-exists", "");
        createdBook = config.getString("created-book", "");
        deletedBook = config.getString("deleted-book", "");
    }

    private String _getPrefix() {
        String prefix = config.getString("prefix", "");
        return prefix.isEmpty() ? "" : prefix + " ";
    }
}
