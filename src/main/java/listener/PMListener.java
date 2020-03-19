package listener;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * Private Message Listener
 */
public class PMListener extends ListenerAdapter{

    /**
     * Private Message Received
     * @param event PrivateMessageReceivedEvent
     */
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {

        // save Message content
        String message = event.getMessage().getContentRaw();

        if(message.equalsIgnoreCase("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }

    }

}
