package com.gmail.maxarmour2.maxbot.utils.cmd;

/**
 * A set of methods that are required to add a command to the command manager.
 * @author Max Armour
 * @since 0.1.1-alpha
 */
public interface Command {

    /**
     * The Code that is executed when invoking a command.
     * @param ctx Passes CommandContext variables to be used in the handle method
     */
    void handle(CommandContext ctx);

    /**
     * The argument tells the Command Manager which command to execute.
     * Is usually the name of the command, but it doesn't have to be.
     * @return The name of the command
     */
    String getName();

    /**
     * The description of the command that is shown in the main Help message and in the command help message.
     * @return A short description of the command
     */
    String getHelp();

    /**
     * The command's correct syntax that is shown in the command help messages.
     * @return The command's syntax
     */
    String getUsage();
}
