package listener;

import java.awt.Color;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;


public class GuildMemberListener extends ListenerAdapter {

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event){

        Role role = event.getGuild().getRolesByName("Angeloid Army", true).get(0);
        event.getGuild().addRoleToMember(event.getMember(),role).queue();
        PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
        privateChannel.sendMessage("Welcome to " + event.getGuild().getName() + "!\n\n" +
                "Check `#welcome` for official rules of this server. Have fun!").queue();

    }


    public void onGuildMemberLeave(@Nonnull GuildMemberLeaveEvent event){

    }


    public void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription(event.getUser().getName() + " changed their nickname from **" + event.getOldNickname() + "** to **" + event.getNewNickname() + "**");
        event.getGuild().getTextChannelsByName("botcommands",true).get(0).sendMessage(eb.build()).queue();

    }

    // Still needs big fixes!
    public void onUserUpdateActivityOrder(@Nonnull UserUpdateActivityOrderEvent event){

    }


    public void onUserActivityStart(@Nonnull UserActivityStartEvent event) {

        // will have a role called LIVE
        //Activity.ActivityType.STREAMING.getKey() -> 1
        //Activity.ActivityType.LISTENING.getKey() -> 2
        //Activity.ActivityType.DEFAULT.getKey() -> 0
        //Activity.ActivityType.WATCHING.getKey() -> 3

        if(event.getNewActivity().getType().getKey() == 1) {
            Member member = event.getMember();
            Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
            event.getGuild().addRoleToMember(member, role).queue();
            //System.out.println(member.getEffectiveName() + " is LIVE");
        }

    }


    public void onUserActivityEnd(@Nonnull UserActivityEndEvent event) {

        if(event.getOldActivity().getType().getKey() == 1) {
            Member member = event.getMember();
            Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
            event.getGuild().removeRoleFromMember(member, role).queue();
            //System.out.println(member.getEffectiveName() + " is not LIVE");
        }

    }


    public void onTextChannelUpdatePermissions(@Nonnull TextChannelUpdatePermissionsEvent event){

    }


    /*
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        System.out.println("RECEIVED " + event.getAuthor().getName() + "\n"
            + event.getChannel().getName() + "\n"
            + event.getMessage().getContentRaw() + "\n"
            + event.getMessageId());

        // writing into a file example
        try {
            FileWriter f = new FileWriter("test");
            f.write("WTF");
            f.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
     */

    public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
        /*
        System.out.println("UPDATE " + event.getAuthor().getName() + "\n"
                + event.getChannel().getName() + "\n"
                + event.getMessage().getContentRaw() + "\n"
                + event.getMessageId());
         */
    }

    public void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {
        /*
        System.out.println("DELETE " + event.getChannel().getName() + "\n"
                + event.getMessageId());
         */

    }

    public void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {
        /*
        System.out.println("EMBED " + event.getChannel().getName() + "\n"
                + event.getMessageEmbeds().get(0).getDescription() + "\n"
                + event.getMessageId());
         */
    }

    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        System.out.println("ROLE ADD : " + event.getMember().getNickname());
        //System.out.println(event.getReactionEmote().getEmote().getName());
        //System.out.println(event.getReactionEmote().getAsCodepoints());
        if(event.getMessageIdLong() == 623343073916878918L) {
            if(event.getReactionEmote().getName().equals("💜")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Streamer", true).get(0)).queue();
            }
            if(event.getReactionEmote().getName().equals("❤")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("YouTuber", true).get(0)).queue();
            }
            if(event.getReactionEmote().getName().equals("💚")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("New Jersey", true).get(0)).queue();
            }
        }
    }

    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {
        System.out.println("ROLE REMOVE : " + event.getMember().getNickname());
        //System.out.println(event.getReactionEmote().getName());
        if(event.getMessageIdLong() == 623343073916878918L) {
            if(event.getReactionEmote().getName().equals("💜")) {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName("Streamer", true).get(0)).queue();
            }
            if(event.getReactionEmote().getName().equals("❤")) {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName("YouTuber", true).get(0)).queue();
            }
            if(event.getReactionEmote().getName().equals("💚")) {
                event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName("New Jersey", true).get(0)).queue();
            }
        }
    }

    public void onGuildMessageReactionRemoveAll(@Nonnull GuildMessageReactionRemoveAllEvent event) {
        //System.out.println("ROLE REMOVEALL");
    }

}
