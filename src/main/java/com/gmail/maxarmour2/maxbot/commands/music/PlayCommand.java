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

/**
 * A command that adds a track or playlist of tracks to the queue
 * and calls the bot to the members voice channel if not already connected.
 * @author Max Armour
 * @since 0.1.5-alpha
 */
@SuppressWarnings("ConstantConditions")
public class PlayCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());

        // Embed Defaults
        String defaultTitle = "Music Command";

        if (ctx.getArgs().isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Missing Arguments\nUsage: `" + prefix + getUsage() + "`");

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder notConnectedToVC = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to a voice channel to invoke this command");

            channel.sendMessageEmbeds(notConnectedToVC.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if (!selfVoiceState.inVoiceChannel()) {
            audioManager.openAudioConnection(memberChannel);
            audioManager.setSelfDeafened(true);

            EmbedBuilder connecting = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Connecting to `" + memberChannel.getName() + "`");

            channel.sendMessageEmbeds(connecting.build()).queue();
        }

        String query = String.join(" ", ctx.getArgs());
        if (!isUrl(query)) {
            query = "ytsearch:" + query;
        }

        PlayerManager.getInstance().loadAndPlay(channel, query);
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

    /**
     * Checks if a string conforms to a URL syntax.
     * @param url The String being tested
     * @return {@code true} if the param is a URL, otherwise false
     */
    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
