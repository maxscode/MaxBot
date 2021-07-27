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

@SuppressWarnings("ConstantConditions")
public class NowPlayingCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {

        // Embed Defaults
        String defaultAuthor = ctx.getAuthor().getAsTag();
        String defaultAuthorAvatar = ctx.getAuthor().getAvatarUrl();
        String defaultTitle = "Music Command";
        String defaultFooter = "MaxBot Music Player";

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("You must be connected to my current voice channel to invoke this command").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (playingTrack == null) {
            channel.sendMessage("There is nothing playing at the moment.").queue();
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();

        EmbedBuilder nowPlaying = new EmbedBuilder();
        nowPlaying.setAuthor(defaultAuthor, null, defaultAuthorAvatar)
                .setTitle(defaultTitle)
                .setDescription("Now Playing:\n `" + info.title + " ` by `" + info.author + "`" +
                        "\n Link: " + info.uri + "" +
                        "\nIn Voice Channel: `" + selfVoiceState.getChannel().getName() + "`")
                .setFooter(defaultFooter);
        channel.sendMessageEmbeds(nowPlaying.build()).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Shows the track currently being played";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
