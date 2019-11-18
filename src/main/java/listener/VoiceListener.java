package listener;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

import javax.annotation.Nonnull;

/**
 * Voice Listener
 */
public class VoiceListener extends ListenerAdapter {

    /**
     * Member joins VC.
     * @param event GuildVoiceJoinEvent
     */
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription(event.getMember().getUser().getName() + " has **joined** " + event.getChannelJoined().getName());
        event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();

    }

    /**
     * Member leaves VC.
     * @param event GuildVoiceLeaveEvent
     */
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription(event.getMember().getUser().getName() + " has **left** " + event.getChannelLeft().getName());
        event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();

    }

    /**
     * Member moves to another VC.
     * @param event GuildVoiceMoveEvent
     */
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription(event.getMember().getUser().getName() + " has **moved** from "
                + event.getChannelLeft().getName() + " -> " + event.getChannelJoined().getName());
        event.getGuild().getTextChannelsByName("voice", true).get(0).sendMessage(eb.build()).queue();

    }

}
