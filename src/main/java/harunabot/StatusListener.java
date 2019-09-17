package harunabot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

//import java.awt.Color;

//import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;

import javax.annotation.Nonnull;


public class StatusListener extends ListenerAdapter {

    public void onReady(@Nonnull ReadyEvent event){
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            System.out.printf("[+] %s (%s Members) - READY\n", event.getJDA().getGuilds().get(i).getName(),
                    event.getJDA().getGuilds().get(i).getMembers().size());
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

    public void onShutdown(@Nonnull ShutdownEvent event){
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            System.out.printf("[+] %s (%s Members) - SHUTDOWN\n", event.getJDA().getGuilds().get(i).getName(),
                    event.getJDA().getGuilds().get(i).getMembers().size());
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setDescription("Haruna has **shut down**. -_-");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

    public void onDisconnect(@Nonnull DisconnectEvent event) {
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            System.out.printf("[+] %s (%s Members) - DISCONNECTED\n", event.getJDA().getGuilds().get(i).getName(),
                    event.getJDA().getGuilds().get(i).getMembers().size());
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setDescription("Haruna has **disconnected**. ;;");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

    public void onReconnect(@Nonnull ReconnectedEvent event) {
        for(int i = 0; i < event.getJDA().getGuilds().size(); i++) {
            System.out.printf("[+] %s (%s Members) - RECONNECTED\n", event.getJDA().getGuilds().get(i).getName(),
                    event.getJDA().getGuilds().get(i).getMembers().size());
        }
		/*
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setDescription("Haruna has **reconnected**. Everything is daijoubu~");
		event.getJDA().getGuilds().get(0).getTextChannelsByName("botcommands", true).get(0).sendMessage(eb.build()).queue();
		*/
    }

}
