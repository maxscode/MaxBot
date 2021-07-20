package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandManager;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

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
        manager.getCommands().stream().map(ICommand::getName).forEach(
                (it) -> commands.append("`").append(prefix).append(it).append("`\n"));

        StringBuilder usages = new StringBuilder();
        manager.getCommands().stream().map(ICommand::getHelp).forEach(
                (it) -> usages.append(it).append("\n"));


        if (args.isEmpty()) {

            EmbedBuilder output = new EmbedBuilder();
            output.setAuthor("Commands", null, api.getSelfUser().getAvatarUrl());
            output.addField("Command", commands.toString(), true);
            output.addField("Usage", usages.toString(), true);
            output.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(output.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Command `" + search + "` does not exist").queue();
            return;
        }
        EmbedBuilder usageInfo = new EmbedBuilder();
        usageInfo.setAuthor("Help Command", null, api.getSelfUser().getAvatarUrl());
        usageInfo.setDescription(command.getHelp() + "\n" + "`" + prefix +  command.getUsage());
        usageInfo.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

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
        return getName() + " [COMMAND]`";
    }
}
