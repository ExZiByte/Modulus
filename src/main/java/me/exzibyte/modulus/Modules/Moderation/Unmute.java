package me.exzibyte.modulus.Modules.Moderation;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import me.exzibyte.modulus.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Unmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder unmuted = new EmbedBuilder();

        if(args[0].equalsIgnoreCase(data.PREFIX + "unmute")){
            // Uncomment(remove the //) the next line if you would like the bot to auto delete the command message
            // event.getMessage().delete().queue();
            if(event.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)){
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments \n" + data.PREFIX + "unmute @<member>");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args.length > 1){
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Role muteRole = event.getGuild().getRolesByName("Muted", true).get(0);
                    if (mentioned.getRoles().contains(event.getGuild().getRoleById(muteRole.getId()))) {

                        // Build Information Embed to be sent to kicked user
                        unmuted.setDescription("You've been unmuted on: " + event.getGuild().getName());
                        unmuted.setColor(0xff5555);
                        unmuted.setFooter(event.getJDA().getSelfUser().getName() + " Unmuted",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        unmuted.setTimestamp(Instant.now());

                        // Build Information Embed to be to server channel
                        eb.setDescription(
                                "You've unmuted: " + mentioned.getAsMention());
                        eb.setColor(0x4fff45);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Unmute",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        eb.setTimestamp(Instant.now());

                        mentioned.getUser().openPrivateChannel().queue((channel) -> {
                            channel.sendMessage(unmuted.build()).queue();
                            unmuted.clear();

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                                event.getGuild().getController().removeSingleRoleFromMember(mentioned, muteRole).queue();
                            });
                        });
                    } else {
                        eb.setDescription(mentioned.getAsMention() + " is not muted!");
                        eb.setColor(0xff5555);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Unmute",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        eb.setTimestamp(Instant.now());

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                        });
                    }

                }
            }
        }

    }
}