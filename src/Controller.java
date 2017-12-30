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
		bot.logBot.dispose();
		bot.dispose();
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
	
	public static void exitBot(){
		disconnectBot();
		bot.logBot.dispose();
		bot.gui.dispose();
		bot.dispose();
		System.exit(0);
	}
	
}