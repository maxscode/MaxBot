package com.gmail.maxarmour2.maxbot.utils.cmd;

import java.util.List;

public interface Command {

    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    String getUsage();
}
