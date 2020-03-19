package listener;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Status Listener
 */
public class StatusListener extends ListenerAdapter {

    static Logger logger = LoggerFactory.getLogger(StatusListener.class);

    /**
     * JDA is ready.
     * @param event ReadyEvent
     */
    public void onReady(@Nonnull ReadyEvent event){
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            String message = event.getJDA().getGuilds().get(i).getName() + " ("
                    + event.getJDA().getGuilds().get(i).getMembers().size() + " Members) - READY";
            logger.info(message);
            //logger.debug("this is a debug log message");
            //logger.info("this is a information log message");
            //logger.warn("this is a warning log message");
        }
        //DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        //Date dateobj = new Date();
        //System.out.println(df.format(dateobj));
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setDescription("Haruna is **ready**. :3");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

    /**
     * JDA is shutting down.
     * @param event ShutdownEvent
     */
    public void onShutdown(@Nonnull ShutdownEvent event){
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            String message = event.getJDA().getGuilds().get(i).getName() + " ("
                    + event.getJDA().getGuilds().get(i).getMembers().size() + " Members) - SHUTDOWN";
            logger.info(message);
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setDescription("Haruna has **shut down**. -_-");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

    /**
     * JDA is disconnected.
     * @param event DisconnectEvent
     */
    public void onDisconnect(@Nonnull DisconnectEvent event) {
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            String message = event.getJDA().getGuilds().get(i).getName() + " ("
                    + event.getJDA().getGuilds().get(i).getMembers().size() + " Members) - DISCONNECTED";
            logger.info(message);
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setDescription("Haruna has **disconnected**. ;;");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

    /**
     * JDA is reconnected.
     * @param event ReconnectedEvent
     */
    public void onReconnect(@Nonnull ReconnectedEvent event) {
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            String message = event.getJDA().getGuilds().get(i).getName() + " ("
                    + event.getJDA().getGuilds().get(i).getMembers().size() + " Members) - RECONNECTED";
            logger.info(message);
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setDescription("Haruna has **reconnected**. Everything is daijoubu~");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

}
