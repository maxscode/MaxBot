package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.Config;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            builder.append("List of Commands:\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append("`").append(Config.get("PREFIX")).append(it).append("`\n"));

            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Command `" + search + "` does not exist").queue();
            return;
        }
        channel.sendMessage(command.getHelp()).queue();
        channel.sendMessage(command.getUsage()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows an ordered list of commands that the bot can invoke.";
    }

    @Override
    public String getUsage() {
        return "Use `" + Config.get("PREFIX") + "help [COMMAND]`";
    }
}
