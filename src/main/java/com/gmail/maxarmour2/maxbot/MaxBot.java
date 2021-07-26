package com.gmail.maxarmour2.maxbot;

import com.gmail.maxarmour2.maxbot.utils.cmd.Listener;
import com.gmail.maxarmour2.maxbot.utils.database.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Scanner;

public class MaxBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaxBot.class);

    public static void main(String[] args) throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        JDA api = JDABuilder.createDefault(
                Config.get("TOKEN"),
                GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .disableCache(
                        CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY, CacheFlag.EMOTE)
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("MaxBot Development"), false);
        api.addEventListener(new Listener());

        Scanner consoleCommands = new Scanner(System.in);
        while (consoleCommands.hasNext()) {

            String scanned = consoleCommands.next();

            if (scanned.equals("shutdown")) {

                LOGGER.info("Shutting Down...");
                api.shutdownNow();
                BotCommons.shutdown(api);
                System.exit(0);

            } else {
                LOGGER.info("Invalid Command.");
            }
        }
    }
}
