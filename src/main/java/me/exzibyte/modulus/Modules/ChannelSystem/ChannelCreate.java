package me.exzibyte.modulus.Modules.ChannelSystem;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

import me.exzibyte.modulus.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelCreate extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        Boolean nsfwBool;
        if (args[0].equalsIgnoreCase(data.PREFIX + "createpc")) {
            if (args.length < 2) {
                
                EnumSet<Permission> pAllow = EnumSet.of(Permission.PRIORITY_SPEAKER, Permission.VOICE_USE_VAD, Permission.VOICE_SPEAK, Permission.VOICE_MUTE_OTHERS, Permission.VOICE_DEAF_OTHERS, Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL, Permission.MESSAGE_ADD_REACTION);
                EnumSet<Permission> pTextAllow = EnumSet.of(Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
                EnumSet<Permission> pDeny = EnumSet.of(Permission.BAN_MEMBERS, Permission.CREATE_INSTANT_INVITE, Permission.KICK_MEMBERS, Permission.MANAGE_CHANNEL, Permission.MANAGE_EMOTES, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_ROLES, Permission.MANAGE_SERVER, Permission.MANAGE_WEBHOOKS);
                EnumSet<Permission> pTextDenyEveryone = EnumSet.of(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_READ, Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE); 
                EnumSet<Permission> pVoiceDenyEveryone = EnumSet.of(Permission.PRIORITY_SPEAKER, Permission.VOICE_CONNECT);

                List<Category> categoryPrivate = event.getGuild().getCategoriesByName("Private Channels", true);
                if (categoryPrivate.size() < 1) {
                    event.getGuild().getController().createCategory("Private Channels").queue();
                    event.getGuild().getController().createTextChannel(event.getMember().getUser().getName().toString() + "-private-text-channel").queue((channel) -> {
                                channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                                channel.getManager().setTopic("{channelOwner}'s Private Text Channel \n\nOwner: {channelOwner} \nInvited:\n\n\nTo invite users the owner of the channel must run the command " + data.PREFIX + "privateinvite @<member>".replace("{channelOwner}", event.getMember().getUser().getName()));
                                channel.getManager().setNSFW(false);
                                channel.getManager().putPermissionOverride(event.getMember(), pTextAllow, pDeny).queue();
                                channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pTextDenyEveryone).queue();
                            });
                    event.getGuild().getController().createVoiceChannel(event.getMember().getUser().getName() + "'s Private Voice Channel").queue((channel) -> {
                                channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                                channel.getManager().setUserLimit(1);
                                channel.getManager().putPermissionOverride(event.getMember(), pAllow, pDeny).queue();
                                channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pVoiceDenyEveryone).queue();
                            });
                } else {
                    event.getGuild().getController().createTextChannel(event.getMember().getUser().getName().toString() + "-private-text-channel").queue((channel) -> {
                        channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                        channel.getManager().setTopic(event.getMember().getUser().getName() + "'s Private Text Channel \n\nOwner: " + event.getMember().getUser().getName() + "\nInvited:\n\n\nTo invite users the owner of the channel must run the command " + data.PREFIX + "privateinvite @<member>");
                        channel.getManager().setNSFW(false);
                        channel.getManager().putPermissionOverride(event.getMember(), pTextAllow, pDeny).queue();
                        channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pTextDenyEveryone).queue();
                    });
            event.getGuild().getController().createVoiceChannel(event.getMember().getUser().getName() + "'s Private Voice Channel").queue((channel) -> {
                        channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                        channel.getManager().setUserLimit(1);
                        channel.getManager().putPermissionOverride(event.getMember(), pAllow, pDeny).queue();
                        channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pVoiceDenyEveryone).queue();
                    });
                }

                eb.setDescription(event.getMember().getAsMention() + ", I created a set of private text and voice channels for you\nText Channel Name: " + event.getMember().getUser().getName().toLowerCase() + "s-private-text-channel\nVoice Channel Name: " + event.getMember().getUser().getName() + "'s Private Voice Channel");
                        eb.setColor(0x4fff45);
                eb.setFooter(event.getJDA().getSelfUser().getName() + " Private Channel System", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                eb.setTimestamp(Instant.now());

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    eb.clear();
                });
            } else {
                if (args[1].equalsIgnoreCase("-nsfw")) {
                    nsfwBool = true;
                } else {
                    nsfwBool = false;
                }

                EnumSet<Permission> pAllow = EnumSet.of(Permission.PRIORITY_SPEAKER, Permission.VOICE_USE_VAD, Permission.VOICE_SPEAK, Permission.VOICE_MUTE_OTHERS, Permission.VOICE_DEAF_OTHERS, Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL, Permission.MESSAGE_ADD_REACTION);
                EnumSet<Permission> pTextAllow = EnumSet.of(Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
                EnumSet<Permission> pDeny = EnumSet.of(Permission.BAN_MEMBERS, Permission.CREATE_INSTANT_INVITE, Permission.KICK_MEMBERS, Permission.MANAGE_CHANNEL, Permission.MANAGE_EMOTES, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_ROLES, Permission.MANAGE_SERVER, Permission.MANAGE_WEBHOOKS);
                EnumSet<Permission> pTextDenyEveryone = EnumSet.of(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_EXT_EMOJI, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_READ, Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE, Permission.VIEW_CHANNEL); 
                EnumSet<Permission> pVoiceDenyEveryone = EnumSet.of(Permission.PRIORITY_SPEAKER, Permission.VOICE_CONNECT, Permission.VIEW_CHANNEL);

                List<Category> categoryPrivate = event.getGuild().getCategoriesByName("Private Channels", true);
                if (categoryPrivate.size() < 1) {
                    event.getGuild().getController().createCategory("Private Channels").queue();
                    event.getGuild().getController().createTextChannel(event.getMember().getUser().getName().toString() + "-private-text-channel").queue((channel) -> {
                                channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                                channel.getManager().setTopic(event.getMember().getUser().getName() + "'s Private Text Channel \n\nOwner: " + event.getMember().getUser().getName() + "\nInvited:\n\n\nTo invite users the owner of the channel must run the command " + data.PREFIX + "privateinvite @<member>");
                                channel.getManager().setNSFW(nsfwBool);
                                channel.getManager().putPermissionOverride(event.getMember(), pTextAllow, pDeny).queue();
                                channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pTextDenyEveryone).queue();
                            });
                    event.getGuild().getController().createVoiceChannel(event.getMember().getUser().getName() + "'s Private Voice Channel").queue((channel) -> {
                                channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                                channel.getManager().setUserLimit(1);
                                channel.getManager().putPermissionOverride(event.getMember(), pAllow, pDeny).queue();
                                channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pVoiceDenyEveryone).queue();
                            });
                } else {
                    event.getGuild().getController().createTextChannel(event.getMember().getUser().getName().toString() + "-private-text-channel").queue((channel) -> {
                        channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                        channel.getManager().setTopic("{channelOwner}'s Private Text Channel \n\nOwner: {channelOwner} \nInvited:\n\n\nTo invite users the owner of the channel must run the command " + data.PREFIX + "privateinvite @<member>".replace("{channelOwner}", event.getMember().getUser().getName()));
                        channel.getManager().setNSFW(nsfwBool);
                        channel.getManager().putPermissionOverride(event.getMember(), pTextAllow, pDeny).queue();
                        channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pTextDenyEveryone).queue();
                    });
            event.getGuild().getController().createVoiceChannel(event.getMember().getUser().getName() + "'s Private Voice Channel").queue((channel) -> {
                        channel.getManager().setParent(event.getGuild().getCategoriesByName("Private Channels", true).get(0));
                        channel.getManager().setUserLimit(1);
                        channel.getManager().putPermissionOverride(event.getMember(), pAllow, pDeny).queue();
                        channel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, pVoiceDenyEveryone).queue();
                    });
                }

                eb.setDescription(event.getMember().getAsMention() + ", I created a set of private text and voice channels for you\nText Channel Name: " + event.getMember().getUser().getName().toLowerCase() + "s-private-text-channel\nVoice Channel Name: " + event.getMember().getUser().getName() + "'s Private Voice Channel");
                eb.setColor(0x4fff45);
                eb.setFooter(event.getJDA().getSelfUser().getName() + " Private Channel System", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                eb.setTimestamp(Instant.now());

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    eb.clear();
                });
            }
        }
    }

}