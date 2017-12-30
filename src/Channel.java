import java.util.ArrayList;

public class Channel{
	
	public static int TYPE_NORMAL = 1;
	public static int TYPE_ADMIN = 0;
	
	public Bot bot;
	
	private String name = "default";
	
	private boolean locked = false;
	private boolean reminderChannel = false;
	
	private String reminderText = "";
	
	private int channelType = Channel.TYPE_NORMAL;
	
	private Commands commands;
	
	private boolean configured = false;
	private boolean connected = false;
	
	private String admins[] = {"PegasusScourge", "PegasusScourge_IRC"};
	
	//INIT METHODS
	
	Channel(Bot b, String name){
		bot = b;
		locked = false;
		this.name = name;
		reminderChannel = false;
		channelType = Channel.TYPE_NORMAL;
		
		commands = new Commands(this);
	}
	
	Channel(Bot b, String name, boolean lock){
		bot = b;
		locked = lock;
		this.name = name;
		reminderChannel = false;
		channelType = Channel.TYPE_NORMAL;
		
		commands = new Commands(this);
	}
	
	Channel(Bot b, String name, boolean lock, boolean isRemind, String remindT){
		bot = b;
		locked = lock;
		this.name = name;
		reminderChannel = isRemind;
		reminderText = remindT;
		channelType = Channel.TYPE_NORMAL;
		
		commands = new Commands(this);
	}
	
	public void configure(){
		//Do anything needed for configuration
		
		configured = true;
	}
	
	//MAIN METHODS
	public void executeCommand(String sender, String login, String hostname, String message){
		
		//Implement blocked users check
		commands.executeCommand(sender, login, hostname, message);
		
	}
	
	public void dispose(){
		disconnect();
	}
	
	public void connect(){
		if(configured){
			//Connects to this channel
			bot.joinChannel(name);
			connected = true;
		}else{
			bot.throwError("Channel failed to connect as configuration was not completed");
		}
	}
	public void disconnect(){
		if(connected){
			//Disconnects from this channel
			bot.partChannel(name);
			connected = false;
		}
	}
	
	//GETTERS
	public boolean isLocked(){
		return locked;
	}
	public boolean isConfigured(){
		return configured;
	}
	public boolean isReminderChannel(){
		return reminderChannel;
	}
	public String getReminderText(){
		return reminderText;
	}
	public int getChannelType(){
		return channelType;
	}
	public String getChannelName(){
		return name;
	}
	public String[] getAdmins(){
		return admins;
	}
	
	//SETTERS
	public void setLocked(boolean l){
		locked = l;
	}
	public void setReminderChannel(boolean r){
		reminderChannel = r;
	}
	public void setReminderText(String t){
		reminderText = t;
	}
	public void setChannelType(int t){
		channelType = t;
	}
	public void setChannelName(String s){
		name = s;
	}

}