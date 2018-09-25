package harunabot;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import java.text.SimpleDateFormat;
import java.util.Date;

//import net.dv8tion.jda.core.requests.RestAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.RichPresence.Timestamps;
//import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.*;
import java.net.*;

public class Commands extends ListenerAdapter{

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {

        // split Message content apart by whitespace
        String[] command = event.getMessage().getContentRaw().split(" ");
        //String message = event.getMessage().getContent();

        /* DEPRECATED. Reason: Infinite Prints
        // owo listener
        if(command[0].equalsIgnoreCase("owo")){
            event.getChannel().sendMessage(new MessageBuilder().append("owo").build()).queue();
        }
        */

        // Check if command does not start with '!' or the website is not osu!
        if(!command[0].startsWith(Reference.PREFIX) && !command[0].startsWith("https://osu.ppy.sh/")) {
            return;
        }

        // Make sure commands stay in appropriate channel(s). Future plan on making this dynamic
        if(command[0].startsWith(Reference.PREFIX)
                && !(event.getChannel().getName().equalsIgnoreCase("botcommands")
                || event.getChannel().getName().equalsIgnoreCase("bottestinglab"))){
            return;
        }

        // initialize Date
        Date now = new Date();

        //System.out.println(command[0]);

        // osu! beatmap listener for info
        // CHANGE : include new URL for beatmaps for new website
        if(command[0].startsWith("https://osu.ppy.sh/b/") || command[0].startsWith("https://osu.ppy.sh/s/")) {
            JSONObject data = null;
            String beatmapId = "";

            if(command[0].charAt(19) == 's') {
                beatmapId = "&s=" + command[0].substring(21);
            }else {
                beatmapId = "&b=" + command[0].substring(21);
            }

            //beatmapId = command[0].substring(21);
            String map = "https://osu.ppy.sh/api/get_beatmaps?k=" + Reference.OSUAPIKEY
                    + beatmapId + "&type=string";

            HttpURLConnection conn = null;
            InputStream in = null;
            try{
                conn = JSONReader.connect(map, Reference.OSUAPIKEY);
                in = conn.getInputStream();
            }catch(IOException e){
                return;
            }

            StringBuffer response = null;
            try{
                response = JSONReader.toStringBuffer(in);
            }catch(IOException e){
                return;
            }
            conn.disconnect();

            //Read JSON response and print
            JSONArray beatmapMetadataResponse = new JSONArray(response.toString());
            data = beatmapMetadataResponse.getJSONObject(0);

            int mapLength = data.getInt("total_length");
            int minutes = mapLength / 60;
            int seconds = mapLength % 60;

            int statusCode = data.getInt("approved");
            String status = "";
            switch(statusCode) {
                case 4:
                    status = "Loved";
                    break;
                case 3:
                    status = "Qualified";
                    break;
                case 2:
                    status = "Approved";
                    break;
                case 1:
                    status = "Ranked";
                    break;
                case 0:
                    status = "Pending";
                    break;
                case -1:
                    status = "WIP";
                    break;
                case -2:
                    status = "Graveyard";
                    break;
                default:
                    status = "ERROR";
            }
            String output = "**Song:** " + data.getString("title") + "\n"
                    + "**Artist:** " + data.getString("artist") + "\n"
                    + "**Diff Name:** " + data.getString("version") + "\n"
                    + "**Mapper:** " + data.getString("creator") + "\n"
                    + "**Length:** " + String.format("%d:%02d", minutes, seconds) + "\t**BPM:** " + data.getDouble("bpm") + "\n"
                    + "**Star Difficulty:** " + String.format("%.2f",data.getDouble("difficultyrating")) + "\n"
                    + "**CS:** " + data.getDouble("diff_size") + "\t**AR:** " + data.getDouble("diff_approach")
                    + "\t**HP:** " + data.getDouble("diff_drain") + "\t**OD:** " + data.getDouble("diff_overall") + "\n"
                    + "**Status:** " + status + "\n"
                    + "**Links:**\t[Download](https://osu.ppy.sh/d/" + data.getInt("beatmapset_id")
                    + ")\t[Bloodcat Mirror](http://bloodcat.com/osu/s/" + data.getInt("beatmapset_id") + ")";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            eb.setThumbnail("https://b.ppy.sh/thumb/" + data.getInt("beatmapset_id") + "l.jpg");
            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // osu! profile stats
        else if(command[0].startsWith("https://osu.ppy.sh/u/") || command[0].startsWith("https://osu.ppy.sh/users/")) {
            String user = "";

            if(command[0].charAt(20) == '/') {
                user = command[0].substring(21);
            }else {
                user = command[0].substring(25);
            }

            String s = "https://osu.ppy.sh/api/get_user?k=" + Reference.OSUAPIKEY + "&u=" + user + "&type=id";

            HttpURLConnection conn = null;
            InputStream in = null;
            try{
                conn = JSONReader.connect(s, Reference.OSUAPIKEY);
                in = conn.getInputStream();
            }catch(IOException e){
                return;
            }

            StringBuffer response = null;
            try{
                response = JSONReader.toStringBuffer(in);
            }catch(IOException e){
                return;
            }
            conn.disconnect();

            //Read JSON response and print
            JSONArray userResponse = new JSONArray(response.toString());
            JSONObject data = userResponse.getJSONObject(0);

            String output = "**[" + data.getString("username") + "](https://osu.ppy.sh/users/" + data.getString("user_id") + ")**\n"
                    + "Level: " + (int)data.getDouble("level") + "\n"
                    + "Global Rank: " + String.format("%,d", data.getInt("pp_rank"))
                    + " (" + data.getString("country") + " #" + String.format("%,d", data.getInt("pp_country_rank")) + ")\n"
                    + "Total PP: " + String.format("%,.2f", data.getDouble("pp_raw")) + "pp\n"
                    + "Ranked Score: " + String.format("%,d", data.getLong("ranked_score")) + "\n"
                    + "Hit Accuracy: " + String.format("%.2f", data.getDouble("accuracy")) + "%\n"
                    + "Playcount: " + String.format("%,d", data.getInt("playcount"));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            eb.setThumbnail("https://a.ppy.sh/" + data.getString("user_id"));
            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // ping
        else if(command[0].equalsIgnoreCase("!ping")) {
            String msg = "Pong! `" + event.getJDA().getPing() + "ms`";
            if(command.length == 1) {
                event.getChannel().sendMessage(msg).queue();
            }else if(command.length == 2 && command[1].equalsIgnoreCase("-e")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setDescription(msg);
                event.getChannel().sendMessage(eb.build()).queue();

            }
        }


        // help
        else if(command[0].equalsIgnoreCase("!help")){
            String output = "**List of commands:**\n!ping\n!roll\n!emotes\n!rolelist\n"
                    + "!server\n!love\n!addrole\n!deleterole\n!osutopscores\n!game\n!pubg";
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            event.getChannel().sendMessage(eb.build()).queue();
            //need to find a better solution to print all commands through a message
        }

        /* DEPRECATED
        // tips
        else if(command[0].equalsIgnoreCase("!tips")){
            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " PLAY MORE!").queue();
        }//returns "PLAY MORE!"
        */


        // roll
        else if(command[0].equalsIgnoreCase("!roll")){
            int max = 0;
            if(command.length == 1) {
                max = 10;
            }else {
                try {
                    max = Integer.parseInt(command[1]);
                }catch(NumberFormatException e){
                    event.getChannel().sendMessage("ERROR: Bad input").queue();
                    return;
                }
            }
            if(max <= 0) {
                event.getChannel().sendMessage("ERROR").queue();
                return;
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getAuthor().getAsMention() + " rolls " + roll(max));
            event.getChannel().sendMessage(eb.build()).queue();
        }//returns a random # from 1 to 100


        /* DEPRECATED
        // roles
		else if(command[0].equalsIgnoreCase("!rolesize")){
			event.getChannel().sendMessage("# of Roles: "+event.getGuild().getRoles().size()).queue();
		}//returns # of roles
        */


        // TEST METHOD
        /*
        else if(command[0].equalsIgnoreCase("!test")){
            int total = event.getChannel().getHistory().size();
            event.getChannel().getHistory().;
            event.getChannel().sendMessage(event.getChannel().getName() + ": " + total).queue();

        }
        */


        // emotes
        else if(command[0].equalsIgnoreCase("!emotes")){
            String output = "**Server Emotes:**\n" + emotePrint(event);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            event.getChannel().sendMessage(eb.build()).queue();
        }//returns all of the server emotes


        // rolelist
        else if(command[0].equals("!rolelist")){
            String output = "**List of available roles:**\n";
            // Display roles that are allowed. Make it Permission sensitive!
            Role role;
            boolean isAllowed = true;
            for(int i = 0; i < event.getGuild().getRoles().size(); i++){
                role = event.getGuild().getRoles().get(i);
                if(role.getName().equals("@everyone") || role.getName().equals("LIVE")) {
                    continue;
                }
                for(int j = 0; j < role.getPermissions().size(); j++) {
                    //System.out.println(temp.getPermissions().get(i).getName());
                    if(role.getPermissions().get(j).getName().equals("Administrator")
                            || role.getPermissions().get(j).getName().equals("Kick Members")
                            || role.getPermissions().get(j).getName().equals("Ban Members")
                            || role.getPermissions().get(j).getName().equals("Move Members")
                            || role.isManaged()) {
                        isAllowed = false;
                        break;
                    }
                }

                if(event.getGuild().getName().equals(Reference.PUBLICSERVER)){
                    if(role.getName().equals("Former Community Lead")
                            || role.getName().equals("Community Lead")
                            || role.getName().equals("Coach/Analyst")
                            || role.getName().equals("Tech Support")
                            || role.getName().equals("Tespa East Regional Coordinator")
                            || role.getName().equals("Scarlet Knights (D1OW)")
                            || role.getName().equals("Scarlet Squires (D2OW)")
                            || role.getName().equals("RU uLoL")
                            || role.getName().equals("RU CSL (LoL)")
                            || role.getName().equals("RUCS-A (CS:GO)")
                            || role.getName().equals("RUCS-B (CS:GO)")
                            || role.getName().equals("Team Bus (Dota)")
                            || role.getName().equals("HoTS Team")
                            || role.getName().equals("Rocket League Team A (Subpar)")
                            || role.getName().equals("Rocket League Team B (Zoomy-Woomy Sauce & Boomy)")) {
                        isAllowed = false;
                    }
                }
                if(isAllowed) {
                    output += role.getName() + "\n";
                }else{
                    isAllowed = true;
                }
            }
            //event.getChannel().sendMessage(output).queue();
            //System.out.println(output);
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            event.getChannel().sendMessage(eb.build()).queue();
        }//returns all roles in server

        /* DEPRECATED
        // choose
		else if(command[0].equalsIgnoreCase("!choose")) {
			//System.out.println(command[1].length());
			if(command.length == 1) {
    				event.getChannel().sendMessage("ERROR: Invalid use of command. "
    				+ "Should be !choose <choice1> <choice2>...<choiceN>."
    				+ " Choices need to be one word").queue();
			}
    			else if(command.length == 2) {
    				event.getChannel().sendMessage("I suggest you " + command[1]).queue();
    				//command.length==2||(command.length>2&&!command[1].endsWith(","))
    				//example: !choose sleep
    			}
    			else if(command.length > 2) {

    				String output = "";
    				for(int i = 1; i < command.length; i++) {
    					if(i + 1 == command.length) {
    						output += command[i];
    						break;
    					}
    					output += command[i] + " ";
    				}


    				for(int i = 1; i < command.length; i++) {
    					if(!command[i].contains(",") && i + 1 == command.length) {

    					}
    				}

    				int indexPick = roll(command.length-1);
    				//add if statement to trim comma HERE
    				String temp = command[indexPick];
    				event.getChannel().sendMessage("I suggest you " + temp).queue();
    				//need to add a temp string that will trim an excess comma ',' (i.e code, sleep)
    			}//example: !choose code, sleep, play
		}// returns the arg that is chosen
		*/

        // love
        else if(command[0].equalsIgnoreCase("!love")) {
            int maxMembers = event.getGuild().getMembers().size();
            //System.out.println(maxMembers);
            //System.out.println(event.getGuild().getMembers().get(roll(maxMembers)));
            String lover = "";
            do {
                lover = event.getGuild().getMembers().get(roll(maxMembers)).getUser().getName();
            }while(event.getAuthor().getName().equals(lover));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(event.getAuthor().getName() + " loves " + lover + " <3");
            event.getChannel().sendMessage(eb.build()).queue();
        }// returns the user "loving" a random user in guild

        // giveaway

        else if(command[0].equalsIgnoreCase("!giveaway")) {
            if(!event.getAuthor().getName().equals(event.getGuild().getOwner().getUser().getName())) {
                return;
            }//tests if author is the owner
            int maxMembers = event.getGuild().getMembers().size();
            String winner = "";
            do {
                winner = event.getGuild().getMembers().get(roll(maxMembers)).getUser().getName();
            }while(event.getAuthor().getName().equals(winner));
            event.getChannel().sendMessage("Winner: " + winner);
            //System.out.println("Winner: "+winner);
        }// returns the winner of the giveaway


        // add role or delete role
        // <command> <role>
        else if(command[0].equalsIgnoreCase("!addrole") || command[0].equalsIgnoreCase("!deleterole")) {
            // include case that tests if author is owner

            // include code for the Confirmed role where they must PM the bot a valid confirmation

            if(command.length < 2) {
                event.getChannel().sendMessage("ERROR! Incorrect format. <!addrole | !deleterole> <role>.").queue();
                return;
            }
            // insert loop to find role (i.e. New Jersey)
            String roleName = ""; // role name
            roleName = command[1];
            if(command.length > 2) {
                for(int i = 2; i < command.length; i++) {
                    roleName = roleName.concat(" " + command[i]);
                }
            }
            //System.out.println(roleName);
            //int roleIndex = 0;
            Role role = null;
            for(int i = 0; i < event.getGuild().getRoles().size(); i++) {
                if(roleName.equalsIgnoreCase(event.getGuild().getRoles().get(i).getName())) {
                    //roleIndex =  i;
                    role = event.getGuild().getRoles().get(i);
                    roleName = role.getName();
                    break;
                }
                if(i + 1 == event.getGuild().getRoles().size()) {
                    event.getChannel().sendMessage("ERROR! Role does not exist.").queue();
                    return;
                }// locates the role in List<Role>
                // include case if the role does not exist
            }

            // if role is already
            if(command[0].equalsIgnoreCase("!addrole") && event.getMember().getRoles().contains(role)){
                return;
            }

            if(role.getName().equalsIgnoreCase("LIVE")){
                event.getChannel().sendMessage("You are not allowed to add/delete this role.").queue();
                return;
            }


            // CASE CHECK PRIMARILY FOR PUBLIC SERVER
            if(event.getGuild().getName().equals(Reference.PUBLICSERVER)) {
                // not sure if I should change
                if(role.getName().equals("Former Community Lead")
                        || role.getName().equals("Community Lead")
                        || role.getName().equals("Coach/Analyst")
                        || role.getName().equals("Tech Support")
                        || role.getName().equals("Tespa East Regional Coordinator")) {
                    event.getChannel().sendMessage("You are not allowed to add/delete this role.").queue();
                    return;
                }
                // checks permissions if user can add/delete specified role
                //Role temp = event.getGuild().getRoles().get(roleIndex);
                for(int i = 0; i < role.getPermissions().size(); i++) {
                    //System.out.println(temp.getPermissions().get(i).getName());
                    if(role.getPermissions().get(i).getName().equals("Administrator")
                            || role.getPermissions().get(i).getName().equals("Kick Members")
                            || role.getPermissions().get(i).getName().equals("Ban Members")
                            || role.getPermissions().get(i).getName().equals("Move Members")
                            || role.getPermissions().get(i).getName().equals("Create Instant Invite")
                            || role.isManaged()) {
                        event.getChannel().sendMessage("You are not allowed to add/delete this role.").queue();
                        return;

                    }
                }
                // COMPETITIVE PLAYER CASE
                // terrible looking testcase, will replace later
                if((role.getName().equalsIgnoreCase("Scarlet Knights (D1OW)")
                        || role.getName().equalsIgnoreCase("Scarlet Squires (D2OW)")
                        || role.getName().equalsIgnoreCase("RU uLoL")
                        || role.getName().equalsIgnoreCase("RU CSL (LoL)")
                        || role.getName().equalsIgnoreCase("RUCS-A (CS:GO)")
                        || role.getName().equalsIgnoreCase("RUCS-B (CS:GO)")
                        || role.getName().equalsIgnoreCase("Team Bus (Dota)")
                        || role.getName().equalsIgnoreCase("HoTS Team")
                        || role.getName().equalsIgnoreCase("Rocket League Team A (Subpar)")
                        || role.getName().equalsIgnoreCase("Rocket League Team B (Zoomy-Woomy Sauce & Boomy)"))
                        && command[0].equalsIgnoreCase("!addrole")){
                    event.getChannel().sendMessage("Contact an Admin/Moderator to add this role.").queue();
                    return;
                }

                // RUTGERS PERSON CASE
                if((role.getName().equalsIgnoreCase("Rutgers Student")
                        || role.getName().equalsIgnoreCase("Rutgers Alumni"))
                        && command[0].equalsIgnoreCase("!addrole")) {

                    // return if User is trying to add different Unique Role
                    if(event.getMember().getRoles().contains(event.getGuild().getRolesByName("Rutgers Student", true).get(0))
                        || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Rutgers Alumni", true).get(0))
                            || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Guest", true).get(0))){
                        return;
                    }
                    // send a PM to User
                    PrivateChannel privateChannel= event.getAuthor().openPrivateChannel().complete();
                    privateChannel.sendMessage("You are about to add a Rutgers role from Rutgers Esports. " +
                            "Please reply with STUDENT or ALUMNI and then your school email here (i.e. STUDENT <EMAIL ADDRESS>):").queue();
                    // DO SOMETHING HERE
                }
            }else{

                // Make sure Users do not get roles that are powerful
                for(int i = 0; i < role.getPermissions().size(); i++) {
                    //System.out.println(temp.getPermissions().get(i).getName());
                    if(role.getPermissions().get(i).getName().equals("Administrator")
                            || role.getPermissions().get(i).getName().equals("Kick Members")
                            || role.getPermissions().get(i).getName().equals("Ban Members")
                            || role.getPermissions().get(i).getName().equals("Move Members")
                            || role.isManaged()) {
                        event.getChannel().sendMessage("You are not allowed to add/delete this role.").queue();
                        return;
                    }
                }
            }

            // create GuildController to modify roles
            GuildController guildControl = new GuildController(event.getGuild());
            EmbedBuilder eb = new EmbedBuilder();

            // start of deleting role
            if(command[0].equalsIgnoreCase("!deleterole")) {
                try {
                    guildControl.removeSingleRoleFromMember(event.getMember(), role).queue();
                    eb.setColor(role.getColor());
                    eb.setDescription("**" + roleName + "**" + " role has been removed.");
                    event.getChannel().sendMessage(eb.build()).queue();
                }catch(HierarchyException e) {
                    event.getChannel().sendMessage("You are not able to delete your highest role.").queue();
                }
                return;
            }

            // start of adding role
            try {
                guildControl.addSingleRoleToMember(event.getMember(), role).queue();//works for single role
                eb.setColor(role.getColor());
                eb.setDescription("**" + roleName + "**" + " role has been added.");
                event.getChannel().sendMessage(eb.build()).queue();
            }catch(HierarchyException e) {
                event.getChannel().sendMessage("You are not able to add a higher role.").queue();
            }

        }


        // osutopscores
        else if(command[0].equalsIgnoreCase("!osutopscores")) {
            String apikey = Reference.OSUAPIKEY;
            JSONArray json = null;
            String user = "";

            if(command.length < 2) {
                event.getChannel().sendMessage("Incorrect command format. !osutopscores <Username>").queue();
                return;
            } else if(command.length == 2){
                user = command[1];
            }else {
                user = command[1];
                for (int i = 2; i < command.length; i++) {
                    user += " " + command[i];
                }
            }

            InputStream in = null;
            HttpURLConnection conn = null;
            try{
                String site = "https://osu.ppy.sh/api/get_user_best?k=" + apikey + "&u=" + user + "&limit=5&type=string";
                URL url = new URL(site);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", Reference.OSUAPIKEY);
                conn.setRequestProperty("Accept", "application/vnd.api+json");
                in = conn.getInputStream();
            }catch(IOException e){
                event.getChannel().sendMessage("RIP").queue();
                return;
            }
            //BufferedReader br = null;
            StringBuffer response = null;
            try{
                response = JSONReader.toStringBuffer(in);
            }catch(IOException e){
                return;
            }
            conn.disconnect();
            //print in String
            //System.out.println(response.toString());

            //Read JSON response and print
            JSONArray userBestResponse = new JSONArray(response.toString());
            int userId = userBestResponse.getJSONObject(0).getInt("user_id");
            //System.out.println(userId);
            JSONObject data = null;
            String output = "**" + user + "'s Top Scores**\n"
                    + "";
            // iterates through each beatmap to get it's metadata
            for(int i = 0; i < userBestResponse.length(); i++) {
                String score = "https://osu.ppy.sh/api/get_beatmaps?k=" + apikey + "&b="
                        + userBestResponse.getJSONObject(i).getInt("beatmap_id") + "&type=string";
                try{
                    URL url = new URL(score);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", Reference.OSUAPIKEY);
                    conn.setRequestProperty("Accept", "application/vnd.api+json");
                    in = conn.getInputStream();
                }catch(IOException e){
                    event.getChannel().sendMessage("RIP").queue();
                    return;
                }
                //BufferedReader br = null;
                response = null;
                try{
                    response = JSONReader.toStringBuffer(in);
                }catch(IOException e){
                    return;
                }
                conn.disconnect();
                //print in String
                //System.out.println(response.toString());

                //Read JSON response and print
                JSONArray beatmapMetadataResponse = new JSONArray(response.toString());
                data = beatmapMetadataResponse.getJSONObject(0);

                /*
                try {
                    data = JSONReader.URLtoJSON(score);//calls JSONReader class to convert URL contents to JSONObject
                }catch(JSONException | IOException e) {
                    event.getChannel().sendMessage("ERROR").queue();
                }
                */

                //System.out.println(data.toString());
                int modCode = userBestResponse.getJSONObject(i).getInt("enabled_mods");
                String mods = osuModReader(modCode);
                output += "\n" + data.getString("artist") + " - " + data.getString("title") + " [" + data.getString("version") + "]"
                        + " by " + data.getString("creator") + "\n"
                        + String.format("%.2f", userBestResponse.getJSONObject(i).getDouble("pp")) + "pp\n"
                        + "Mods: " + mods + "\t" + "Rank: " + userBestResponse.getJSONObject(i).getString("rank") + "\n";
            }
            //https://osu.ppy.sh/images/badges/score-ranks/Score-SS-Small-60@2x.png

            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setDescription(output);
            try {
                eb.setThumbnail("https://a.ppy.sh/" + userId);
            }catch(JSONException e) {
                return;
            }
            eb.setFooter("osu!", "https://osu.ppy.sh/images/layout/osu-logo@2x.png");
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // server
        else if(command[0].equalsIgnoreCase("!server")) {
            int online = 0, offline = 0;
            for(int i = 0; i < event.getGuild().getMembers().size(); i++) {
                if(event.getGuild().getMembers().get(i).getOnlineStatus().name().equals("OFFLINE")) {
                    offline++;
                }else {
                    online++;
                }
            }
            String output = "Population: " + event.getGuild().getMembers().size() + "\n"
                    + "Online: " + online +"\tOffline: " + offline;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            eb.setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl());
            eb.setDescription(output);
            //eb.setThumbnail(event.getGuild().getIconUrl());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("E MMM d'th,' yyyy 'at' h:m a");
            eb.setFooter("Created on " + event.getGuild().getCreationTime().getMonthValue() + "/"
                    + event.getGuild().getCreationTime().getDayOfMonth() + "/"
                    + event.getGuild().getCreationTime().getYear()
                    + " | " + dateFormatter.format(now), null);
            event.getChannel().sendMessage(eb.build()).queue();
        }


        // game
        else if(command[0].equalsIgnoreCase("!game")) {
            //event.getGuild().getMembersByName("YourPrincess", true).get(0).getGame().getName()
            // Title of Livestream
            //event.getGuild().getMembersByName("YourPrincess", true).get(0).getGame().getUrl()
            // URL of Livestream

            String user = "";
            EmbedBuilder eb = new EmbedBuilder();

            if(command.length == 1) {
                Game game = event.getMember().getGame();
                if(game != null) {
                    // is RichPresence
                    if(game.isRich()) {
                        System.out.println(game.asRichPresence().getName() + "\n"
                                + game.asRichPresence().getDetails() + "\n"
                                + game.asRichPresence().getState() + "\n"
                                + game.asRichPresence().getTimestamps());
                        return;
                    }else {
                        // not RichPresence, possible that it is a Stream
                        System.out.println(game.getName() + " " + game.getType());
                        eb.setDescription(event.getAuthor().getName() + " is playing " + game.getName());
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                        return;
                    }
                }else {
                    //System.out.println("You are not playing a game right now.");
                    eb.setColor(Color.ORANGE);
                    eb.setDescription("You are not playing a game right now.");
                    event.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }else if(command.length > 2) {
                user = command[1];
                for(int i = 2; i < command.length; i++) {
                    user += " " + command[i];
                }
            }else {
                user = command[1];
            }
            //System.out.println(user);
            if(event.getGuild().getMembersByName(user, true).isEmpty()) {
                return;
            }
            Member member = event.getGuild().getMembersByName(user, true).get(0);
            Game game = member.getGame();
            if(game != null) {
                // is RichPresence
                if(game.isRich()) {
                    String gameName = game.asRichPresence().getName();
                    String gameDetails = "";
                    try {
                        gameDetails = game.asRichPresence().getDetails();
                    }catch(NullPointerException e) {

                    }
                    String gameState = game.asRichPresence().getState();
                    String gameTimestamps = "";
                    try {
                        Timestamps timestamps = game.asRichPresence().getTimestamps();
                        gameTimestamps = "\n" + timestamps.toString();

                    }catch(NullPointerException e) {

                    }

                    System.out.println(gameName + "\n"
                            + gameDetails + "\n"
                            + gameState
                            + gameTimestamps);
                    eb.setAuthor(member.getUser().getName() + " is playing " + gameName, null, member.getUser().getAvatarUrl());
                    //eb.setDescription(gameDetails + " in " + gameState);
                    eb.setColor(Color.ORANGE);
                    event.getChannel().sendMessage(eb.build()).queue();

                }else {
                    // not RichPresence, possible that it is a Stream
                    //game.getUrl() will always return null if DEFAULT, no STREAMING
                    if(game.getType().toString().equals("STREAMING")) {
                        eb.setAuthor(member.getUser().getName() + " is streaming.", null, member.getUser().getAvatarUrl());
                        eb.setDescription(game.getName() + "\n" + game.getUrl());
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                    }else {
                        //System.out.println(game.getName() + " " + game.getType() + " " + game.getUrl());
                        if(game.getType().toString().equals("LISTENING")) {
                            eb.setAuthor(member.getUser().getName() + " is listening to " + game.getName(), null, member.getUser().getAvatarUrl());
                        }else {
                            eb.setAuthor(member.getUser().getName() + " is playing " + game.getName(), null, member.getUser().getAvatarUrl());
                        }
                        eb.setColor(Color.ORANGE);
                        event.getChannel().sendMessage(eb.build()).queue();
                    }
                }
            }else {
                eb.setDescription(member.getUser().getName() + " is not playing a game right now.");
                eb.setColor(Color.ORANGE);
                event.getChannel().sendMessage(eb.build()).queue();
            }
            //event.getGuild().getMembersByName("YourPrincess", true).get(0).getGame();
        }

        //event.getJDA().shutdownNow();

        //kick member???

        // pic/gif commands


        // pubg
        else if(command[0].equalsIgnoreCase("!pubg")){
            // data from latest match
            String user = "";
            if(command.length == 1){
                event.getChannel().sendMessage("ERROR! Must be: !pubg <Steam Name>").queue();
                return;
            }
            user = command[1];
            if(command.length > 2){
                for(int i = 2; i < command.length; i++){
                    user = user.concat(" " + command[i]);
                }
            }
            InputStream in = null;
            HttpURLConnection conn = null;
            try{
                URL url = new URL("https://api.playbattlegrounds.com/shards/pc-na/players?filter[playerNames]=" + user);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", Reference.PUBGAPIKEY);
                conn.setRequestProperty("Accept", "application/vnd.api+json");
                in = conn.getInputStream();
            }catch(IOException e){
                event.getChannel().sendMessage("Invalid Player Name. Must be in NA region on PC.");
                return;
            }
            //BufferedReader br = null;
            StringBuffer response = null;
            try{
                response = JSONReader.toStringBuffer(in);
            }catch(IOException e){
                return;
            }
            conn.disconnect();
            //print in String
            //System.out.println(response.toString());

            //Read JSON response and print
            JSONObject myResponse = new JSONObject(response.toString());
            String playerId = myResponse.getJSONArray("data")
                    .getJSONObject(0)
                    .getString("id");
            JSONArray matchData = myResponse.getJSONArray("data")
                    .getJSONObject(0)
                    .getJSONObject("relationships")
                    .getJSONObject("matches")
                    .getJSONArray("data");
            //System.out.println(matchData.toString());

            String[] matchIDArray = new String[matchData.length()];
            JSONObject match;
            try{
                match = matchData.getJSONObject(0);
            }catch(JSONException e) {
                return;
            }
            //matchIDArray[i] = temp2.getString("id");
            // System.out.println(matchIDArray[i]);

            // begin finding matches
            try{
                URL url = new URL("https://api.playbattlegrounds.com/shards/pc-na/matches/" + match.getString("id"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", Reference.PUBGAPIKEY);
                conn.setRequestProperty("Accept", "application/vnd.api+json");
                in = conn.getInputStream();
            }catch(IOException e){
                event.getChannel().sendMessage("You do not have any recent matches.").queue();
                return;
            }
            try{
                response = JSONReader.toStringBuffer(in);
            }catch(IOException e){
                return;
            }
            conn.disconnect();
            //System.out.println(response.toString());
            myResponse = new JSONObject(response.toString());
            //JSONObject data = myResponse.getJSONObject("data");
            JSONObject attributes = myResponse.getJSONObject("data").getJSONObject("attributes");
            //JSONObject relationships = myResponse.getJSONObject("data").getJSONObject("relationships");
            JSONArray included = myResponse.getJSONArray("included");

            JSONObject player = null;
            for(int j = 0; j < included.length(); j++) {
                player = myResponse.getJSONArray("included").getJSONObject(j);
                String test = "";
                try{
                    test = player.getJSONObject("attributes").getJSONObject("stats")
                            .getString("playerId");
                    // compare playerIds
                    if(test.equals(playerId)){
                        break;
                    }
                }catch(JSONException e){
                    continue;
                }
            }
            JSONObject stats = player.getJSONObject("attributes").getJSONObject("stats");
            //System.out.println("PLAYER FOUND");
            //System.out.println(player.toString());
            EmbedBuilder eb = new EmbedBuilder();

            int duration = attributes.getInt("duration");
            duration /= 60;
            String createdAt = attributes.getString("createdAt");
            String gameMode = attributes.getString("gameMode");
            //System.out.println(duration + " " + createdAt + " " + gameMode);
            int timeSurvived = stats.getInt("timeSurvived") / 60;

            eb.setAuthor(user + "'s Latest PUBG Match");
            eb.setDescription("Kills: " + stats.getInt("kills") + "\n"
                    + "Headshot Kills: " + stats.getInt("headshotKills") + "\n"
                    + "Assists: " + stats.getInt("assists") + "\n"
                    + "Damage Dealt: " + stats.getDouble("damageDealt") + "\n"
                    + "Revives: " + stats.getInt("revives") + "\n"
                    + "Time Survived: " + timeSurvived + " minutes \n"
                    + "Cause of Death: " + stats.getString("deathType") + "\n");
            eb.setFooter("PUBG | Match Duration: " + duration + " minutes", null);
            eb.setColor(Color.ORANGE);
            event.getChannel().sendMessage(eb.build()).queue();

        }

    }// end of commands



    // List of helper methods for some of the commands

    // emotePrint
    private String emotePrint(GuildMessageReceivedEvent e){
        int emoteTotal = e.getGuild().getEmotes().size();
        String[] idList = new String[emoteTotal];
        String message = "";
        for(int i = 0; i < emoteTotal; i++){
            idList[i] = e.getGuild().getEmotes().get(i).getId();
            //e.getGuild().getEmotes().get(i);
        }// list of emoteIDs

        for(int j = 0; j < emoteTotal; j++){
            if(j == emoteTotal - 1){
                message += "\t" + e.getGuild().getEmoteById(idList[j]).getAsMention() + "\n";
            }
            message += "\t" + e.getGuild().getEmoteById(idList[j]).getAsMention() + "\n";
        }//constructs entire message with emote names
        return message;
    }// constructs entire list of emotes on server as a message

    private int roll(int max){
        Random rn = new Random();
        int answer = rn.nextInt(max)+1;
        return answer;
    }// returns a roll from 1 and n=max (ex: 1 and 100)

    private String osuModReader(int code) {

        // referencing osu! API documentation on Game Mods
        if(code == 0) {
            return "NoMod";
        }
        String result = "";
        while(code != 0) {
            if(code >= 16416) {
                result = "PF" + result;
                code -= 16416;
            }else if(code >= 8192) {
                result = "AP" + result;
                code -= 8192;
            }else if(code >= 4096) {
                result = "SO" + result;
                code -= 4096;
            }else if(code >= 2048) {
                result = "AP" + result;
                code -= 2048;
            }else if(code >= 1024) {
                result = "FL" + result;
                code -= 1024;
            }else if(code >= 576) {
                result = "NC" + result;
                code -= 576;
            }else if(code >= 256) {
                result = "HT" + result;
                code -= 256;
            }else if(code >= 128) {
                result = "RX" + result;
                code -= 128;
            }else if(code >= 64) {
                result = "DT" + result;
                code -= 64;
            }else if(code >= 32) {
                result = "SD" + result;
                code -= 32;
            }else if(code >= 16) {
                result = "HR" + result;
                code -= 16;
            }else if(code >= 8) {
                result = "HD" + result;
                code -= 8;
            }else if(code >= 4) {
                result = "NoVideo" + result;
                code -= 4;
            }else if(code >= 2) {
                result = "EZ" + result;
                code -= 2;
            }else if(code >= 1) {
                result = "NF" + result;
                code -= 1;
            }
        }
        return result;
    }// end of osuModReader

}
