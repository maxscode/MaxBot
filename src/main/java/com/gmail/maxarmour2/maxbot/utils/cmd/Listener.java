package com.gmail.maxarmour2.maxbot.utils.cmd;

import com.gmail.maxarmour2.maxbot.Config;
import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(ReadyEvent event) {
        LOGGER.info("{} is ready!", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = CustomPrefix.PREFIXES.computeIfAbsent(guildId, (id) -> Config.get("PREFIX"));
        String raw = event.getMessage().getContentRaw();

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}
