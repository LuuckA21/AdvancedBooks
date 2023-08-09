package me.luucka.advancedbooks.listener;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Settings;
import me.luucka.advancedbooks.manager.BookManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final Settings settings;
    private final BookManager bookManager;

    public PlayerListener(final AdvancedBooksPlugin plugin) {
        this.settings = plugin.getSettings();
        this.bookManager = plugin.getBookManager();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        bookManager.getBookByName(settings.bookOnJoin())
                .ifPresent(aBook -> player.openBook(aBook.toBook(player)));
    }

}
