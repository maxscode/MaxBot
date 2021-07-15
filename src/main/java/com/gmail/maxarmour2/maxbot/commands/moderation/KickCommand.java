package com.gmail.maxarmour2.maxbot.commands.moderation;

import com.gmail.maxarmour2.maxbot.Config;
import com.gmail.maxarmour2.maxbot.commands.CommandContext;
import com.gmail.maxarmour2.maxbot.commands.ICommand;
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

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing arguments.\n" + getUsage()).queue();
            return;
        }

        final Member targetMember = message.getMentionedMembers().get(0);

        if (!member.canInteract(targetMember) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You do not have permission to invoke this command").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(targetMember) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I don't have permission to kick members.").queue();
            return;
        }

        final String reasonForKick =  String.join(" ", args.subList(1, args.size()));

        ctx.getGuild().kick(targetMember, reasonForKick).reason(reasonForKick).queue(
                (__) -> channel.sendMessage(targetMember.getAsMention() + " was kicked.").queue(),
                (error) -> channel.sendMessageFormat("Kick failed", error.getMessage()).queue()
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
        return "Usage: `" + Config.get("PREFIX") + "kick [player] [reason]`";
    }

    @Override
    public String getHelpCommand() {
        return "`" + Config.get("PREFIX") + "kick [player] [reason]`";
    }
}
