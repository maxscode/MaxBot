package com.gmail.maxarmour2.maxbot;

import com.gmail.maxarmour2.maxbot.utils.cmd.Listener;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class MaxBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaxBot.class);

    public static void main(String[] args) throws LoginException {

        JDA api = JDABuilder.createDefault(Config.get("TOKEN")).build();
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("MaxBot Development"), false);
        api.addEventListener(new Listener());


        Scanner consoleCommands = new Scanner(System.in);
        String scanned = consoleCommands.next();

        if (scanned.equals("shutdown")) {
            LOGGER.info("Shutting Down...");
            api.shutdownNow();
            BotCommons.shutdown(api);
        }
    }
}
