package com.gmail.maxarmour2.maxbot.commands.management;

import com.gmail.maxarmour2.maxbot.Config;
import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class SetCustomPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final User author = ctx.getAuthor();

        if (args.isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor(author.getAsTag(), null, author.getAvatarUrl());
            missingArgs.setTitle("Missing Args");
            missingArgs.setDescription("Usage: `" + getUsage() + "`");
            missingArgs.setFooter("MaxBot Management Commands");

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor(author.getAsTag(), null, author.getAvatarUrl());
            noUserPerms.setTitle("No Permission");
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Manage Server");
            noUserPerms.setFooter("MaxBot Management Commands");

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        CustomPrefix.PREFIXES.put(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder();
        success.setTitle("Prefix Changed");
        success.setDescription("Prefix has been modified to `" + newPrefix + "`");
        success.setFooter("MaxBot Management Commands");

        channel.sendMessageEmbeds(success.build()).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server.";
    }

    @Override
    public String getUsage() {
        return getName() + "[prefix]`";
    }
}
