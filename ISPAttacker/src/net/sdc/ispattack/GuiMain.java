package net.sdc.ispattack;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.JFrame;

public class GuiMain  extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Timer timer = new Timer();
	private Label ISPLabel = new Label("ISP Twitter Handle:"), 
			AdvertisedDownloadSpeedLabel = new Label("Advertised Download(Mbps):"), 
			lastDownloadSpeedLabel = new Label("Last Download Speed Test (Mbps):"),
			waitLabel = new Label("Wait time between checks(Minutes):");
	private TextField ISPField, AdvertisedDownloadSpeedField, lastDownloadSpeedField,
			waitField;
	private Button StartStopButton; 
	
	private boolean running = false;
	
	public GuiMain()
	{
		this.setLayout(new FlowLayout());
		this.setSize(400, 200);
		
		this.add(ISPLabel);
		
		ISPField = new TextField(20);
		this.add(ISPField);
		
		this.add(waitLabel);
		
		waitField = new TextField(10);
		this.add(waitField);
		
		this.add(AdvertisedDownloadSpeedLabel);
		
		AdvertisedDownloadSpeedField = new TextField(10);
		this.add(AdvertisedDownloadSpeedField);
		
		this.add(lastDownloadSpeedLabel);
		
		lastDownloadSpeedField = new TextField(10);
		lastDownloadSpeedField.setEditable(false);
		this.add(lastDownloadSpeedField);
		
		MouseListener lis = new StartStopListener(this);
		StartStopButton = new Button("Start");
		StartStopButton.addMouseListener(lis);
		this.add(StartStopButton);
		
		this.setVisible(true);
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        System.exit(0);
		    }
		});
		
	}
	
	public double getWaitTime()
	{
		return Double.parseDouble(waitField.getText()) * 60000;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void validateLL() throws IllegalArgumentException
	{
		String isp = ISPField.getText();
		if(!isp.startsWith("@")) throw new IllegalArgumentException("Invalid twitter handle format");
		if(isp.trim().isEmpty()) throw new IllegalArgumentException("Twitter handle is empty");
		try {
			Double.parseDouble(waitField.getText());
			Double.parseDouble(AdvertisedDownloadSpeedField.getText());
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("Wait time or Download speed is not a valid number.");
		}
	}
	
	public void switchRunning() throws IllegalArgumentException
	{
		validateLL();
		
		running = !running;
		
		if(running)
		{
			
			StartStopButton.setLabel("Stop");
			ISPField.setEditable(false);
			AdvertisedDownloadSpeedField.setEditable(false);
			waitField.setEditable(false);

			timer = new Timer();
			timer.schedule(new TestingThread(this), 0, (long) getWaitTime());
			
		}else
		{
			StartStopButton.setLabel("Start");
			ISPField.setEditable(true);
			AdvertisedDownloadSpeedField.setEditable(true);
			waitField.setEditable(true);
			
			timer.cancel();
		}
		
	}
	
	public boolean isNearAdvertisedDownload(double speed)
	{
		double ad = Double.parseDouble(AdvertisedDownloadSpeedField.getText());
		ad = ad * .90;
		
		return speed >= ad;
	}
	
	public String getHandle()
	{
		return ISPField.getText();
	}
	
	public void setRecentDownload(double speed)
	{
		lastDownloadSpeedField.setText(speed + "");
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
}

