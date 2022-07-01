package me.luucka.advancedbooks.commands;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.exceptions.NotEnoughArgumentsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OpenBookCommand extends BaseCommand {

    private final AdvancedBooksPlugin plugin;

    public OpenBookCommand(final AdvancedBooksPlugin plugin) {
        super("openbook", "Open a Book");
        this.plugin = plugin;
        this.setUsage("/openbook <name>");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!sender.isPlayer()) throw new Exception(plugin.getSettings().noConsole());

        if (args.length < 1) throw new NotEnoughArgumentsException(this.usageMessage);

        final String bookName = args[0].toLowerCase();

        if (!plugin.getBooksDataManager().exists(bookName)) throw new Exception(plugin.getSettings().notExists(bookName));

        plugin.openBook(sender.getPlayer(), bookName);
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (args.length == 1) {
            if (plugin.getSettings().perBookPermission()) {
                final List<String> options = new ArrayList<>();
                for (final String book : plugin.getBooksDataManager().getAllBooksName()) {
                    if (sender.hasPermission("advancedbooks.open." + book)) {
                        options.add(book);
                    }
                }
                return options;
            }
            return plugin.getBooksDataManager().getAllBooksName();
        }

        return Collections.emptyList();
    }
}
