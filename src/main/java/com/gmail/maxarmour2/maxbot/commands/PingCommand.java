package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.Config;
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
            output.setFooter("Ping is Low!");
        }

        if (gatePing > 200 && gatePing < 500) {
            output.setColor(0xff7700);
            output.setFooter("Ping is OK.");
        }

        if (gatePing > 500) {
            output.setColor(0xff0000);
            output.setFooter("Ping is High!");
        }
        ctx.getChannel().sendMessageEmbeds(output.build()).queue();
    }

    @Override
    public String getHelp() {
        return "Shows the Bot's current ping in milliseconds.";
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage() {
        return "Usage: `" + Config.get("PREFIX") + "ping`";
    }

    @Override
    public String getHelpCommand() {
        return "`" + Config.get("PREFIX") + "ping`";
    }
}
