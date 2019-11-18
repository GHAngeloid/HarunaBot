package listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

/**
 * List of helper methods for some of the commands.
 */
public class CommandListenerHelper {


    /**
     * Prints all available emotes in the server.
     * @param e GuildMessageReceivedEvent
     * @return String
     */
    static String emotePrint(GuildMessageReceivedEvent e){

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
    }


    /**
     * Roll a number between 1 and n. n is max, the outer-bound. (ex: 1 and 100).
     * @param max int
     * @return int
     */
    static int roll(int max){

        Random rn = new Random();
        int answer = rn.nextInt(max)+1;
        return answer;

    }


    /**
     * Referencing osu! API documentation on Game Mods.
     * @param code int
     * @return String
     */
    static String osuModReader(int code) {

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

    }


    /**
     * Referencing osu! API documentation on Approval.
     * @param status int
     * @return String
     */
    static String osuApprovedReader(int status) {

        switch(status) {
            case 4:
                return "Loved";
            case 3:
                return "Qualified";
            case 2:
                return "Approved";
            case 1:
                return "Ranked";
            case 0:
                return "Pending";
            case -1:
                return "WIP";
            case -2:
                return "Graveyard";
            default:
                return null;
        }

    }

}
