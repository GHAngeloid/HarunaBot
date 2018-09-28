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
            return;
        }
		/*
		else if(message.equalsIgnoreCase("!confirm")) {
			event.getChannel().sendMessage("You are about to confirm a role in the server. Please input a valid Rutgers email.").queue();
		}
		*/

        else if(message.contains(Reference.SCHOOLEMAIL1) || message.contains(Reference.SCHOOLEMAIL2)) {

            if(!message.contains("STUDENT") && !message.contains("ALUMNI")){
                return;
            }

            message.indexOf(' ');

            /*
            if(message.contains(Reference.SCHOOLEMAIL1)){
                if(!Character.isAlphabetic(message.charAt(0)) && !Character.isDigit(message.charAt(0))){
                    return;
                }
            }else{
                if(!Character.isAlphabetic(message.charAt(0))){
                    return;
                }
            }
            */

            // add Role to server
            Guild guild = event.getJDA().getGuildById(Reference.PUBLICGUILDID);
            GuildController gc = new GuildController(guild);

            if(message.contains("ALUMNI")){
                gc.addSingleRoleToMember(guild.getMember(event.getAuthor()), guild.getRolesByName("Rutgers Alumni", true).get(0)).queue();
            }else{
                gc.addSingleRoleToMember(guild.getMember(event.getAuthor()), guild.getRolesByName("Rutgers Student", true).get(0)).queue();
            }
            event.getChannel().sendMessage("Access granted!").queue();
            return;

        }

        else if(message.contains("!initroles")){
            if(!event.getJDA().getGuildById(Reference.PRIVATEGUILDID).getOwner().getUser().equals(event.getAuthor())){
                return;
            }
            int index = message.indexOf(' ');
            if(index == -1){
                return;
            }
            if(Reference.COMPROLES.size() == 10){
                event.getChannel().sendMessage("Fail").queue();
                return;
            }
            String roleName = message.substring(index + 1);
            Reference.COMPROLES.add(roleName);
            event.getChannel().sendMessage("Success").queue();
            return;
        }

        else if(message.contains("!print")){
            if(!event.getJDA().getGuildById(Reference.PRIVATEGUILDID).getOwner().getUser().equals(event.getAuthor())){
                return;
            }
            if(Reference.COMPROLES.isEmpty()){
                event.getChannel().sendMessage("List is empty!").queue();
                return;
            }
            event.getChannel().sendMessage(Reference.COMPROLES.toString()).queue();
        }

        /*
        else if(message.contains("echo") && event.getAuthor().isBot() == false){
            event.getChannel().sendMessage(message).queue();
        }
        */

        // include call to server to add Role to User
    }

}
