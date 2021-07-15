package com.gmail.maxarmour2.maxbot.commands.moderation;

import com.gmail.maxarmour2.maxbot.Config;
import com.gmail.maxarmour2.maxbot.commands.CommandContext;
import com.gmail.maxarmour2.maxbot.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class KickCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        final Member selfMember = ctx.getSelfMember();
        final Member targetMember = message.getMentionedMembers().get(0);
        final String reasonForKick =  String.join(" ", args.subList(1, args.size()));

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            missingArgs.setDescription("Missing Arguments.\n" + getUsage());
            missingArgs.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            noUserPerms.setDescription("You do not have permission to invoke this command.");
            noUserPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            noBotPerms.setDescription("I do not have permission to execute this command.");
            noBotPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        AtomicBoolean userIsKicked = new AtomicBoolean(false);

        ctx.getGuild().ban(targetMember, 0, reasonForKick).reason(reasonForKick).queue(
                (__) -> userIsKicked.set(true),
                (error) -> userIsKicked.set(false));

        if (userIsKicked.get()) {
            EmbedBuilder success = new EmbedBuilder();
            success.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            success.setDescription(targetMember.getAsMention() + " was kicked");
            success.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(success.build()).queue();
            return;
        }

        if (!userIsKicked.get()) {
            EmbedBuilder failure = new EmbedBuilder();
            failure.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            failure.setDescription("Kick failed.");
            failure.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(failure.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getHelp() {
        return "Kicks the specified player from the server. Requires Kick permission to invoke.";
    }

    @Override
    public String getUsage() {
        return "Usage: `" + Config.get("PREFIX") + getName() + " [user] [reason]`";
    }

    @Override
    public String getHelpCommand() {
        return "`" + Config.get("PREFIX") + getName() + " [user] [reason]`";
    }
}
