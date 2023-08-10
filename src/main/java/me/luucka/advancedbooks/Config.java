package me.luucka.advancedbooks;

import me.luucka.advancedbooks.config.BaseConfiguration;
import me.luucka.extendlibrary.util.IReload;

import java.io.File;

public class Config implements IReload {

    private final BaseConfiguration config;

    private boolean perBookPermission;

    private String bookOnJoin;

    public boolean isPerBookPermission() {
        return perBookPermission;
    }

    public String getBookOnJoin() {
        return bookOnJoin;
    }

    public Config(final AdvancedBooksPlugin plugin) {
        this.config = new BaseConfiguration(new File(plugin.getDataFolder(), "config.yml"), "/config.yml");
        reload();
    }

    @Override
    public void reload() {
        config.load();
        perBookPermission = config.getBoolean("per-book-permission", false);
        bookOnJoin = config.getString("open-on-join", "");
    }
}
