package application;

import javax.security.auth.login.LoginException;

import configuration.AppConfig;
import listener.*;
import net.dv8tion.jda.api.AccountType;
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

    public static void main(String[] args) throws LoginException, IOException, IllegalArgumentException{

        // builds account type
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        // setting bot characteristics
        builder.setAutoReconnect(true);
        builder.setActivity(Activity.of(ActivityType.DEFAULT, "with Taichou"));
        //builder.setAudioEnabled(true);

        // include Listener classes
        addListeners(builder);

        // initialize properties
        AppConfig.initializeProperties();

        // set token to JDA and build blocking
        JDA jda = builder.setToken(AppConfig.PROPERTIES.getProperty("TOKEN")).build();

    }

    /**
     * Add Listeners to JDABuilder
     * @param builder JDABuilder
     */
    private static void addListeners(JDABuilder builder) {

        builder.addEventListeners(new StatusListener(),
                new GuildMemberListener(),
                new VoiceListener(),
                new PMListener(),
                new CommandListener(),
                new AuditListener());

    }

}
