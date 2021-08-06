package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@SuppressWarnings("ConstantConditions")
public class PauseCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {
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

        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("Nothing is playing right now").queue();
            return;
        }
        if (musicManager.scheduler.player.isPaused()) {
            channel.sendMessage("The music player is already paused.").queue();
            return;
        }
        musicManager.scheduler.player.setPaused(true);
        channel.sendMessage("The music player was paused.").queue();
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return "Pauses the music player.";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
