package com.gmail.maxarmour2.maxbot.commands.management.moderation;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BanCommand implements Command {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        // Embed Defaults
        String defaultAuthor = ctx.getAuthor().getAsTag();
        String defaultAuthorAvatar = ctx.getAuthor().getAvatarUrl();
        String defaultTitle = "Ban Command";
        String defaultFooter = "MaxBot Server Management";

        // Missing Arguments Message.
        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            missingArgs.setTitle(defaultTitle);
            missingArgs.setDescription("Missing Arguments.");
            missingArgs.setFooter(defaultFooter);

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(1);

        // Executed if the member who invokes this command does not have the permissions.
        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            noUserPerms.setTitle(defaultTitle);
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Ban Members");
            noUserPerms.setFooter(defaultFooter);

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        // Executed if the bot does not have the permissions to execute the command.
        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            noBotPerms.setTitle(defaultTitle);
            noBotPerms.setDescription("I do not have permission to execute this command.\nRequired Permission: Ban Members");
            noBotPerms.setFooter(defaultFooter);

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        final String reasonForBan = String.join(" ", args.subList(1, args.size()));

        // Ban successful
        EmbedBuilder success = new EmbedBuilder();
        success.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
        success.setTitle(defaultTitle);
        success.setDescription(targetMember.getAsMention() + " was banned.");
        success.setFooter(defaultFooter);

        //  Ban Unsuccessful
        EmbedBuilder failure = new EmbedBuilder();
        failure.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
        failure.setTitle(defaultTitle);
        failure.setDescription("Ban failed.");
        failure.setFooter(defaultFooter);

        ctx.getGuild().ban(targetMember, 0, reasonForBan).reason(reasonForBan).queue(
                (__) -> channel.sendMessageEmbeds(success.build()).queue(),
                (error) -> channel.sendMessageEmbeds(failure.build()).queue()
        );
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getHelp() {
        return "Bans the target player from the server";
    }

    @Override
    public String getUsage() {
        return getName() + " [user] [reason]";
    }
}