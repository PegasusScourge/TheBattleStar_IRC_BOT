import org.jibble.pircbot.*;

public class Controller{
	
	private static Bot bot = null;
	
	public static void main(String[] args) throws Exception{
		createBot();
    }
	
	public static void createBot(){
		bot = new Bot();
        bot.BOT_INIT();
		bot.runCfgRead();
        bot.runConnect();
	}
	
	public static void recreateBot(){
		disconnectBot();
		cleanBot();
		bot = null;
		createBot();
	}
	
	private static void disconnectBot(){
		try{
			bot.disconnect();
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Bot exit failure");
		}
	}
	
	private static void cleanBot(){
		bot.logBot.dispose();
		bot.gui.dispose();
		bot.dispose();
	}
	
	public static void exitBot(){
		disconnectBot();
		cleanBot();
		System.exit(0);
	}
	
}