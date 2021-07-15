package com.gmail.maxarmour2.maxbot.commands.management.moderation;

import com.gmail.maxarmour2.maxbot.Config;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class KickCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        // Missing Arguments Message.
        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            missingArgs.setDescription("Missing Arguments.\n" + getUsage());
            missingArgs.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(1);

        // Executed if the member who invokes this command does not have the permissions.
        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Kick Members");
            noUserPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        // Executed if the bot does not have the permissions to execute the command.
        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
            noBotPerms.setDescription("I do not have permission to execute this command.\nRequired Permission: Kick Members");
            noBotPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        final String reasonForKick = String.join(" ", args.subList(1, args.size()));

        // Kick successful
        EmbedBuilder success = new EmbedBuilder();
        success.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
        success.setDescription(targetMember.getAsMention() + " was kicked");
        success.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

        // Kick Unsuccessful
        EmbedBuilder failure = new EmbedBuilder();
        failure.setAuthor("Kick Command", null, ctx.getSelfUser().getAvatarUrl());
        failure.setDescription("Kick failed.\n" + getUsage());
        failure.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

        ctx.getGuild().kick(targetMember, reasonForKick).reason(reasonForKick).queue(
                (__) -> channel.sendMessageEmbeds(success.build()).queue(),
                (error) -> channel.sendMessageEmbeds(failure.build()).queue()
        );

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
        return "`" + Config.get("PREFIX") + getName() + " [user] [reason]`";
    }
}
