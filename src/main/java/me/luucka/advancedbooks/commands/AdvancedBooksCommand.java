package me.luucka.advancedbooks.commands;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.Settings;
import me.luucka.advancedbooks.managers.BookManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.luucka.advancedbooks.utils.MMColor.toComponent;

public class AdvancedBooksCommand extends BaseCommand {

    private final AdvancedBooksPlugin plugin;
    private final Settings settings;
    private final BookManager bookManager;

    public AdvancedBooksCommand(final AdvancedBooksPlugin plugin) {
        super("abooks", "Main command, create, delete, reload", "advancedbooks.admin");
        this.plugin = plugin;
        this.settings = plugin.getSettings();
        this.bookManager = plugin.getBookManager();
        this.setUsage("/advancedbooks < create | delete | reload >");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!sender.isPlayer()) throw new Exception(settings.noConsole());
        if (!testPermissionSilent(sender.getSender())) throw new Exception(settings.noPermission());
        if (args.length < CommandType.getMinArgsNeeded()) throw new Exception(settings.commandUsage(getUsage()));

        final CommandType cmd;

        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(settings.commandUsage(getUsage()));
        }

        final Player player = sender.getPlayer();

        switch (cmd) {
            case CREATE -> {
                if (args.length < cmd.argsNeeded)
                    throw new Exception(settings.commandUsage("/advancedbooks create <name> <title> <author>"));

                final String name = args[1].toLowerCase();
                bookManager.getBookByName(name).ifPresentOrElse(
                        book -> player.sendMessage(toComponent(settings.alreadyExists(book.getName()))),
                        () -> player.sendMessage(toComponent(bookManager.createBook(name, args[2], args[3]) ? settings.createdBook(name) : settings.alreadyExists(name)))
                );
            }
            case DELETE -> {
                if (args.length < cmd.argsNeeded)
                    throw new Exception(settings.commandUsage("/advancedbooks delete <name>"));

                final String name = args[1].toLowerCase();
                bookManager.deleteBook(
                        bookManager.getBookByName(name).orElseThrow(() -> new RuntimeException(settings.notExists(name))
                        ));
                player.sendMessage(toComponent(settings.deletedBook(name)));
            }
            case RELOAD -> {
                plugin.reload();
                player.sendMessage(toComponent(settings.reload()));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSource sender, String[] args) {
        if (!testPermissionSilent(sender.getSender())) return Collections.emptyList();

        if (args.length == 1) {
            final List<String> options = new ArrayList<>();
            for (final CommandType ct : CommandType.values()) {
                options.add(ct.name().toLowerCase());
            }
            return options;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            return bookManager.getAllBooksName();
        }

        return Collections.emptyList();
    }

    private enum CommandType {
        CREATE(4),
        DELETE(2),
        RELOAD(1);

        private final int argsNeeded;

        CommandType(int argsNeeded) {
            this.argsNeeded = argsNeeded;
        }

        public static int getMinArgsNeeded() {
            int min = values()[0].argsNeeded;
            for (CommandType ct : values()) {
                if (ct.argsNeeded < min) min = ct.argsNeeded;
            }
            return min;
        }
    }

}
