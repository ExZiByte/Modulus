//Insert Own Package Name
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

public class Ban extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder banned = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.PREFIX + "ban")) {
            if (event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
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

                    banned.setDescription("You've been banned from: " + event.getGuild().getName()
                            + " for: There was no reason specified");
                    banned.setColor(0xff5555);
                    banned.setFooter(event.getJDA().getSelfUser().getName() + " Banned",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    banned.setTimestamp(Instant.now());

                    eb.setDescription("You've banned: " + mentioned.getAsMention() + " for: No reason specified");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Ban",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(banned.build()).queue();
                        banned.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().ban(mentioned, 7, "No Reason Specified").queue();
                        });
                    });

                } else {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

                    banned.setDescription("You've been banned from: " + event.getGuild().getName() + " for: " + reason);
                    banned.setColor(0xff5555);
                    banned.setFooter(event.getJDA().getSelfUser().getName() + " Banned",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    banned.setTimestamp(Instant.now());

                    eb.setDescription("You've banned: " + mentioned.getAsMention() + " for: " + reason);
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Ban",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(banned.build()).queue();
                        banned.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().ban(mentioned, 7, reason).queue();
                        });
                    });

                }
                } else {
                    eb.setDescription(event.getMember().getAsMention()
                            + ", You dont have the permission to ban members from this guild.");
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