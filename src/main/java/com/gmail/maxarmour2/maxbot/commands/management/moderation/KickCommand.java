package com.gmail.maxarmour2.maxbot.commands.management.moderation;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
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
        final String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());

        // Missing Arguments Message.
        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
            missingArgs.setTitle("Kick Command");
            missingArgs.setDescription("Missing Arguments.\n Usage: `" + prefix + getUsage() + "`");
            missingArgs.setFooter("MaxBot Server Management");

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(1);

        // Executed if the member who invokes this command does not have the permissions.
        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
            noUserPerms.setTitle("Kick Command");
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Kick Members");
            noUserPerms.setFooter("MaxBot Server Management");

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        // Executed if the bot does not have the permissions to execute the command.
        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
            noBotPerms.setTitle("Kick Command");
            noBotPerms.setDescription("I do not have permission to execute this command.\nRequired Permission: Kick Members");
            noBotPerms.setFooter("MaxBot Server Management");

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        final String reasonForKick = String.join(" ", args.subList(1, args.size()));

        // Kick successful
        EmbedBuilder success = new EmbedBuilder();
        success.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
        success.setTitle("Kick Command");
        success.setDescription(targetMember.getAsMention() + " was kicked");
        success.setFooter("MaxBot Server Management");

        // Kick Unsuccessful
        EmbedBuilder failure = new EmbedBuilder();
        failure.setAuthor(ctx.getAuthor().getAsTag(), null, ctx.getAuthor().getAvatarUrl());
        failure.setTitle("Kick Command");
        failure.setDescription("Kick unsuccessful.");
        failure.setFooter("MaxBot Server Management");

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
        return "Kicks the specified player from the server.";
    }

    @Override
    public String getUsage() {
        return getName() + " [user] [reason]";
    }
}
