package application;

import javax.security.auth.login.LoginException;

import configuration.AppConfig;
import listener.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;

/**
 * HarunaBot
 */
public class HarunaBot extends ListenerAdapter{

    public static void main(String[] args) throws LoginException, IOException, IllegalArgumentException, InterruptedException {

        AppConfig.initializeProperties();

        JDABuilder builder = JDABuilder.createDefault(AppConfig.PROPERTIES.getProperty("TOKEN"));

        // setting bot characteristics
        builder.setAutoReconnect(true);
        builder.setActivity(Activity.of(ActivityType.DEFAULT, "with Taichou"));

        // build
        JDA jda = builder.build();

        // add listeners
        jda.addEventListener(new StatusListener(),
                new GuildMemberListener(),
                new VoiceListener(),
                new PMListener(),
                new CommandListener(),
                new AuditListener());

    }

}
