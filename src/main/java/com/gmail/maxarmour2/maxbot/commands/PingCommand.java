package com.gmail.maxarmour2.maxbot.commands;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

/**
 * Outputs the current Gateway ping to the members text channel.
 * @author Max Armour
 * @since 0.1.1-alpha
 */
public class PingCommand implements Command {

    // TODO: 10-08-2021  Add REST ping to the output.
    @Override
    public void handle(CommandContext ctx) {

        JDA api = ctx.getJDA();

        long gatePing = api.getGatewayPing();

        // Embed Defaults
        String defaultTitle = "MaxBot Ping";

        EmbedBuilder output = new EmbedBuilder();
        output.setTitle(defaultTitle);
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
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Shows the current ping";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
