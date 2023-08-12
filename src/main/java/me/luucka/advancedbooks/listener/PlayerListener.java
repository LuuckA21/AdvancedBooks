package me.luucka.advancedbooks.listener;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Setting;
import me.luucka.advancedbooks.manager.BookManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final Setting setting;
    private final BookManager bookManager;

    public PlayerListener(final AdvancedBooksPlugin plugin) {
        this.setting = plugin.getSettings();
        this.bookManager = plugin.getBookManager();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        bookManager.getBookByName(setting.getBookOnJoin())
                .ifPresent(book -> player.openBook(book.toBook(player)));
    }

}
