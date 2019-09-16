package harunabot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.factory.IAudioSendFactory;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HarunaBot extends ListenerAdapter{

    public static void main(String[] args) throws LoginException, InterruptedException, IllegalArgumentException{

        // builds account type
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        // setting bot characteristics
        builder.setAutoReconnect(true);
        builder.setActivity(Activity.of(ActivityType.DEFAULT, "with Taichou"));
        //builder.setAudioEnabled(true);

        // include Listener classes
        addListeners(builder);


        // set token to JDA and build blocking
        JDA jda = builder.setToken(Reference.TOKEN).build();

    }

    private static void addListeners(JDABuilder builder) {

        builder.addEventListeners(new StatusListener(),
                new GuildMemberListener(),
                new VoiceListener(),
                new PMListener(),
                new Commands(),
                new Audit());

    }

}
