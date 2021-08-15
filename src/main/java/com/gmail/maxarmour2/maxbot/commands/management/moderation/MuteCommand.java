package com.gmail.maxarmour2.maxbot.commands.management.moderation;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

/**
 * A command that mutes a specified player from speaking in a guild.
 * @author Max Armour
 * @since 0.1.6-alpha
 */
public class MuteCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());
        final Member selfMember = ctx.getSelfMember();
        final List<Role> muted = ctx.getGuild().getRolesByName("Muted", false);

        // Embed Defaults
        String defaultTitle = "Mute Command";

        // Checks if the member has correctly invoked the command
        if (message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Missing Arguments.\n Usage: `" + prefix + getUsage() + "`");

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(0);

        // Checks if the member has permission to add or remove roles to other members
        if (!member.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(targetMember)) {
            EmbedBuilder noPerms = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You do not have permission to invoke this command.\nRequired Permission: Manage Roles");
            channel.sendMessageEmbeds(noPerms.build()).queue();
            return;
        }

        // Checks if the bot has permission to add or remove roles to other members.
        if (!selfMember.hasPermission(Permission.MANAGE_ROLES) || !member.canInteract(targetMember)) {
            EmbedBuilder noBotPerms = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("I do not have permission to execute this command.\nRequired Permission: Manage Roles");
            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        if (muted.isEmpty()) {
            ctx.getGuild().createRole().setName("Muted").complete();

            EmbedBuilder roleCreated = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("No muted role exists. Role `Muted` was created and has no permissions.\nPlease re-execute this command.");
            channel.sendMessageEmbeds(roleCreated.build()).queue();
            return;
        }

        EmbedBuilder success = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("The member " + targetMember.getAsMention() + " was muted.");

        EmbedBuilder failure = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Mute action failed");

        ctx.getGuild().addRoleToMember(targetMember, muted.get(0)).queue(
                (__) -> channel.sendMessageEmbeds(success.build()).queue(),
                (error) -> channel.sendMessageEmbeds(failure.build()).queue()
        );
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getHelp() {
        return "Mutes the specified member in the guild.";
    }

    @Override
    public String getUsage() {
        return getName() + " [user]";
    }
}
