package harunabot;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.json.JSONArray;
import org.json.JSONException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Role;


public class GuildMemberListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){

        Role role = event.getGuild().getRolesByName("Angeloid Army", true).get(0);
        event.getGuild().addRoleToMember(event.getMember(),role).queue();
        PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
        privateChannel.sendMessage("Welcome to " + event.getGuild().getName() + "!\n\n" +
                "Check `#welcome` for official rules of this server and add your roles in `#botcommands` using the `!addrole` command.\n\n" +
                "Thank you!").queue();

    }


    public void onGuildMemberLeave(GuildMemberLeaveEvent event){

    }


    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        if(! event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            if(event.getOldNickname().equals(null)) {
                eb.setDescription(event.getUser().getName() + " changed their nickname to **" + event.getNewNickname() + "**");
            }else {
                eb.setDescription(event.getUser().getName() + " changed their nickname from **" + event.getOldNickname() + "** to **" + event.getNewNickname() + "**");
            }
            event.getGuild().getTextChannelsByName("botcommands",true).get(0).sendMessage(eb.build()).queue();
        }
    }

    // Still needs big fixes!
    public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event){
        //EmbedBuilder eb = new EmbedBuilder();
        //Game game = event.getNewGame();
        //Game prevGame = event.getOldGame();
        List<Activity> activityList = event.getNewValue();
        List<Activity> oldActivityList = event.getOldValue();
        Member member = event.getMember();

        // if User begins STREAMING
        if(activityList.get(0) != null && activityList.get(0).getType().toString().equals("STREAMING")) {
            //eb.setColor(Color.MAGENTA);
            //event.getCurrentGame().getType()
            //eb.setAuthor(event.getUser().getName() + " is now live!", null, event.getUser().getAvatarUrl());
            //eb.setDescription(game.getName() + "\n" + game.getUrl());

            if(event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
                // DO STUFF
                for(int i = 0; i < member.getRoles().size(); i++){
                    Role role = member.getRoles().get(i);
                    if(role.getName().equalsIgnoreCase("Rutgers Student")
                            || role.getName().equalsIgnoreCase("Rutgers Alumni")){
                        try{
                            //GuildController gc = new GuildController(event.getGuild());
                            // will have a role called LIVE
                            Role liveRole = event.getGuild().getRolesByName("LIVE", true).get(0);
                            //gc.addSingleRoleToMember(member, liveRole).queue();
                            event.getGuild().addRoleToMember(member, liveRole).queue();
                            //System.out.println(member.getEffectiveName() + " is LIVE");
                        }catch(HierarchyException e) {

                        }
                        return;
                    }
                }
                // no LIVE role for you
                return;

            }else {
                //event.getGuild().getTextChannelsByName("self_promotion", true).get(0).sendMessage(eb.build()).queue();
                //GuildController gc = new GuildController(event.getGuild());
                // will have a role called LIVE
                Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
                //gc.addSingleRoleToMember(member, role).queue();
                event.getGuild().addRoleToMember(member, role).queue();
                //System.out.println(member.getEffectiveName() + " is LIVE");
                return;
            }
        }
        // check if User stops STREAMING
        if(oldActivityList.get(0) == null){
            return;
        }
        if(oldActivityList.get(0).getType().toString().equals("STREAMING")){
            if(event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
                // DO STUFF
                try{
                    //GuildController gc = new GuildController(event.getGuild());
                    // will have a role called LIVE
                    Role liveRole = event.getGuild().getRolesByName("LIVE", true).get(0);
                    //gc.removeSingleRoleFromMember(member, liveRole).queue();
                    event.getGuild().removeRoleFromMember(member, liveRole).queue();
                    //System.out.println(member.getEffectiveName() + " is not LIVE");
                }catch(HierarchyException e) {

                }
                return;
            }else{
                // DO STUFF
                //GuildController gc = new GuildController((event.getGuild()));
                Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
                //gc.removeSingleRoleFromMember(member, role).queue();
                event.getGuild().removeRoleFromMember(member, role).queue();
                //System.out.println(member.getEffectiveName() + " is not LIVE");
                return;
            }
        }

    }


    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event){

    }

}
