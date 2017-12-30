import javax.swing.*;

public class BotGUI{
	
	JFrame frame;
	
	BotGUI(){
		initGUI();
	}
	
	private void initGUI(){
		frame = new JFrame();
		frame.setSize(400,300);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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