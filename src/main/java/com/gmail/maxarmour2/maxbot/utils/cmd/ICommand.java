package com.gmail.maxarmour2.maxbot.utils.cmd;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;

import java.util.List;

public interface ICommand {

    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    String getUsage();

    default List<String> getAliases() {
        return List.of();
    }
}