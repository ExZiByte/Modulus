package me.exzibyte.modulus.Modules.Moderation;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.exzibyte.modulus.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.PREFIX + "clear")) {
            if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length == 2) {
                    try {
                        Integer messageCount = Integer.parseInt(args[1]);
                        if (messageCount < 2) {
                            eb.setDescription("Too few messages to delete. Minimum amount of messages I can delete is 2");
                            eb.setColor(0xff5555);
                            eb.setFooter(event.getJDA().getSelfUser().getName() + ", Too Few Messages to Delete", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                            eb.setTimestamp(Instant.now());

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                                eb.clear();
                            });
                        } else if (messageCount > 100) {
                            eb.setDescription("Too many messages to delete. Maximum amount of messages I can delete at a time is 100");
                            eb.setColor(0xff5555);
                            eb.setFooter(event.getJDA().getSelfUser().getName() + ", Too Many Messages to Delete", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                            eb.setTimestamp(Instant.now());

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                                eb.clear();
                            });
                        } else {
                            List<Message> messages = event.getChannel().getHistory().retrievePast(messageCount).complete();
                            event.getChannel().deleteMessages(messages).queue();

                            eb.setDescription("Deleted " + messageCount.toString() + " messages from " + event.getChannel().getAsMention());
                            eb.setColor(0x4fff45);
                            eb.setFooter(event.getJDA().getSelfUser().getName() + ", Clear Messages", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                            eb.setTimestamp(Instant.now());
                            
                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                                eb.clear();
                            });
                        }
                    } catch (NumberFormatException nfe) {
                        eb.setDescription("`" + args[1] + "` is not a valid number. Please input a number between 2 and 100");
                        eb.setColor(0xff5555);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + ", Not a valid number",  event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        eb.setTimestamp(Instant.now());
                        
                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                            eb.clear();
                        });
                    }

                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                        + ", You dont have the permission to manage messages for this guild.");
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