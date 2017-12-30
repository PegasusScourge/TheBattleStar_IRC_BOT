import org.jibble.pircbot.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Iterator;

public class Bot extends PircBot{

	public Bot(){
		this.setName("TheBattleStar");
	}
	
	private boolean noDisconnectAction = false;
	
	private String lockedChannels[] = {};
	private String adminChannels[] = {};
	private String blockedUsers[] = {};
	private String autoJoin[] = {};
	private String logChannels[] = {};
	
	public String owner = "PegasusScourge";
	
	protected LogBot logBot = null;
	protected BotGUI gui = null;
	
	private ChannelBook book = new ChannelBook();
	
	private int reconnectTime = 15000;
	
	public void BOT_INIT(){
		//Creates logs
		logBot = new LogBot("TheBattleStar", true);
		
		//Creates gui
		gui = new BotGUI(this);
	}
	
	public void runCfgRead(){
		String data[] = new String[0];
		try{
			BufferedReader abc = new BufferedReader(new FileReader(new File("default.cfg")));
			ArrayList<String> lines = new ArrayList<String>();
			
			String line = null;
			while((line = abc.readLine()) != null) {
				lines.add(line);
			}
			abc.close();
			data = lines.toArray(new String[]{});
		}catch(Exception e){
			e.printStackTrace();
			log("Failed to load CFG file, terminating.");
			try{
				Thread.sleep(10000);
			}catch(Exception e2){}
			System.exit(0);
		}
		
		//Parse data
		System.out.println("Reading cfg...");
		for(int i=0; i < data.length; i++){
			String init[] = data[i].split("=");
			
			//if(init.length <= 1){
				//System.out.println("Skipped input " + init[0]);
			//}else{
			if(init.length > 1){
				if(init[0].contains("auto_join")){
					String array[] = init[1].split(",");
					for(int u=0; u < array.length; u++){
						array[u] = array[u].trim();
					}
					log("Autojoin list has size " + array.length);
					autoJoin = array;
				}
				
				if(init[0].contains("log_channels")){
					String array[] = init[1].split(",");
					for(int u=0; u < array.length; u++){
						array[u] = array[u].trim();
					}
					log("Autojoin list has size " + array.length);
					logChannels = array;
				}
				
				if(init[0].contains("blocked_users")){
					String array[] = init[1].split(",");
					for(int u=0; u < array.length; u++){
						array[u] = array[u].trim();
					}
					log("Blockedusers list has size " + array.length);
					blockedUsers = array;
				}
				
				if(init[0].contains("locked_channels")){
					String array[] = init[1].split(",");
					for(int u=0; u < array.length; u++){
						array[u] = array[u].trim();
					}
					log("Lockedchannels list has size " + array.length);
					lockedChannels = array;
				}
				
				if(init[0].contains("admin_channels")){
					String array[] = init[1].split(",");
					for(int u=0; u < array.length; u++){
						array[u] = array[u].trim();
					}
					
					log("Adminchannels list has size " + array.length);
					adminChannels = array;
				}
			}
		}
		log("Read complete");
	}
	
	public void runConnect(){
		try{
			//Sets the PircBot to be verbose
			this.setVerbose(true);
			
			this.connect("irc.faforever.com");
			
			//Do a verbose log of channels
			this.listChannels();
			
			if(autoJoin.length <= 0){
				//Fail the bot
				throwError("There are no autojoin channels: The bot is useless");
				exitBot();
			}else{
				//Do autojoins
				for(int i=0; i < autoJoin.length; i++){
					boolean shouldLock = false;
					boolean shouldAdmin = false;
					
					for(int y=0; y < lockedChannels.length; y++){
						if(lockedChannels[y].equalsIgnoreCase(autoJoin[i])){
							shouldLock = true;
						}
					}
					for(int y=0; y < lockedChannels.length; y++){
						if(adminChannels[y].equalsIgnoreCase(autoJoin[i])){
							shouldAdmin = true;
						}
					}
					log("Run connect: " + autoJoin[i] + ", startsLocked = " + shouldLock + ", shouldAdmin = " + shouldAdmin);
					Channel c = new Channel(this, autoJoin[i]);
					c.configure();
					c.connect();
					c.setLocked(shouldLock);
					if(shouldAdmin){
						c.setChannelType(Channel.TYPE_ADMIN);
					}else{
						c.setChannelType(Channel.TYPE_NORMAL);
					}
		
					book.addChannel(c);
				}
			}
			
			//this.setLogChannel("#TheBattleStar_admin");
		}catch(Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}
    
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		Channel cs[] = book.getChannels();
		for(int i=0; i < cs.length; i++){
			if(cs[i] != null){
				if(cs[i].getChannelName().equalsIgnoreCase(channel)){
					//Execute the command according to the channel:
				
					cs[i].executeCommand(sender, login, hostname, message);
				}
			}
		}
    }
	
	public void simulateOnMessage(String channel, String sender, String message){
		String login = "default";
		String hostname = "default";
		
		Channel cs[] = book.getChannels();
		for(int i=0; i < cs.length; i++){
			if(cs[i] != null){
				if(cs[i].getChannelName().equalsIgnoreCase(channel)){
					//Execute the command according to the channel:
				
					cs[i].executeCommand(sender, login, hostname, message);
				}
			}
		}
	}
	
	public void onJoin(String channel, String sender, String login, String hostname){
		// Void
	}
	
	public void reloadCfg(){
		log("WARNING: A reload of the cfg has been requested. Leaving all channels and disconnecting from server");
		
		resetBot();
	}
	
	public boolean isInChannel(String channel){
		//NOTE: Uses the IRC integration for this
		
		String channel_list[] = getChannels();
		for(int i=0; i < channel_list.length; i++){
			if(channel_list[i].equalsIgnoreCase(channel)){
				return true;
			}
		}
		return false;
	}
	
	public void connectToChannel(String ch){
		//Check for the channel already existing:
		if(isInChannel(ch)){
			log("Already in channel");
			return;
		}
		
		Channel c = new Channel(this, ch);
		
		c.configure();
		c.connect();
		
		book.addChannel(c);
	}
	
	public void leaveChannel(String ch){
		Channel cs[] = book.getChannels();
		for(int i=0; i < cs.length; i++){
			if(cs[i] != null){
				if(cs[i].getChannelName().equalsIgnoreCase(ch)){
					book.removeChannel(i);
				}
			}
		}
	}
	
	public void resetBot(){
		book.clearChannels();
		Controller.recreateBot();
	}
	public void exitBot(){
		book.clearChannels();
		Controller.exitBot();
	}
	
	public void throwError(String e){
		//Do something here
		log("[ERROR] " + e);
		//sendMessage(logChannel, e);
	}
	
	public void onDisconnect(){
		if(noDisconnectAction){
			log("Disconnected, no action taken");
			return;
		}
		
		log("Got disconnected, attempting to reconnect in " + (reconnectTime/1000) + " seconds");
		try{
			Thread.sleep(reconnectTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		log("Reconnecting...");
		
		runConnect();
	}
	
	public void log(String txt){
		logBot.log(txt);
	}
}