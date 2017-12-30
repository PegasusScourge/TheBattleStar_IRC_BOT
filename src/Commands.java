import org.jibble.pircbot.User;
import java.util.ArrayList;

public class Commands{
	
	Commands(Channel p){
		parent = p;
		channel = parent.getChannelName();
	}
	
	private Channel parent;
	private String channel;
	
	private int name_said_repeats = 0;
	
	private long lastPingTime = 0;
	private long lastRamTime = 0;
	private long lastLennyTime = 0;
	private long lastPointsTime = 0;
	
	private long ramTimeOut = 60000;
	private long pointsTimeOut = 120000;
	private long lennyTimeOut = 120000;
	private long pingTimeOut = 30000;
	
	public boolean isAdminChannel(){
		return (parent.getChannelType() == Channel.TYPE_ADMIN);
	}
	
	public boolean isAdmin(String person){
		String admins[] = parent.getAdmins();
		for(int i=0; i < admins.length; i++){
			if(person.equals(admins[i])){
				return true;
			}
		}
		return false;
	}
	
	public boolean isNotLockedChannel(){
		return !parent.isLocked();
	}
	
	public void executeCommand(String sender, String login, String hostname, String message){
		try{
			//------------------------------------------------------------------- START ADMIN CHANNEL -------------------------------
			if(isAdminChannel()){
				checkAdminCommands(sender, login, hostname, message);
			} //------------------------------------------------------------------- END ADMIN CHANNEL -------------------------------
			
			checkNormalCommands(sender, login, hostname, message);
			
			//------------------------------------------------------------------- CHAT REACTION -------------------------------
			reactToChat(sender, login, hostname, message);
			
		}catch(Exception e){
			parent.bot.throwError("Error in TheBattleStar command execution: \"" + e.getMessage() + "\", " + e.toString());
		}
	}
	
	private void checkNormalCommands(String sender, String login, String hostname, String message){
		//Command: ram [name]
		//Type = normal
		if(message.startsWith("!ram")){
			if(System.currentTimeMillis() > (lastRamTime + ramTimeOut)){
				if(message.split(" ").length != 2){
					parent.bot.sendMessage(channel, "This command needs a name after it! E.g. !ram Chief_Tyrol");
				}else{
					String person = message.split(" ")[1];
					parent.bot.sendAction(channel, "finds " + person + " and accelerates to ramming speed...");
					parent.bot.sendMessage(channel, "You're coming down with me, Cylon scum");
					lastRamTime = System.currentTimeMillis();
				}
			}else{
				parent.bot.sendMessage(sender, "Please don't spam me, " + sender + ". I have a " + (ramTimeOut/1000) + " second cooldown on that command");
			}
		}
		
		//Command: freepoints, summons everyone in the chat
		//Type = normal
		if(message.equals("!freepoints")){
			if(System.currentTimeMillis() > (lastPointsTime + pointsTimeOut)){
				//TODO
				
				User userList[] = parent.bot.getUsers(channel);
				
				parent.bot.sendAction(channel, "With great enthusiasm and a loud voice, " + sender + " summons the clan of [ONI] to their side to farm some free points!");
				
				String pingList = "";
				for(int i=0; i < userList.length; i++){
					if(!userList[i].getNick().equalsIgnoreCase(parent.bot.getNick())){
						pingList += userList[i].getNick() + " ";
					}
				}
				
				parent.bot.sendMessage(channel, pingList);
				
				lastPointsTime = System.currentTimeMillis();
			}else{
				parent.bot.sendMessage(sender, "Please don't spam me, " + sender + ". I have a " + (pointsTimeOut/1000) + " second cooldown on that command");
			}
		}
			
		//Command: owner
		//Type = normal
		if(message.equals("!whoisowner")){
			parent.bot.sendMessage(channel, "The owner of this bot is " + parent.bot.owner);
			parent.bot.sendMessage(parent.bot.owner, sender + " wanted to know who owns me in " + channel);
		}
		
		//Command: owner
		//Type = normal
		if(message.equals("!lenny")){
			if(System.currentTimeMillis() > (lastLennyTime + lennyTimeOut)){
				parent.bot.sendAction(channel, sender + " summons Lenny from the aether...");
				parent.bot.sendMessage(channel, "( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)( ͡° ͜ʖ ͡°)");
				lastLennyTime = System.currentTimeMillis();
			}else{
				parent.bot.sendMessage(sender, "Please don't spam me, " + sender + ". I have a " + (lennyTimeOut/1000) + " second cooldown on that command");
			}
		}
			
		//Command: channelType
		//Type = normal
		if(message.equals("!channelType") && isNotLockedChannel()){
			parent.bot.sendMessage(channel, "Current channel is Admin channel: " + isAdminChannel());
		}
			
		//Command: ping [name]
		//Type = normal
		if(message.startsWith("!ping") && isNotLockedChannel()){
			if(System.currentTimeMillis() > (lastPingTime + pingTimeOut)){
				if(message.split(" ").length != 2){
					parent.bot.sendMessage(channel, "This command needs a name after it! E.g. !ping TheBattleStar");
				}else{
					parent.bot.sendMessage(channel, message.split(" ")[1] + " come look at what " + sender + " wants to show you...");
					lastPingTime = System.currentTimeMillis();
				}
			}else{
				parent.bot.sendMessage(channel, "Please don't spam me, " + sender + ". I have a " + (pingTimeOut/1000) + " second cooldown on that command");
			}
		}
			
		/* TESTING - REMOVED IN FAVOUR OF NON-TOGGLE ADMIN CHANNEL
		//Command: admin
		//Type = normal, but needs admin permission
		if(message.equals("!admin") && isNotLockedChannel()){
			parent.setChannelType(Channel.TYPE_ADMIN);
			
			if(!isAdmin(sender)){
				parent.bot.sendMessage(parent.bot.owner, "Channel " + channel + " has had Admin mode enabled by " + sender);
				parent.bot.sendMessage(channel, "WARNING: You are not an admin, " + sender + ", so you will not be able to use the admin commands...");
			}
			parent.bot.sendMessage(channel, "This channel is NOW an admin channel.");
		}
		*/
			
		//------------------------------------------------------------------- NORMAL HELP -------------------------------
		//Command: help
		//Type = normal
		if(message.equalsIgnoreCase("!help") && isNotLockedChannel()){
			parent.bot.sendMessage(channel, "Avaliable Commands: !freepoints, !ping, !lenny, !ram, !whoisowner, !channelType");
		}else if(message.equalsIgnoreCase("!help")){
			parent.bot.sendMessage(channel, "Avaliable Commands: !ram, !lenny, !whoisowner");
		}
	}
	
	private void reactToChat(String sender, String login, String hostname, String message){
		if(message.contains(parent.bot.getNick())){
			try{
				Thread.sleep(500);
			}catch(Exception e){
				e.printStackTrace();
			}
				
			switch(name_said_repeats){
				case 1:
					parent.bot.sendMessage(channel, "Excuse me?");
				break;
				
				case 2:
					parent.bot.sendMessage(channel, "I don't understand");
				break;
				
				case 3:
					parent.bot.sendMessage(channel, "What did you say?");
				break;
				
				case 4:
					parent.bot.sendMessage(channel, ".");
				break;
				
				case 5:
					parent.bot.sendMessage(channel, "I'm sorry?");
				break;
				
				case 6:
					parent.bot.sendMessage(channel, "????");
				break;
				
				case 7:
					parent.bot.sendMessage(channel, "Um");
				break;
				
				case 8:
					parent.bot.sendMessage(channel, "...");
				break;
				
				default:
					parent.bot.sendMessage(channel, "Who, me?");
					name_said_repeats = 0;
				break;
			}
			this.name_said_repeats++;
		}
	}
	
	private void checkAdminCommands(String sender, String login, String hostname, String message){
		//Command: bs_exit
		//Type = admin
		if(message.equals("!bs_exit") && isAdmin(sender) && isNotLockedChannel()){
			parent.bot.exitBot();
		}
				
		//Command: bs_listChannels
		//Type = admin
		if(message.equals("!bs_listChannels") && isAdmin(sender) && isNotLockedChannel()){
			String channel_list[] = parent.bot.getChannels();
			parent.bot.sendMessage(channel, "Part of the following channels:");
			for(int i=0; i < channel_list.length; i++){
				parent.bot.sendMessage(channel, channel_list[i]);
			}
		}
		
		//Command: bs_reloadcfg [channels]
		//Type = admin
		if(message.startsWith("!bs_reloadcfg") && isAdmin(sender) && isNotLockedChannel()){
			//Reload cfg
			parent.bot.reloadCfg();
		}
		
		//Command: bs_join [channels]
		//Type = admin
		if(message.startsWith("!bs_join") && isAdmin(sender) && isNotLockedChannel()){
			String c[] = message.split(" ");
			for(int i=1; i < c.length; i++){
				parent.bot.connectToChannel(c[i]);
			}
		}
		
		//Command: bs_leave [channels]
		//Type = admin
		if(message.startsWith("!bs_leave") && isAdmin(sender) && isNotLockedChannel()){
			String c[] = message.split(" ");
			for(int i=1; i < c.length; i++){
				parent.bot.leaveChannel(c[i]);
			}
		}
		
		//Command: bs_leave [channels]
		//Type = admin
		if(message.startsWith("!bs_type") && isAdmin(sender) && isNotLockedChannel()){
			String c[] = message.split(" ");
			parent.bot.sendMessage(c[1], message.split(c[1])[1]);
		}
		
		//Command: bs_act [channels]
		//Type = admin
		if(message.startsWith("!bs_act") && isAdmin(sender) && isNotLockedChannel()){
			String c[] = message.split(" ");
			parent.bot.sendAction(c[1], message.split(c[1])[1]);
		}
		
		//Command: bs_notice [channels]
		//Type = admin
		if(message.startsWith("!bs_notice") && isAdmin(sender) && isNotLockedChannel()){
			String c[] = message.split(" ");
			parent.bot.sendNotice(c[1], message.split(c[1])[1]);
		}
		
		//Command: invade [channel]
		//Type = admin
		if(message.startsWith("!invade") && isAdmin(sender) && isNotLockedChannel()){
			String channel2 = message.split(" ")[1];
			parent.bot.sendAction(channel, "begins invasion of " + channel2);
			parent.bot.sendMessage(channel, "Type ./join " + channel2 + " to join me!");
			
			parent.bot.joinChannel(channel2);
			parent.bot.sendMessage(channel2, "THIS IS AN INVASION BY [ONI]");
			parent.bot.sendMessage(channel2, "PREPARE YOUR ACUs, COMMANDERS!");
		}
				
				
		/* TESTING - REMOVED IN FAVOUR OF NON-TOGGLE ADMIN CHANNELS
		//-------------------------------~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ SUBTYPE - Admin channel but not admin permission ~~~~~~~~~~~~~~~~
		//Command: unadmin
		//Type = admin
		if(message.equals("!unadmin") && isNotLockedChannel()){
			parent.bot.sendMessage(channel, "This channel is NOW NOT an admin channel.");
			parent.setChannelType(Channel.TYPE_NORMAL);
		}
		*/
		
		//------------------------------------------------------------------- ADMIN HELP -------------------------------
		//Command: help
		//Type = admin
		if(message.equalsIgnoreCase("!help") && isNotLockedChannel()){
			parent.bot.sendMessage(channel, "Admin commands: !bs_reloadcfg, !bs_notice, !bs_type, !bs_act, !invade, !bs_join, !bs_leave, !bs_listChannels, !bs_exit");
		}
	}
}