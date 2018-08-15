package harunabot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class PMListener extends ListenerAdapter{

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        // save Message content
        String message = event.getMessage().getContentRaw();

        if(message.equalsIgnoreCase("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }
		/*
		else if(message.equalsIgnoreCase("!confirm")) {
			event.getChannel().sendMessage("You are about to confirm a role in the server. Please input a valid Rutgers email.").queue();
		}
		*/

        else if(message.contains(Reference.SCHOOLEMAIL1) || message.contains(Reference.SCHOOLEMAIL2)) {

            // add Role to server
            Guild guild = event.getJDA().getGuildsByName(Reference.PUBLICSERVER, true).get(0);
            GuildController gc = new GuildController(guild);

            if(message.contains("ALUMNI")){
                gc.addSingleRoleToMember(guild.getMember(event.getAuthor()), guild.getRolesByName("Rutgers Alumni", true).get(0)).queue();
            }else{
                gc.addSingleRoleToMember(guild.getMember(event.getAuthor()), guild.getRolesByName("Rutgers Student", true).get(0)).queue();
            }
            event.getChannel().sendMessage("Access granted!").queue();

        }

        // include call to server to add Role to User
    }

}
