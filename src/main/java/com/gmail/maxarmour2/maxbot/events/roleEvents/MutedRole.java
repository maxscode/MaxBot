package com.gmail.maxarmour2.maxbot.events.roleEvents;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class MutedRole extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        final Message message = event.getMessage();
        final Member member = event.getMember();
        final MessageChannel channel = event.getMessage().getChannel();
        final List<Role> mutedRole = event.getGuild().getRolesByName("Muted", false);

        try {
            assert member != null;
            if (member.getRoles().contains(mutedRole.get(0))) {
                channel.deleteMessageById(message.getIdLong()).queue();
            }
        } catch (NullPointerException ignored) {}
    }
}
