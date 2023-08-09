package me.luucka.advancedbooks.command;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Settings;
import me.luucka.advancedbooks.manager.BookManager;
import me.luucka.extendlibrary.util.MMColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenBookCommand {

    private final AdvancedBooksPlugin plugin;
    private final BookManager bookManager;
    private final Settings settings;

    public OpenBookCommand(AdvancedBooksPlugin plugin) {
        this.plugin = plugin;
        this.bookManager = plugin.getBookManager();
        this.settings = plugin.getSettings();
        register();
    }

    private void register() {
        List<Argument<?>> arguments = new ArrayList<>();
        arguments.add(new StringArgument("book").replaceSuggestions(ArgumentSuggestions.strings(info -> {
            if (info.sender() instanceof Player player) {
                if (plugin.getSettings().perBookPermission() && !player.hasPermission("advancedbooks.bypass")) {
                    final List<String> options = new ArrayList<>();
                    for (final String book : plugin.getBookManager().getAllBooksName()) {
                        if (player.hasPermission("advancedbooks.open." + book)) {
                            options.add(book);
                        }
                    }
                    return options.toArray(new String[0]);
                }
            }
            return bookManager.getAllBooksName().toArray(new String[0]);
        })));

        new CommandAPICommand("openbook")
                .withHelp("Open a book", "Open the selected book")
                .withUsage("/openbook <book>")
                .withArguments(arguments)
                .executesPlayer((player, args) -> {
                    final String name = (String) args.get("book");

                    player.openBook(bookManager.getBookByName(name)
                            .map(book -> {
                                if (settings.perBookPermission()
                                        && !player.hasPermission("advancedbooks.open." + book.getName())) {
                                    throw new RuntimeException(settings.noPermission());
                                }
                                return book.toBook(player);
                            }).orElseThrow(
                                    () -> {
                                        CommandAPIBukkit.failWithAdventureComponent(MMColor.toComponent(settings.notExists(name)));
                                    }
                            )
                    );
                })
                .register();
    }

    // Function that returns our custom argument
    public Argument<World> customWorldArgument(String nodeName) {

        // Construct our CustomArgument that takes in a String input and returns a World object
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            // Parse the world from our input
            World world = Bukkit.getWorld(info.input());

            if (world == null) {
                throw CustomArgument.CustomArgumentException.fromMessageBuilder(new CustomArgument.MessageBuilder("Unknown world: ").appendArgInput());
            } else {
                return world;
            }
        }).replaceSuggestions(ArgumentSuggestions.strings(info ->
                // List of world names on the server
                Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new))
        );
    }


}
