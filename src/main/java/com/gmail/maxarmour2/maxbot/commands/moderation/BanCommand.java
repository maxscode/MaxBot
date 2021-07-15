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

public class BanCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        // Missing Arguments Message.
        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor("Ban Command", null, ctx.getSelfUser().getAvatarUrl());
            missingArgs.setDescription("Missing Arguments.\n" + getUsage());
            missingArgs.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(1);

        // Executed if the member who invokes this command does not have the permissions.
        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor("Ban Command", null, ctx.getSelfUser().getAvatarUrl());
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Ban Members");
            noUserPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        // Executed if the bot does not have the permissions to execute the command.
        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.BAN_MEMBERS)) {
            EmbedBuilder noBotPerms = new EmbedBuilder();
            noBotPerms.setAuthor("Ban Command", null, ctx.getSelfUser().getAvatarUrl());
            noBotPerms.setDescription("I do not have permission to execute this command.\nRequired Permission: Ban Members");
            noBotPerms.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

            channel.sendMessageEmbeds(noBotPerms.build()).queue();
            return;
        }

        final String reasonForBan = String.join(" ", args.subList(1, args.size()));

        // Ban successful
        EmbedBuilder success = new EmbedBuilder();
        success.setAuthor("Ban Command", null, ctx.getSelfUser().getAvatarUrl());
        success.setDescription(targetMember.getAsMention() + " was banned");
        success.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

        //  Ba Unsuccessful
        EmbedBuilder failure = new EmbedBuilder();
        failure.setAuthor("Ban Command", null, ctx.getSelfUser().getAvatarUrl());
        failure.setDescription("Ban failed.");
        failure.setFooter("Command invoked by " + ctx.getAuthor().getAsTag());

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
        return "Bans the target player from the server. Requires Ban permission to invoke";
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