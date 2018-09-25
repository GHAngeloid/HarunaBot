package harunabot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.core.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePositionEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateTopicEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdatePositionEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.Response;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;

public class Audit extends ListenerAdapter {

    // Text Channel Events
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription("Created text channel **" + event.getChannel().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();

    }

    public void onTextChannelDelete(TextChannelDeleteEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription("Deleted text channel **" + event.getChannel().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {

    }

    public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event) {

    }

    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {

    }

    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {

    }


    // Voice Channel Events
    public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription("Created voice channel **" + event.getChannel().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription("Deleted voice channel **" + event.getChannel().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }
    public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {}
    public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event) {}
    public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {}
    public void onVoiceChannelUpdateUserLimit(VoiceChannelUpdateUserLimitEvent event) {}


    // Category Events
    public void onCategoryCreate(CategoryCreateEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription("Created category **" + event.getCategory().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onCategoryDelete(CategoryDeleteEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription("Deleted category **" + event.getCategory().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }
    public void onCategoryUpdateName(CategoryUpdateNameEvent event) {}
    public void onCategoryUpdatePosition(CategoryUpdatePositionEvent event) {}
    public void onCategoryUpdatePermissions(CategoryUpdatePermissionsEvent event) {}


}
