package com.gmail.maxarmour2.maxbot.commands;

import java.util.List;

public abstract class ICommand {
    public abstract void handle(CommandContext ctx);

    public abstract String getName();

    public List<String> getAliases() {
        return List.of();
    }
}
