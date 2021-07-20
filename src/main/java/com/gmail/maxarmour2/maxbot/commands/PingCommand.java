package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        JDA api = ctx.getJDA();

        //TODO Add REST ping
        long gatePing = api.getGatewayPing();

        EmbedBuilder output = new EmbedBuilder();
        output.setAuthor("MaxBot Ping \uD83D\uDCE1", null, api.getSelfUser().getAvatarUrl());
        output.setDescription("Gateway Ping: `" + gatePing + " ms`");


        if (gatePing < 200) {
            output.setColor(0x3aeb34);
            output.setFooter("Ping is Low!\nCommand invoked by " + ctx.getAuthor().getAsTag());
        }

        if (gatePing > 200 && gatePing < 500) {
            output.setColor(0xff7700);
            output.setFooter("Ping is OK.\nCommand invoked by " + ctx.getAuthor().getAsTag());
        }

        if (gatePing > 500) {
            output.setColor(0xff0000);
            output.setFooter("Ping is High!\nCommand invoked by " + ctx.getAuthor().getAsTag());
        }
        ctx.getChannel().sendMessageEmbeds(output.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows the current ping";
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
