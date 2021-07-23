package com.gmail.maxarmour2.maxbot.commands.management;

import com.gmail.maxarmour2.maxbot.utils.CustomPrefix;
import com.gmail.maxarmour2.maxbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.maxbot.utils.cmd.ICommand;
import com.gmail.maxarmour2.maxbot.utils.database.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetCustomPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        // Embed Defaults
        String defaultAuthor = ctx.getAuthor().getAsTag();
        String defaultAuthorAvatar = ctx.getAuthor().getAvatarUrl();
        String defaultTitle = "Set Prefix Command";
        String defaultFooter = "MaxBot Server Management";

        if (args.isEmpty()) {
            EmbedBuilder missingArgs = new EmbedBuilder();
            missingArgs.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            missingArgs.setTitle(defaultTitle);
            missingArgs.setDescription("Missing Args\nUsage: `" + getUsage() + "`");
            missingArgs.setFooter(defaultFooter);

            channel.sendMessageEmbeds(missingArgs.build()).queue();
            return;
        }

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder noUserPerms = new EmbedBuilder();
            noUserPerms.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
            noUserPerms.setTitle(defaultTitle);
            noUserPerms.setDescription("You do not have permission to invoke this command.\nRequired Permission: Manage Server");
            noUserPerms.setFooter(defaultFooter);

            channel.sendMessageEmbeds(noUserPerms.build()).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        EmbedBuilder success = new EmbedBuilder();
        success.setAuthor(defaultAuthor, null, defaultAuthorAvatar);
        success.setTitle(defaultTitle);
        success.setDescription("Prefix has been modified to `" + newPrefix + "`");
        success.setFooter(defaultFooter);

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
        return getName() + "[prefix]";
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
