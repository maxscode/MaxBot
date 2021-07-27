package com.gmail.maxarmour2.maxbot.commands.music;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {

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

        String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            missingArgs.setTitle(defaultTitle);
            missingArgs.setDescription("Missing Arguments\nUsage: `" + prefix + getUsage() + "`");
            missingArgs.setFooter(defaultFooter);

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

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

        if (!selfVoiceState.inVoiceChannel()) {
            audioManager.openAudioConnection(memberChannel);
            EmbedBuilder connected = new EmbedBuilder();
            connected.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            connected.setTitle(defaultTitle);
            connected.setDescription("Connecting to `" + memberChannel.getName() + "`");
            connected.setFooter(defaultFooter);

            channel.sendMessageEmbeds(connected.build()).queue();
        }

        String query = String.join(" ", ctx.getArgs());
        if (!isUrl(query)) {
            query = "ytsearch:" + query;
        }

        PlayerManager.getInstance().loadAndPlay(channel, query, ctx);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Adds a song to the queue.";
    }

    @Override
    public String getUsage() {
        return getName() + " [URL/Search]";
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}