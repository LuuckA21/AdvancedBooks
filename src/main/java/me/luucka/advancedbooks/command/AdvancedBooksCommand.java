package me.luucka.advancedbooks.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.manager.ABook;
import me.luucka.advancedbooks.manager.BookManager;
import me.luucka.extendlibrary.message.Message;

import static me.luucka.advancedbooks.command.CommandArgument.customABookArgument;

public class AdvancedBooksCommand {

    private final AdvancedBooksPlugin plugin;
    private final BookManager bookManager;

    private final Message messages;

    public AdvancedBooksCommand(final AdvancedBooksPlugin plugin) {
        this.plugin = plugin;
        this.bookManager = plugin.getBookManager();
        this.messages = plugin.getMessages();
        register();
    }

    private void register() {
        CommandAPICommand advancedBookCommand = new CommandAPICommand("advancedbooks")
                .withHelp("AdvancedBooks admin command", "AdvancedBooks admin command for create, delete and reload")
                .withPermission("advancedbooks.admin")
                .withSubcommand(
                        new CommandAPICommand("create")
                                .withUsage("/advancedbooks create <name> [title] [author]")
                                .withShortDescription("Create a new book")
                                .withArguments(new StringArgument("name"))
                                .withOptionalArguments(new StringArgument("title"))
                                .withOptionalArguments(new StringArgument("author"))
                                .executesPlayer((player, args) -> {
                                    final String name = (String) args.get("name");
                                    final String title = (String) args.getOptional("title").orElse(name);
                                    final String author = (String) args.getOptional("author").orElse(player.getName());

                                    bookManager.getBookByName(name).ifPresentOrElse(
                                            book -> messages.from("already-exists").with("book", book.getName()).send(player),
                                            () -> messages.from(bookManager.createBook(name, title, author) ? "created-book" : "already-exists").with("book", name).send(player)
                                    );
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("delete")
                                .withUsage("/advancedbooks delete <book>")
                                .withShortDescription("Delete a book")
                                .withArguments(customABookArgument("book")
                                        .replaceSuggestions(ArgumentSuggestions.strings(info -> bookManager.getAllBooksName().toArray(new String[0])))
                                )
                                .executesPlayer((player, args) -> {
                                    final ABook book = (ABook) args.get("book");
                                    bookManager.deleteBook(book);
                                    messages.from("deleted-book").with("book", book.getName()).send(player);
                                })
                )
                .withSubcommand(
                        new CommandAPICommand("reload")
                                .withUsage("/advancedbooks reload")
                                .withShortDescription("Reload plugin")
                                .executesPlayer((player, args) -> {
                                    plugin.reload();
                                    messages.from("reload").send(player);
                                })
                );

        advancedBookCommand.withUsage(
                advancedBookCommand.getSubcommands().stream().map(command -> command.getUsage()[0] + " - " + command.getShortDescription()).toArray(String[]::new)
        ).register();
    }
}
