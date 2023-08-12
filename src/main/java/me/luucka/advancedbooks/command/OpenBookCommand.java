package me.luucka.advancedbooks.command;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Setting;
import me.luucka.advancedbooks.manager.ABook;
import me.luucka.advancedbooks.manager.BookManager;
import me.luucka.extendlibrary.message.Message;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.luucka.advancedbooks.command.CommandArgument.customABookArgument;

public class OpenBookCommand {

    private final BookManager bookManager;

    private final Setting setting;

    private final Message messages;

    public OpenBookCommand(AdvancedBooksPlugin plugin) {
        this.bookManager = plugin.getBookManager();
        this.setting = plugin.getSettings();
        this.messages = plugin.getMessages();
        register();
    }

    private void register() {
        new CommandAPICommand("openbook")
                .withHelp("Open a book", "Open the selected book")
                .withUsage("/openbook <book>")
                .withArguments(customABookArgument("book")
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            if (info.sender() instanceof Player player) {
                                if (setting.isPerBookPermission() && !player.hasPermission("advancedbooks.bypass")) {
                                    final List<String> options = new ArrayList<>();
                                    for (final String book : bookManager.getAllBooksName()) {
                                        if (player.hasPermission("advancedbooks.open." + book)) {
                                            options.add(book);
                                        }
                                    }
                                    return options.toArray(new String[0]);
                                }
                            }
                            return bookManager.getAllBooksName().toArray(new String[0]);
                        })))
                .executesPlayer((player, args) -> {
                    final ABook book = (ABook) args.get("book");
                    if (setting.isPerBookPermission() && !player.hasPermission("advancedbooks.open." + book.getName())) {
                        throw CommandAPIBukkit.failWithAdventureComponent(messages.from("no-permission").build());
                    }
                    player.openBook(book.toBook(player));
                })
                .register();
    }
}
