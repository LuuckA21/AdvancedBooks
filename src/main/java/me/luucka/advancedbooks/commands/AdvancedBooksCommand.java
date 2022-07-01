package me.luucka.advancedbooks.commands;

import me.luucka.advancedbooks.AdvancedBooksPlugin;
import me.luucka.advancedbooks.exceptions.InsufficientPermissionException;
import me.luucka.advancedbooks.exceptions.NotEnoughArgumentsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdvancedBooksCommand extends BaseCommand {

    private final AdvancedBooksPlugin plugin;

    public AdvancedBooksCommand(final AdvancedBooksPlugin plugin) {
        super("advancedbooks", "Main command", "advancedbooks.admin", "abooks");
        this.plugin = plugin;
        this.setUsage("/advancedbooks < create | delete | reload >");
    }

    @Override
    public void execute(CommandSource sender, String[] args) throws Exception {
        if (!testPermissionSilent(sender.getSender())) throw new InsufficientPermissionException(plugin.getSettings().noPermission());

        if (args.length < 1) throw new NotEnoughArgumentsException(plugin.getSettings().commandUsage(this.usageMessage));

        final CommandType cmd;

        try {
            cmd = CommandType.valueOf(args[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            throw new Exception(this.usageMessage);
        }

        switch (cmd) {
            case CREATE -> {
                if (args.length < cmd.argsNeeded) throw new NotEnoughArgumentsException(plugin.getSettings().commandUsage("/advancedbooks create <name> <title> <author>"));
                final String bookName = args[1].toLowerCase();
                final String title = args[2];
                final String author = args[3];

                if (plugin.getBooksDataManager().exists(bookName)) throw new Exception(plugin.getSettings().alreadyExists(bookName));

                plugin.getBooksDataManager().createBook(bookName, title, author);
                sender.sendMessage(plugin.getSettings().createdBook(bookName));
            }
            case DELETE -> {
                if (args.length < cmd.argsNeeded) throw new NotEnoughArgumentsException(plugin.getSettings().commandUsage("/advancedbooks delete <name>"));
                final String bookName = args[1].toLowerCase();

                if (!plugin.getBooksDataManager().exists(bookName)) throw new Exception(plugin.getSettings().notExists(bookName));

                plugin.getBooksDataManager().delete(bookName);
                sender.sendMessage(plugin.getSettings().deletedBook(bookName));
            }
            case RELOAD -> {
                plugin.reload();
                sender.sendMessage(plugin.getSettings().reload());
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
            return plugin.getBooksDataManager().getAllBooksName();
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
    }

}
