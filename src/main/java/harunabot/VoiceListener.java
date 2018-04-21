package harunabot;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class VoiceListener extends ListenerAdapter {

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        if(! event.getGuild().getName().equals("Rutgers Esports")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getMember().getUser().getName() + " has **joined** " + event.getChannelJoined().getName());
            event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if(! event.getGuild().getName().equals("Rutgers Esports")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getMember().getUser().getName() + " has **left** " + event.getChannelLeft().getName());
            event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();
        }
    }

    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if(! event.getGuild().getName().equals("Rutgers Esports")) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getMember().getUser().getName() + " has **moved** from "
                    + event.getChannelLeft().getName() + " -> " + event.getChannelJoined().getName());
            event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();
        }
    }

}
