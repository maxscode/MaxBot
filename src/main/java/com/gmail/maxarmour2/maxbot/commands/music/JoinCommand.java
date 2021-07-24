package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements ICommand {
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

        if (selfVoiceState.inVoiceChannel()) {
            EmbedBuilder alreadyConnected = new EmbedBuilder();
            alreadyConnected.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            alreadyConnected.setTitle(defaultTitle);
            alreadyConnected.setDescription("I am already connected to a voice channel.");
            alreadyConnected.setFooter(defaultFooter);

            channel.sendMessageEmbeds(alreadyConnected.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder memberNotConnected = new EmbedBuilder();
            memberNotConnected.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            memberNotConnected.setTitle(defaultTitle);
            memberNotConnected.setDescription("You must be connected to a voice channel to invoke this command");
            memberNotConnected.setFooter(defaultFooter);

            channel.sendMessageEmbeds(memberNotConnected.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessage("Connecting to " + memberChannel.getName()).queue();

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