package me.luucka.advancedbooks.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Settings;
import me.luucka.advancedbooks.manager.BookManager;

import java.util.ArrayList;
import java.util.List;

import static me.luucka.extendlibrary.util.MMColor.toComponent;

public class AdvancedBooksCommand {

    private final AdvancedBooksPlugin plugin;
    private final Settings settings;
    private final BookManager bookManager;

    public AdvancedBooksCommand(final AdvancedBooksPlugin plugin) {
        this.plugin = plugin;
        this.settings = plugin.getSettings();
        this.bookManager = plugin.getBookManager();
        register();
    }

    private void register() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("book").replaceSuggestions(ArgumentSuggestions.strings(bookManager.getAllBooksName())));

        CommandAPICommand advancedBookCommand = new CommandAPICommand("advancedbooks")
                .withHelp("AdvancedBooks admin command", "AdvancedBooks admin command for create, delete and reload")
                .withPermission("advancedbooks.admin")
                .withSubcommand(
                        new CommandAPICommand("create")
                                .withUsage("/advancedbooks create <name> <title> <author>")
                                .withShortDescription("Create a new book")
                                .withArguments(new StringArgument("name"))
                                .withArguments(new StringArgument("title"))
                                .withArguments(new StringArgument("author"))
                                .executesPlayer((player, args) -> {
                                    final String name = (String) args.get("name");
                                    final String title = (String) args.get("title");
                                    final String author = (String) args.get("author");

                                    bookManager.getBookByName(name).ifPresentOrElse(
                                            book -> player.sendMessage(toComponent(settings.alreadyExists(book.getName()))),
                                            () -> player.sendMessage(toComponent(bookManager.createBook(name, title, author) ? settings.createdBook(name) : settings.alreadyExists(name)))
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("delete")
                                .withUsage("/advancedbooks delete <book>")
                                .withShortDescription("Delete a book")
                                .withArguments(arguments)
                                .executesPlayer((player, args) -> {
                                    final String name = (String) args.get("book");
                                    bookManager.deleteBook(
                                            bookManager.getBookByName(name).orElseThrow(() -> new RuntimeException(settings.notExists(name))
                                            ));
                                    player.sendMessage(toComponent(settings.deletedBook(name)));
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withUsage("/advancedbooks reload")
                                .withShortDescription("Reload plugin")
                                .executesPlayer((player, args) -> {
                                    plugin.reload();
                                    player.sendMessage(toComponent(settings.reload()));
                                })
                );

        advancedBookCommand.withUsage(
                advancedBookCommand.getSubcommands().stream().map(command -> command.getUsage()[0] + " - " + command.getShortDescription()).toArray(String[]::new)
        ).register();
    }
}
