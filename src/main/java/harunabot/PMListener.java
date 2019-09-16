package harunabot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Role;
import java.util.List;

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

        else if(message.equals("!color")){
            List<Role> roles = event.getJDA().getGuildById(Reference.PUBLICGUILDID).getRoles();
            String output = "";
            for(int i = 0; i < roles.size(); i++){
                if(roles.get(i).getName().equals("@everyone")){
                    continue;
                }
                else if(roles.get(i).getColor() == null){
                    continue;
                }
                output = output + roles.get(i).getName() + " " + roles.get(i).getColor().getRed()
                        + " " + roles.get(i).getColor().getGreen() + " " + roles.get(i).getColor().getBlue() + "\n";
            }
            event.getChannel().sendMessage(output).queue();
        }

        /*
        else if(message.contains("echo") && event.getAuthor().isBot() == false){
            event.getChannel().sendMessage(message).queue();
        }
        */

        // include call to server to add Role to User
    }

}
