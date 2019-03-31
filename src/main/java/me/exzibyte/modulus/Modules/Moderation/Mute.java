package me.exzibyte.modulus.Modules.Moderation;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.exzibyte.modulus.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Mute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder muted = new EmbedBuilder();
        Role muteRole;

        if (args[0].equalsIgnoreCase(data.PREFIX + "mute")) {
            if (event.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length > 1) {

                    List<Role> roles = event.getGuild().getRolesByName("Muted", true);

                    if (roles.size() < 1) {

                        muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff)
                                .setMentionable(false).complete();

                        muteRole.getManager()
                                .revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                                        Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                                        Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                                        Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION)
                                .queue();

                        EnumSet<Permission> denyPermissions = EnumSet.of(Permission.MESSAGE_WRITE);
                        
                        for (Channel channel : event.getGuild().getTextChannels()){
                            channel.getManager().putPermissionOverride(muteRole, null, denyPermissions).queue();
                        }


                        event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you").queue((message) -> {
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                        });

                        Member mentioned = event.getMessage().getMentionedMembers().get(0);

                        // Build Information Embed to be sent to kicked user
                        muted.setDescription("You've been muted on: " + event.getGuild().getName()
                                + " for: There was no reason specified");
                        muted.setColor(0xff5555);
                        muted.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        muted.setTimestamp(Instant.now());

                        // Build Information Embed to be to server channel
                        eb.setDescription("You've muted: " + mentioned.getAsMention() + " for: No reason specified");
                        eb.setColor(0x4fff45);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Kick",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        eb.setTimestamp(Instant.now());

                        mentioned.getUser().openPrivateChannel().queue((channel) -> {
                            channel.sendMessage(muted.build()).queue();
                            muted.clear();

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                                event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                            });
                        });
                    } else {
                        
                        muteRole = event.getGuild().getRolesByName("Muted", true).get(0);
                        EnumSet<Permission> denyPermissions = EnumSet.of(Permission.MESSAGE_WRITE);

                        for (Channel channel : event.getGuild().getTextChannels()){
                            channel.getManager().putPermissionOverride(muteRole, null, denyPermissions).queue();
                        }

                        Member mentioned = event.getMessage().getMentionedMembers().get(0);

                        // Build Information Embed to be sent to kicked user
                        muted.setDescription("You've been muted on: " + event.getGuild().getName()
                                + " for: There was no reason specified");
                        muted.setColor(0xff5555);
                        muted.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        muted.setTimestamp(Instant.now());

                        // Build Information Embed to be to server channel
                        eb.setDescription("You've muted: " + mentioned.getAsMention() + " for: No reason specified");
                        eb.setColor(0x4fff45);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Kick",
                                event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                        eb.setTimestamp(Instant.now());

                        mentioned.getUser().openPrivateChannel().queue((channel) -> {
                            channel.sendMessage(muted.build()).queue();
                            muted.clear();

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                                event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                            });
                        });
                    }

                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                        + ", You dont have the permission to mute members on this guild.");
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