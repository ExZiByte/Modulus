package me.exzibyte.modulus.Modules.ChannelSystem;

import java.util.List;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelCleanup extends ListenerAdapter {

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
        List<Member> membersPrivChannel = event.getChannelLeft().getMembers();
        if(event.getChannelLeft().getParent().equals(event.getGuild().getCategoriesByName("Private Channels", true).get(0)) && membersPrivChannel.size() < 1){
           event.getChannelLeft().getManager().getChannel().delete().queue();
           String voiceChannelName = event.getChannelLeft().getName().toLowerCase().replace("'", "").replace(" ", "-").replace("private-voice-channel", "private-text-channel");
           event.getGuild().getTextChannelsByName(voiceChannelName, true).get(0).delete().queue();
        }
    }
}