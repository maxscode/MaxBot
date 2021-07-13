package com.gmail.maxarmour2.maxbot.commands;

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
