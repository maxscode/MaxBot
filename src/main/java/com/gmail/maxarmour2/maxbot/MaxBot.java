package com.gmail.maxarmour2.maxbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class MaxBot {

    private static JDA api;

    public static void main(String[] args) throws LoginException {

        api = JDABuilder.createDefault(Config.get("TOKEN")).build();
        api.getPresence().setPresence(OnlineStatus.ONLINE, Activity.watching("MaxBot Development"), false);


    }
}
