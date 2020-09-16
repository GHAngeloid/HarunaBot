package listener;

import configuration.AppConfig;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.collections4.ArrayStack;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        if(message.equalsIgnoreCase("!write")) {
            event.getChannel().sendMessage("WRITING ROLES TO FILE").queue();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("roles.txt");
                List<Role> roles = event.getJDA().getGuildById(AppConfig.PROPERTIES
                        .getProperty("GUILD"))
                        .getRoles();
                ArrayList<String> test = new ArrayList<>();
                for(Role role : roles) {
                    String roleName = role.getName();
                    if(roleName.equals("Nekogirl Idol")
                            || roleName.equals("Admin")
                            || roleName.equals("Mods")
                            || roleName.equals("LIVE")
                            || roleName.equals("Maid")
                            || roleName.equals("Rythm")
                            || roleName.equals("Lofi Radio")
                            || roleName.contains("Nugget")
                            || roleName.contains("Nitro")
                            || roleName.contains("Twitch")
                            || roleName.contains("@")) {
                        continue;
                    }
                    test.add(roleName);
                    for(int i = 0; i < roleName.length(); i++) {
                        fileOutputStream.write(roleName.charAt(i));
                    }
                    fileOutputStream.write('\n');
                }
                fileOutputStream.close();
                AppConfig.availableRoles = test;
                event.getChannel().sendMessage("ROLES -> FILE SUCCESS").queue();
            }
            catch(IOException e) {
                event.getChannel().sendMessage("FAILURE ON WRITE DETECTED").queue();
            }
        }

    }

}
