package com.gmail.maxarmour2.maxbot.commands;

import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA api = ctx.getJDA();

        api.getRestPing().queue((ping) -> ctx.getChannel().sendMessageFormat("Reset Ping: %sms\n WS Ping: %sms", ping, api.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }
}
