package com.gmail.maxarmour2.maxbot.events.roleEvents;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A class that deletes a message if the author has the Muted Role.
 *
 * @see com.gmail.maxarmour2.maxbot.commands.management.moderation.MuteCommand
 * @see com.gmail.maxarmour2.maxbot.commands.management.moderation.UnmuteCommand
 *
 *
 * @since 1.0.1
 * @author Max Armour
 */
public class MutedRole extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        final Message message = event.getMessage();
        final Member member = event.getMember();
        final MessageChannel channel = event.getMessage().getChannel();
        final List<Role> mutedRole = event.getGuild().getRolesByName("Muted", false);


        try {
            // Checks if the author of any message that is sent has the Muted Role. Deletes the message if true.
            assert member != null;
            if (member.getRoles().contains(mutedRole.get(0))) {
                channel.deleteMessageById(message.getIdLong()).queue();
            }
        } catch (NullPointerException ignored) {
            // TODO Find solution for why NullPointerException is thrown but the desired outcome is achieved.
        }
    }
}
