package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
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

        // Embed Defaults
        String defaultAuthor = ctx.getAuthor().getAsTag();
        String defaultAuthorAvatar = ctx.getAuthor().getAvatarUrl();
        String defaultTitle = "Music Command";
        String defaultFooter = "MaxBot Music Player";

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder memberNotConnected = new EmbedBuilder();
            memberNotConnected.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            memberNotConnected.setTitle(defaultTitle);
            memberNotConnected.setDescription("You must be connected to my current voice channel to invoke this command");
            memberNotConnected.setFooter(defaultFooter);

            channel.sendMessageEmbeds(memberNotConnected.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.scheduler.player.setPaused(true);

        EmbedBuilder playerPaused = new EmbedBuilder();
        playerPaused.setAuthor(defaultAuthor, null, defaultAuthorAvatar)
                .setTitle(defaultTitle)
                .setDescription("The music player was paused.")
                .setFooter(defaultFooter);

        channel.sendMessageEmbeds(playerPaused.build()).queue();
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
