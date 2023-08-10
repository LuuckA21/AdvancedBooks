package me.luucka.advancedbooks.listener;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Config;
import me.luucka.advancedbooks.manager.BookManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final Config config;
    private final BookManager bookManager;

    public PlayerListener(final AdvancedBooksPlugin plugin) {
        this.config = plugin.getSettings();
        this.bookManager = plugin.getBookManager();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        bookManager.getBookByName(config.getBookOnJoin())
                .ifPresent(book -> player.openBook(book.toBook(player)));
    }

}
