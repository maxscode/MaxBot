package com.gmail.maxarmour2.maxbot.utils.cmd;

import com.gmail.maxarmour2.maxbot.commands.*;
import com.gmail.maxarmour2.maxbot.commands.management.*;
import com.gmail.maxarmour2.maxbot.commands.management.moderation.*;

import com.gmail.maxarmour2.maxbot.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));

        // Music Commands
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new RepeatCommand());

        // Moderation Commands
        addCommand(new KickCommand());
        addCommand(new BanCommand());

        // Management Commands
        addCommand(new SetCustomPrefixCommand());
    }

    private void addCommand(Command cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("Command Name in use.");
        }

        commands.add(cmd);
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Nullable
    public Command getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (Command cmd : this.commands) {
            if (cmd.getName().equals(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        Command cmd = this.getCommand(invoke);

        if (cmd != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}