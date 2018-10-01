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
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.*;
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
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The text channel **" + event.getOldName() + "** has been renamed to **"
                + event.getNewName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The text channel **" + event.getChannel().getName() + "** changed its topic from **"
                + event.getOldTopic() + "** to **" + event.getNewTopic() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The text channel **" + event.getChannel().getName() + "** moved from position **"
                + event.getOldPosition() + "** to position **" + event.getNewPosition() + "** in the server.");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
        // This one is tricky
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

    public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The voice channel **" + event.getOldName() + "** has been renamed to **"
                + event.getNewName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The voice channel **" + event.getChannel().getName() + "** moved from position **"
                + event.getOldPosition() + "** to position **" + event.getNewPosition() + "** in the server.");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

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

    public void onCategoryUpdateName(CategoryUpdateNameEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The category **" + event.getOldName() + "** has been renamed to **"
                + event.getNewName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onCategoryUpdatePosition(CategoryUpdatePositionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The category **" + event.getCategory().getName() + "** moved from position **"
                + event.getOldPosition() + "** to position **" + event.getNewPosition() + "** in the server.");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onCategoryUpdatePermissions(CategoryUpdatePermissionsEvent event) {
        // HARD
    }

    // Role Events
    public void onRoleCreate(RoleCreateEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription("Created role **" + event.getRole().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onRoleDelete(RoleDeleteEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription("Deleted role **" + event.getRole().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onRoleUpdateName(RoleUpdateNameEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The role **" + event.getOldName() + "** has been renamed to **"
                + event.getNewName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onRoleUpdateColor(RoleUpdateColorEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The role **" + event.getRole().getName() + "** had its color changed.");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onRoleUpdatePosition(RoleUpdatePositionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The role **" + event.getRole().getName() + "** moved from position **"
                + event.getOldPosition() + "** to position **" + event.getNewPosition() + "** in the server.");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onRoleUpdateHoisted(RoleUpdateHoistedEvent event) {}
    public void onRoleUpdateMentionable(RoleUpdateMentionableEvent event) {}
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {}


    // Emote Events
    public void onEmoteAdded(EmoteAddedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        eb.setDescription("Created emote **" + event.getEmote().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onEmoteRemoved(EmoteRemovedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setDescription("Deleted emote **" + event.getEmote().getName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onEmoteUpdateName(EmoteUpdateNameEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        eb.setDescription("The emote **" + event.getOldName() + "**" + " has been renamed to **"
                + event.getNewName() + "**");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' hh:mm a z");
        String msg = dateFormatter.format(new Date());
        eb.setFooter(msg, null);
        event.getGuild().getTextChannelsByName("audit", true).get(0)
                .sendMessage(eb.build()).queue();
    }

    public void onEmoteUpdateRoles(EmoteUpdateRolesEvent event) {}


}
