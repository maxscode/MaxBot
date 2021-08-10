package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * A command that toggles whether the music player will repeat the currently playing track.
 * @author Max Armour
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class RepeatCommand implements Command {
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
        final boolean repeating = !musicManager.scheduler.repeatEnabled;
        musicManager.scheduler.repeatEnabled = repeating;

        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = playingTrack.getInfo();

        if (repeating) {
            EmbedBuilder repeatOn = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Repeating: `" + info.title + "` by `" + info.author + "`");

            channel.sendMessageEmbeds(repeatOn.build()).queue();
        } else {

            EmbedBuilder repeatOff = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Repeating disabled");

            channel.sendMessageEmbeds(repeatOff.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Loops the currently playing track";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
