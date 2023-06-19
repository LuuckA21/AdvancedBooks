package me.luucka.advancedbooks;

import me.luucka.advancedbooks.config.BaseConfiguration;
import me.luucka.advancedbooks.config.IConfig;

import java.io.File;

public class Settings implements IConfig {

    private final BaseConfiguration config;

    private boolean perBookPermission;

    private String bookOnJoin;

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

    public String bookOnJoin() {
        return bookOnJoin;
    }

    public String noPermission() {
        return noPermission.replace("{PREFIX}", prefix);
    }

    public String noConsole() {
        return noConsole.replace("{PREFIX}", prefix);
    }

    public String reload() {
        return reload.replace("{PREFIX}", prefix);
    }

    public String commandUsage(final String usage) {
        return commandUsage.replace("{PREFIX}", prefix).replace("{COMMAND_USAGE}", usage);
    }

    public String notExists(final String book) {
        return notExists.replace("{PREFIX}", prefix).replace("{BOOK}", book);
    }

    public String alreadyExists(final String book) {
        return alreadyExists.replace("{PREFIX}", prefix).replace("{BOOK}", book);
    }

    public String createdBook(final String book) {
        return createdBook.replace("{PREFIX}", prefix).replace("{BOOK}", book);
    }

    public String deletedBook(final String book) {
        return deletedBook.replace("{PREFIX}", prefix).replace("{BOOK}", book);
    }

    public Settings(final AdvancedBooksPlugin plugin) {
        this.config = new BaseConfiguration(new File(plugin.getDataFolder(), "config.yml"), "/config.yml");
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        config.load();
        perBookPermission = config.getBoolean("per-book-permission", false);
        bookOnJoin = config.getString("open-on-join", "");
        prefix = config.getString("prefix", "");
        noPermission = config.getString("no-permission", "");
        noConsole = config.getString("no-console", "");
        reload = config.getString("reload", "");
        commandUsage = config.getString("command-usage", "");
        notExists = config.getString("not-exists", "");
        alreadyExists = config.getString("already-exists", "");
        createdBook = config.getString("created-book", "");
        deletedBook = config.getString("deleted-book", "");
    }
}
