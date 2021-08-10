package com.gmail.maxarmour2.maxbot.commands.management;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.Command;
import com.gmail.maxarmour2.maxbot.utils.database.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * A command that alters the custom prefix for a guild, and saves it to a database for further reference.
 * @author Max Armour
 * @since 0.1.3-alpha
 */
public class SetCustomPrefixCommand implements Command {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        String prefix = CustomPrefix.PREFIXES.get(ctx.getGuild().getIdLong());

        // Embed Defaults
        String defaultTitle = "Set Prefix Command";

        if (args.isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Missing Args\nUsage: `" + prefix + getUsage() + "`");

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder noUserPerms = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You do not have permission to invoke this command.\nRequired Permission: Manage Server");

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Prefix has been modified to `" + newPrefix + "`");

        channel.sendMessageEmbeds(success.build()).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server.";
    }

    @Override
    public String getUsage() {
        return getName() + " [prefix]";
    }

    private void updatePrefix(long guildId, String newPrefix) {
        CustomPrefix.PREFIXES.put(guildId, newPrefix);

        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {
            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
