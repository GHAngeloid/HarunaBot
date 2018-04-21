package harunabot;

import java.awt.Color;
//import java.io.IOException;

//import org.json.JSONArray;
//import org.json.JSONException;

import net.dv8tion.jda.core.EmbedBuilder;
//import net.dv8tion.jda.core.entities.Game;
//import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
//import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
//import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class GuildMemberListener extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        //GuildController guild=new GuildController(j.getGuild());
        if(event.getGuild().getName().equals("Rutgers Esports")) {
            //event.getGuild().getTextChannelsByName("introductions", true).get(0).sendMessage("Welcome to Rutgers Esports "
                    //+ event.getUser().getAsMention() + "! Check #welcome for official rules of this server.").queue();
            PrivateChannel priv = event.getUser().openPrivateChannel().complete();
            priv.sendMessage("Welcome to " + event.getGuild().getName() + "! Check #welcome for official rules of this server and add your roles in #botcommands.").queue();
        }else {
            //event.getGuild().getTextChannels().get(0).sendMessage("Welcome to GHAngeloid's Discord Server "+event.getUser().getAsMention()
                    //+"! Make sure you read #rules.").queue();
            PrivateChannel priv = event.getUser().openPrivateChannel().complete();
            priv.sendMessage("Welcome to " + event.getGuild().getName() + "! Check #rules for official rules of this server and add your roles in #roles.").queue();

        }
    }

    /*
    public void onGuildMemberLeave(GuildMemberLeaveEvent event){
        if(! event.getGuild().getName().equals("Rutgers Esports")) {
            event.getGuild().getTextChannels().get(0).sendMessage(event.getUser().getName()+" has left").queue();
        }
    }
    */

	/*
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
        if(! event.getGuild().getName().equals("Rutgers Esports")) {
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

    // DEPRECATED
    /*
    public void onUserGameUpdate(UserGameUpdateEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        Game game = event.getCurrentGame();
        Game prevGame = event.getPreviousGame();
        if(game != null) {
            if(game.getType().toString().equals("STREAMING")) {
                try{
                    String prevGameName = prevGame.getName();
                    if(prevGame.getType().toString().equals("STREAMING")
                            || !prevGameName.equals(game.getName())) {
                        return;
                    }
                }catch(NullPointerException e) {
                    return;
                }

                if(event.getPreviousGame().getType().toString().equals("STREAMING")
                        && !event.getPreviousGame().getName().equals(game.getName())) {
                    return;
                }


				String s = "https://api.twitch.tv/helix/streams&client_id=" + Reference.TWITCHAPIKEY;
				try {
					JSONArray json = JSONReader.URLtoJSONArray(s);
					System.out.println("SUCCESS");
				}catch(IOException | JSONException e) {
					System.out.println("FAILURE");
				}

                eb.setColor(Color.MAGENTA);
                //event.getCurrentGame().getType()
                eb.setAuthor(event.getUser().getName() + " is now live!", null, event.getUser().getAvatarUrl());
                eb.setDescription(game.getName() + "\n" + game.getUrl());
                if(event.getGuild().getName().equals("Rutgers Esports")) {
                    int roleSize = event.getMember().getRoles().size();
                    int i;
                    for(i = 0; i < roleSize; i++) {
                        if(event.getMember().getRoles().get(i).getName().equals("Rutgers Student")) {
                            event.getGuild().getTextChannelsByName("livestreams", true).get(0).sendMessage(eb.build()).queue();
                            break;
                        }
                    }

                }else {
                    event.getGuild().getTextChannelsByName("self_promotion", true).get(0).sendMessage(eb.build()).queue();
                }
            }
        }
    }
    */

}
