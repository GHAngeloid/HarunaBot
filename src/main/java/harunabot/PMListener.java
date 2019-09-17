package harunabot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nonnull;
import java.util.List;

public class PMListener extends ListenerAdapter{

    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {

        // save Message content
        String message = event.getMessage().getContentRaw();

        if(message.equalsIgnoreCase("ping")) {
            event.getChannel().sendMessage("pong").queue();
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
