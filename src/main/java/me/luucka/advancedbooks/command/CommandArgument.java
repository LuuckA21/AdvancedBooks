package me.luucka.advancedbooks.command;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.manager.ABook;
import me.luucka.advancedbooks.manager.BookManager;
import me.luucka.extendlibrary.message.Message;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class CommandArgument {

    private static BookManager bookManager;

    private static Message messages;

    private CommandArgument() {
    }

    public static void set(AdvancedBooksPlugin plugin) {
        CommandArgument.bookManager = plugin.getBookManager();
        CommandArgument.messages = plugin.getMessages();
    }

    public static Argument<ABook> customABookArgument(String nodeName) {
        return new CustomArgument<>(new StringArgument(nodeName), info -> {
            if (info.sender() instanceof Player) {
                Optional<ABook> book = bookManager.getBookByName(info.input());
                if (book.isPresent()) {
                    return book.get();
                }
                throw CustomArgument.CustomArgumentException.fromAdventureComponent(messages.from("not-exists").with("book", info.input()).build());
            }
            throw CustomArgument.CustomArgumentException.fromAdventureComponent(messages.from("no-console").build());
        });
    }

}
