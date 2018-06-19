package harunabot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class HarunaBot extends ListenerAdapter{

    public static void main(String[] args) throws LoginException, InterruptedException, IllegalArgumentException{

        // builds account type
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        // setting bot characteristics
        builder.setAutoReconnect(true);
        builder.setGame(Game.of(GameType.DEFAULT, "with Taichou"));
        builder.setAudioEnabled(true);

        // include Listener classes
        addListeners(builder);

        // set token to JDA and build blocking
        JDA jda = builder.setToken(Reference.TOKEN).buildBlocking();

    }

    public static void addListeners(JDABuilder builder) {

        builder.addEventListener(new StatusListener());
        builder.addEventListener(new GuildMemberListener());
        builder.addEventListener(new VoiceListener());
        builder.addEventListener(new PMListener());
        builder.addEventListener(new Commands());

    }

}
