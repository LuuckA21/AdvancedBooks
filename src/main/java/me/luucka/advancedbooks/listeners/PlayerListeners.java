package me.luucka.advancedbooks.listeners;

import lombok.RequiredArgsConstructor;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerListeners implements Listener {

    private final AdvancedBooksPlugin plugin;

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        if (!plugin.getSettings().openOnJoin().isEmpty()) {
            plugin.openBook(event.getPlayer(), plugin.getSettings().openOnJoin());
        }
    }

}
