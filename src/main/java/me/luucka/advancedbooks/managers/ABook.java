package me.luucka.advancedbooks.managers;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.config.BaseConfiguration;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.luucka.advancedbooks.utils.MMColor.toComponent;

public class ABook {

    private final BaseConfiguration config;

    public String getName() {
        return config.getFile().getName().substring(0, config.getFile().getName().length() - 4);
    }

    @Getter
    private final String title;

    @Getter
    private final String author;

    @Getter
    private final List<String> pages = new ArrayList<>();

    public ABook(final BaseConfiguration config) {
        this.config = config;
        config.load();
        this.title = config.getString("title", "");
        this.author = config.getString("author", "");
        final Set<String> pages = config.getKeys("pages");
        for (final String page : pages) {
            this.pages.add(String.join("\n", config.getList("pages." + page, String.class)));
        }
    }

    public ABook(final BaseConfiguration config, final String title, final String author) {
        this.config = config;
        config.load();
        this.title = title;
        this.author = author;
        this.pages.addAll(List.of("row1", "row2", "row3"));
        save();
    }

    public void save() {
        config.setProperty("title", title);
        config.setProperty("author", author);
        config.setProperty("pages." + 0, pages);
        config.save();
    }

    public void delete() {
        config.getFile().delete();
    }

    public Book toBook(final Player player) {
        final List<Component> pages = new ArrayList<>();
        for (String page : this.pages) {
            if (AdvancedBooksPlugin.isUsePlaceholderAPI()) {
                page = PlaceholderAPI.setPlaceholders(player, page);
            }
            pages.add(toComponent(page));
        }
        return Book.book(toComponent(this.title), toComponent(this.author), pages);
    }

}
