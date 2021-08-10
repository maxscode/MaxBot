package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * A command that skips the currently playing track.
 * @author Max Armour
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class SkipCommand implements Command {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Embed Defaults
        String defaultTitle = "Music Command";

        if (!memberVoiceState.inVoiceChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder noConnectionToVC = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to my current voice channel to invoke this command");
            channel.sendMessageEmbeds(noConnectionToVC.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            EmbedBuilder nothingPlaying = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Nothing is playing right now");

            channel.sendMessageEmbeds(nothingPlaying.build()).queue();
            return;
        }
        musicManager.scheduler.nextTrack();
        EmbedBuilder trackSkipped = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("The track was skipped");

        channel.sendMessageEmbeds(trackSkipped.build()).queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skips the current track.";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
