// Insert own package name
package me.exzibyte.modulus.Modules.Moderation;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import me.exzibyte.modulus.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Kick extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder kicked = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.PREFIX + "kick")) {
            // Uncomment(remove the //) the next line if you would like the bot to auto delete the command message
            // event.getMessage().delete().queue();
            if (event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length < 3) {

                    Member mentioned = event.getMessage().getMentionedMembers().get(0);

                    // Build Information Embed to be sent to kicked user
                    kicked.setDescription("You've been kicked from: " + event.getGuild().getName()
                            + " for: There was no reason specified");
                    kicked.setColor(0xff5555);
                    kicked.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    kicked.setTimestamp(Instant.now());

                    // Build Information Embed to be to server channel
                    eb.setDescription("You've kicked: " + mentioned.getAsMention() + " for: No reason specified");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Kick",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(kicked.build()).queue();
                        kicked.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().kick(mentioned).queue();
                        });
                    });

                } else {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

                    kicked.setDescription("You've been kicked from: " + event.getGuild().getName() + " for: " + reason);
                    kicked.setColor(0xff5555);
                    kicked.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    kicked.setTimestamp(Instant.now());

                    eb.setDescription("You've kicked: " + mentioned.getAsMention() + " for: " + reason);
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Kick",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(kicked.build()).queue();
                        kicked.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().kick(mentioned).queue();
                        });
                    });

                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                        + ", You dont have the permission to kick members from this guild.");
                eb.setColor(0xff5555);
                eb.setFooter("Insufficient Permissions", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                eb.setTimestamp(Instant.now());
                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                    eb.clear();
                });
            }
        }
    }

}