package com.gmail.maxarmour2.maxbot.commands.management.moderation;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

/**
 * A command that bans a specific member or set of members from a guild.
 * @author Max Armour
 * @since 0.1.2-alpha
 */
public class BanCommand implements Command {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());
        final Member selfMember = ctx.getSelfMember();

        // Embed Defaults
        String defaultTitle = "Ban Command";
        String defaultFooter = "TIP: Mention multiple members in the users argument to perform a mass ban.";

        // Checks if the member has correctly invoked the command
        if (message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Missing Arguments.\n Usage: `" + prefix + getUsage() + "`")
                    .setFooter(defaultFooter);

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }


        // Checks if the member who invokes this command does not have the permissions.
        if (!member.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You do not have permission to invoke this command.\nRequired Permission: Ban Members")
                    .setFooter(defaultFooter);

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        // Checks if the bot does not have the permissions to execute the command.
        if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("I do not have permission to execute this command.\nRequired Permission: Ban Members")
                    .setFooter(defaultFooter);

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        // Checks if the user has requested a ban of more than one member argument
        if (message.getMentionedMembers().size() > 1) {

            final List<Member> targetMembers = message.getMentionedMembers();
            final String reasonForBan = args.get(args.size() - 1);

            for (Member targetMember : targetMembers) {

                if (!member.canInteract(targetMember)) {
                    EmbedBuilder targetAboveMember = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("The member `" + targetMember.getAsMention() + "` is above you in the hierarchy and was skipped");

                    channel.sendMessageEmbeds(targetAboveMember.build()).queue();
                    continue;
                }

                if (!selfMember.canInteract(targetMember)) {
                    EmbedBuilder targetAboveBot = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("The member  `" + targetMember.getAsMention() + "`  is above me in the hierarchy and was skipped.");

                    channel.sendMessageEmbeds(targetAboveBot.build()).queue();
                    continue;
                }

                // Ban successful
                EmbedBuilder success = new EmbedBuilder()
                        .setTitle(defaultTitle)
                        .setDescription(targetMember.getAsMention() + " was banned.");

                //  Ban Unsuccessful
                EmbedBuilder failure = new EmbedBuilder()
                        .setTitle(defaultTitle)
                        .setDescription("Ban failed.");

                ctx.getGuild().ban(targetMember, 0, reasonForBan).reason(reasonForBan).queue(
                        (__) -> channel.sendMessageEmbeds(success.build()).queue(),
                        (error) -> channel.sendMessageEmbeds(failure.build()).queue()
                );
            }
            return; // Returns as the mass ban has been completed. No further tasks required.
        }


        // Executed if only one member is targeted for a ban
        final Member targetMember = message.getMentionedMembers().get(1);

        // Checks if the Member has the ability to interact with the targeted member
        if (!member.canInteract(targetMember)) {
            EmbedBuilder targetAboveMember = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("The target member is above you in the hierarchy");

            channel.sendMessageEmbeds(targetAboveMember.build()).queue();
        }

        // Checks if the bot has the ability to interact with the targeted member
        if (!selfMember.canInteract(targetMember)) {
            EmbedBuilder targetAboveBot = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("The target member is above me in the hierarchy");

            channel.sendMessageEmbeds(targetAboveBot.build()).queue();
        }

        final String reasonForBan = String.join(" ", args.subList(1, args.size()));

        // Ban successful
        EmbedBuilder success = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription(targetMember.getAsMention() + " was banned.");

        //  Ban Unsuccessful
        EmbedBuilder failure = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Ban failed.");

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
        return "Bans the target member from the server";
    }

    @Override
    public String getUsage() {
        return getName() + " [user] [reason]";
    }
}