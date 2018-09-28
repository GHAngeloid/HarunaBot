package harunabot;

import java.awt.Color;
import java.io.IOException;

import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.managers.GuildController;
import org.json.JSONArray;
import org.json.JSONException;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
//import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.entities.Role;


public class GuildMemberListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){

        if(event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
            PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
            privateChannel.sendMessage("Welcome to " + event.getGuild().getName() + "!\n\n" +
                    "Check `#welcome` for official rules of this server and add your roles in `#botcommands` using the `!addrole` command. " +
                    "Some roles include `Rutgers Student`, `Rutgers Alumni`, and `Guest`. Selecting one of these roles allows you to chat in " +
                    "all of the channels on the server.\n\n" +
                    "Thank you!").queue();
        }else {
            GuildController gc = new GuildController(event.getGuild());
            Role role = event.getGuild().getRolesByName("Angeloid Army", true).get(0);
            gc.addSingleRoleToMember(event.getMember(), role).queue();
            PrivateChannel privateChannel = event.getUser().openPrivateChannel().complete();
            privateChannel.sendMessage("Welcome to " + event.getGuild().getName() + "!\n\n" +
                    "Check `#welcome` for official rules of this server and add your roles in `#botcommands` using the `!addrole` command.\n\n" +
                    "Thank you!").queue();
        }
        // keeping both servers separated in case of future message edits
    }


    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        if(event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
            //event.getGuild().getTextChannels().get(0).sendMessage(event.getUser().getName()+" has left").queue();
            event.getGuild().getTextChannelsByName("audit", true)
                    .get(0).sendMessage(event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " has left").queue();
        }
    }


	/* DEPRECATED
	public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
		//System.out.println(event.getUser().getName()+" went "+event.getPreviousOnlineStatus().name());
		Member current=event.getGuild().getMember(event.getUser());
		for(int i=0;i<current.getRoles().size();i++) {
			if(current.getRoles().get(i).getName().equals("Mods")||current.getRoles().get(i).getName().equals("Twitch Subscriber")) {
				break;
			}
			if(i+1==current.getRoles().size()) {
				return;
			}
		}

		String status="";
		EmbedBuilder eb = new EmbedBuilder();
		if(current.getOnlineStatus().name().equals("ONLINE")) {
			status="**online**";
			eb.setColor(Color.GREEN);
		}else if(current.getOnlineStatus().name().equals("OFFLINE")) {
			status="**offline**";
			eb.setColor(Color.RED);
		}else {
			return;
		}
		eb.setDescription(event.getUser().getName()+" is "+status);
		//System.out.println(event.getUser().getName()+" is "+event.getGuild().getMember(event.getUser()).getOnlineStatus());
		//event.getGuild().getTextChannels().get(9).sendMessage(eb.build()).queue();
		event.getGuild().getTextChannelsByName("botcommands",true).get(0).sendMessage(eb.build()).queue();
	}
	*/

    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        if(! event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            if(event.getPrevNick().equals(null)) {
                eb.setDescription(event.getUser().getName() + " changed their nickname to **" + event.getNewNick() + "**");
            }else {
                eb.setDescription(event.getUser().getName() + " changed their nickname from **" + event.getPrevNick() + "** to **" + event.getNewNick() + "**");
            }
            event.getGuild().getTextChannelsByName("botcommands",true).get(0).sendMessage(eb.build()).queue();
        }
    }

    // Still needs big fixes!
    public void onUserUpdateGame(UserUpdateGameEvent event){
        //EmbedBuilder eb = new EmbedBuilder();
        Game game = event.getNewGame();
        Game prevGame = event.getOldGame();
        Member member = event.getMember();

        // if User begins STREAMING
        if(game != null && game.getType().toString().equals("STREAMING")) {
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
                            GuildController gc = new GuildController(event.getGuild());
                            // will have a role called LIVE
                            Role liveRole = event.getGuild().getRolesByName("LIVE", true).get(0);
                            gc.addSingleRoleToMember(member, liveRole).queue();
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
                GuildController gc = new GuildController(event.getGuild());
                // will have a role called LIVE
                Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
                gc.addSingleRoleToMember(member, role).queue();
                //System.out.println(member.getEffectiveName() + " is LIVE");
                return;
            }
        }
        // check if User stops STREAMING
        if(prevGame == null){
            return;
        }
        if(prevGame.getType().toString().equals("STREAMING")){
            if(event.getGuild().getId().equals(Reference.PUBLICGUILDID)) {
                // DO STUFF
                try{
                    GuildController gc = new GuildController(event.getGuild());
                    // will have a role called LIVE
                    Role liveRole = event.getGuild().getRolesByName("LIVE", true).get(0);
                    gc.removeSingleRoleFromMember(member, liveRole).queue();
                    //System.out.println(member.getEffectiveName() + " is not LIVE");
                }catch(HierarchyException e) {

                }
                return;
            }else{
                // DO STUFF
                GuildController gc = new GuildController((event.getGuild()));
                Role role = event.getGuild().getRolesByName("LIVE", true).get(0);
                gc.removeSingleRoleFromMember(member, role).queue();
                //System.out.println(member.getEffectiveName() + " is not LIVE");
                return;
            }
        }

    }

    /*
    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event){
        event.getGuild().getTextChannelsByName("audit", true)
                .get(0).sendMessage("").queue();
    }
    */

}
