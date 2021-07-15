package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        JDA api = ctx.getJDA();

        long gatePing = api.getGatewayPing();

        //TODO Add REST ping
        EmbedBuilder message = new EmbedBuilder();
        message.setTitle("MaxBot Ping \uD83D\uDCE1");
        message.setDescription("Gateway Ping: `" + gatePing + " ms`");


        if (gatePing < 200) {
            message.setColor(0x3aeb34);
            message.setFooter("Ping is Low!");
        }

        if (gatePing > 200 && gatePing < 500) {
            message.setColor(0xff7700);
            message.setFooter("Ping is OK.");
        }

        if (gatePing > 500) {
            message.setColor(0xff0000);
            message.setFooter("Ping is High!");
        }
        ctx.getChannel().sendMessageEmbeds(message.build()).queue();
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
}
