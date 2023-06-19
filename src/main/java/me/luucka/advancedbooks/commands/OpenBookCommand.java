package me.luucka.advancedbooks.commands;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Settings;
import me.luucka.advancedbooks.managers.BookManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenBookCommand extends BaseCommand {

    private final AdvancedBooksPlugin plugin;
    private final BookManager bookManager;
    private final Settings settings;

    public OpenBookCommand(final AdvancedBooksPlugin plugin) {
        super("openbook", "Open a Book");
        this.plugin = plugin;
        this.bookManager = plugin.getBookManager();
        this.settings = plugin.getSettings();
        this.setUsage("/openbook <name>");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!sender.isPlayer()) throw new Exception(plugin.getSettings().noConsole());
        if (args.length < 1) throw new Exception(plugin.getSettings().commandUsage(this.usageMessage));

        final Player player = sender.getPlayer();
        final String name = args[0].toLowerCase();

        player.openBook(bookManager.getBookByName(name)
                .map(book -> {
                    if (settings.perBookPermission()
                            && !player.hasPermission("advancedbooks.open." + book.getName())) {
                        throw new RuntimeException(settings.noPermission());
                    }
                    return book.toBook(player);
                }).orElseThrow(
                        () -> new RuntimeException(settings.notExists(name))
                )
        );
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (args.length == 1) {
            if (plugin.getSettings().perBookPermission() && !sender.hasPermission("advancedbooks.bypass")) {
                final List<String> options = new ArrayList<>();
                for (final String book : plugin.getBookManager().getAllBooksName()) {
                    if (sender.hasPermission("advancedbooks.open." + book)) {
                        options.add(book);
                    }
                }
                return options;
            }
            return plugin.getBookManager().getAllBooksName();
        }

        return Collections.emptyList();
    }
}
