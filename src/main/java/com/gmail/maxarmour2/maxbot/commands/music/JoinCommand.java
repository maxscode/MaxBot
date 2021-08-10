package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements Command {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        // Embed Defaults
        String defaultTitle = "Music Command";

        if (selfVoiceState.inVoiceChannel()) {
            EmbedBuilder alreadyConnected = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("I am already connected to a voice channel.");

            channel.sendMessageEmbeds(alreadyConnected.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder memberNotConnected = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to a voice channel to invoke this command");

            channel.sendMessageEmbeds(memberNotConnected.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        audioManager.setSelfDeafened(true);

        EmbedBuilder connected = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Connecting to `" + memberChannel.getName() + "`");

        channel.sendMessageEmbeds(connected.build()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Makes the bot join your Voice Channel";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
