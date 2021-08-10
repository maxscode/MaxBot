package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandManager;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements Command {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {

        JDA api = ctx.getJDA();

        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();
        String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());

        StringBuilder commands = new StringBuilder();
        manager.getCommands().stream().map(Command::getName).forEach(
                (it) -> commands.append("`").append(prefix).append(it).append("`\n"));

        StringBuilder usages = new StringBuilder();
        manager.getCommands().stream().map(Command::getHelp).forEach(
                (it) -> usages.append(it).append("\n"));

        // Embed Defaults
        String defaultTitle = "Help Command";
        String defaultFooter = "MaxBot Command Manager";


        if (args.isEmpty()) {

            EmbedBuilder output = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .addField("Command", commands.toString(), true)
                    .addField("Usage", usages.toString(), true)
                    .setFooter("Use "+ prefix + "help [command] for more information\n" + defaultFooter);

            channel.sendMessageEmbeds(output.build()).queue();
            return;
        }

        String search = args.get(0);
        Command command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Command `" + search + "` does not exist").queue();
            return;
        }
        EmbedBuilder usageInfo = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription(command.getHelp() + "\n" + "Usage: `" + prefix +  command.getUsage() + "`")
                .setFooter(defaultFooter);

        channel.sendMessageEmbeds(usageInfo.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows a list of commands";
    }

    @Override
    public String getUsage() {
        return getName() + " [COMMAND]";
    }
}
