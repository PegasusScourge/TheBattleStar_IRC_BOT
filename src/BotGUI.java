import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BotGUI implements ActionListener{
	
	/*
	
	frame: boxLayout1
		- northPanel: boxLayout2, height=120
			- sendBotCommandGenericButton
			- other specific bot commands
	*/
	
	private Bot bot;
	
	private JFrame frame;
	private BoxLayout frameLayout;
	
	private JPanel northPanel;
	private BoxLayout nPanelLayout;
	
	private JButton closeBot;
	private JButton resetBot;
	private JButton customCommand;
	private JButton sendMessage;
	
	BotGUI(Bot parent){
		bot = parent;
		initGUI();
	}
	
	private void initGUI(){
		//Init each component
		frame = new JFrame("TheBattleStar IRC Bot");
		frameLayout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
		frame.getContentPane().setLayout(frameLayout);
		
		northPanel = new JPanel();
		nPanelLayout = new BoxLayout(northPanel, BoxLayout.X_AXIS);
		northPanel.setLayout(nPanelLayout);
		northPanel.setSize(450, 100);
		northPanel.setVisible(true);
		
		closeBot = new JButton("Close Bot");
		closeBot.setVisible(true);
		closeBot.addActionListener(this);
		closeBot.setActionCommand("button:closeBot:clicked");
		
		resetBot = new JButton("Reset Bot");
		resetBot.setVisible(true);
		resetBot.addActionListener(this);
		resetBot.setActionCommand("button:resetBot:clicked");
		
		customCommand = new JButton("Custom command");
		customCommand.setVisible(true);
		customCommand.addActionListener(this);
		customCommand.setActionCommand("button:customCommand:clicked");
		
		sendMessage = new JButton("Send Msg");
		sendMessage.setVisible(true);
		sendMessage.addActionListener(this);
		sendMessage.setActionCommand("button:sendMessage:clicked");
		
		//Do the additions steps
		//	- northPanel
		northPanel.add(sendMessage);
		northPanel.add(customCommand);
		northPanel.add(resetBot);
		northPanel.add(closeBot);
		
		//- frame
		frame.add(northPanel);
		
		//Something else?
		
		//Finalise frame
		frame.pack();
		frame.setSize(450,100);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		long when = e.getWhen();
		String cc;
		String tcmd;
		switch(cmd){
			case "button:customCommand:clicked":
				//Create a dialog getting command
				bot.log("Custom command request detected");
				cc = getUserInput("Custom Command", "Input an IRC channel to execute command in:");
				tcmd = getUserInput("Custom Command", "Input the command to execute:");
				bot.simulateOnMessage(cc,"TheBattleStar",tcmd);
			break;
			
			case "button:sendMessage:clicked":
				//Bring up the send message dialog
				bot.log("Send message request detected");
				cc = getUserInput("Custom Command", "Input an IRC channel to send to:");
				tcmd = getUserInput("Custom Command", "Input the text to send:");
				bot.simulateOnMessage("#TheBattleStar_admin","TheBattleStar","!bs_type " + cc + " " + tcmd);
			break;
			
			case "button:closeBot:clicked":
				//Close the bot
				bot.log("Close bot request detected");
				bot.simulateOnMessage("#TheBattleStar_admin","TheBattleStar","!bs_exit");
			break;
			
			case "button:resetBot:clicked":
				//Close the bot
				bot.log("Reset bot request detected: NOT OKAY");
				//bot.simulateOnMessage("#TheBattleStar_admin","TheBattleStar","!bs_reloadcfg");
			break;
			
			default:
				bot.log("Unknown event command recieved at " + when + ": \"" + cmd + "\"");
			break;
		}
	}
	
	private final String getUserInput(String title, String question){
		String s = (String)JOptionPane.showInputDialog(
                    null,
                    question,
                    title,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    null,
                    "");
		return s;
	}
	
	public final void hideWindow(){
		frame.setVisible(false);
	}
	
	public final void showWindow(){
		frame.setVisible(true);
	}
	
	private void closeGUI(){
		frame.dispose();
	}
	
	public final void dispose(){
		closeGUI();
	}
}