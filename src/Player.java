import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

 
public class Player extends JFrame implements ActionListener, ChangeListener
{
	AudioManager audioManager;
		
	JLabel volumeLabel, currentSongLabel, scrubberLabel, currentSong;
	JLabel volumeValue, scrubberValue;
	JButton playButton, stopButton, prevButton, nextButton;
	JSlider volumeSlider, scrubberSlider;

	enum PlayerState { STOPPED, PLAYING, PAUSED }
	
	PlayerState playerState = PlayerState.STOPPED;
	
    private Timer scrubberTimer;
    private int scrubberInterval = 100;
    
    long clipLengthMicroseconds;
    long clipLengthSeconds;
    long lengthMinutes;
    long lengthSeconds;
    String formattedLengthMinutes;
    String formattedLengthSeconds;
    String formattedLengthTime;
    
    long clipPositionMicroseconds;
    long clipPositionSeconds;
    long positionMinutes;
    long positionSeconds;
    String formattedPositionMinutes;
    String formattedPositionSeconds;
    String formattedPositionTime;
    
    JMenuBar menuBar;
    JMenu fileMenu, helpMenu;
    JMenuItem songListItem, exitItem, aboutItem;
    
	Player(AudioManager audioManager) throws IOException
	{
		this.audioManager = audioManager;
		
		this.setTitle("Music Player");
		this.setSize(400, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.cyan);
		this.setResizable(false);
		this.setLayout(null);
        Image originalImage = ImageIO.read(new File("src/logo.png")).getScaledInstance(10, 10, Image.SCALE_SMOOTH);

		ImageIcon image = new ImageIcon(originalImage);
				
		// Menu Bar
		
		menuBar = new JMenuBar();
		
		// File menu
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);  // alt/option-F
		
		songListItem = new JMenuItem("Song List");
		songListItem.setMnemonic(KeyEvent.VK_L);  //  L for List
		songListItem.addActionListener(this);

		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);  // X for Exit
		exitItem.addActionListener(this);
		
		fileMenu.add(songListItem);
		fileMenu.add(exitItem);
		
		// Help menu
		
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);  // alt/option-H
		
		aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic(KeyEvent.VK_A);  // A for About
		aboutItem.addActionListener(this);

		helpMenu.add(aboutItem);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		
		this.setJMenuBar(menuBar);
		
		// Player Items
		
		currentSongLabel = new JLabel("Current Song");
		currentSongLabel.setBounds(10,0,100,25);
		this.add(currentSongLabel);
		
		currentSong = new JLabel(audioManager.getCurrentTitle());
		currentSong.setBounds(115,0,300,25);
		this.add(currentSong);
		
		// Player Buttons
		
		playButton = new JButton("Play");
		playButton.setBounds(0,30,100,25);
		playButton.addActionListener(this);
		playButton.setFocusable(false);
		this.add(playButton);
		
		stopButton = new JButton("Stop");
		stopButton.setBounds(100,30,100,25);
		stopButton.addActionListener(this);
		stopButton.setFocusable(false);
		this.add(stopButton);
		
		prevButton = new JButton("Prev");
		prevButton.setBounds(200,30,100,25);
		prevButton.addActionListener(this);
		prevButton.setFocusable(false);
		this.add(prevButton);
		
		nextButton = new JButton("Next");
		nextButton.setBounds(300,30,100,25);
		nextButton.addActionListener(this);
		nextButton.setFocusable(false);
		this.add(nextButton);
		
		// Volume Slider
		
		volumeLabel = new JLabel("Volume");
		volumeLabel.setBounds(10,60,100,25);
		this.add(volumeLabel);
		
		volumeSlider = new JSlider(0, 100, 50);
		volumeSlider.setBounds(100, 60, 200, 40);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setMinorTickSpacing(5);
		volumeSlider.setPaintTrack(true);
		volumeSlider.setMajorTickSpacing(25);
		volumeSlider.setPaintLabels(true);
		volumeSlider.addChangeListener(this);
		this.add(volumeSlider);

		volumeValue = new JLabel("50%");
		volumeValue.setBounds(300,60,100,25);
		this.add(volumeValue);
		
		// Scrubber Slider
		
		scrubberLabel = new JLabel("Scrubber");
		scrubberLabel.setBounds(10,110,100,25);
		this.add(scrubberLabel);
		
		scrubberSlider = new JSlider();
		scrubberSlider.setBounds(100, 110, 200,40);
		scrubberSlider.setPaintTicks(true);
		scrubberSlider.setMinorTickSpacing(5);
		scrubberSlider.setPaintTrack(true);
		scrubberSlider.setMajorTickSpacing(25);
		scrubberSlider.setPaintLabels(true);
		scrubberSlider.addChangeListener(this);
		this.add(scrubberSlider);
		
		scrubberValue = new JLabel("0:00/0:00");
		scrubberValue.setBounds(300,110,100,25);
		this.add(scrubberValue);
		

		
		
		this.setVisible(true);

		initializeScrubber();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("entering actionPerformed");

		Object src = e.getSource();
		
		if (src == playButton)
		{
			try
			{
				stopScrubberTimer();
				audioManager.playWavFile();
				initializeScrubber();
				startScrubberTimer();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		if (src == stopButton)
		{
			audioManager.stop();
			stopScrubberTimer();
		}
		else
		if (src == nextButton)
		{
			try
			{
				stopScrubberTimer();
				audioManager.nextSong();
				initializeScrubber();
				startScrubberTimer();
				currentSong.setText(audioManager.getCurrentTitle());
				this.repaint();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		if (src == prevButton)
		{
			try
			{
				stopScrubberTimer();
				audioManager.previousSong();
				initializeScrubber();
				startScrubberTimer();
				currentSong.setText(audioManager.getCurrentTitle());
				this.repaint();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		if (src == songListItem)	
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setAcceptAllFileFilterUsed(false);


	        // Create a file filter to only allow image files to be selected.
	        FileFilter fileFilter = new FileFilter() {
	            @Override
	            public boolean accept(File file) {
	                return file.isDirectory() || file.getName().toLowerCase().endsWith(".wav");
	            }
	            
	            @Override
	            public String getDescription() {
	                return "wav Files";
	            }
	        };
	        
	        fileChooser.addChoosableFileFilter(fileFilter);
	        
			int resultFileChooser = fileChooser.showOpenDialog(this);

			if (resultFileChooser == JFileChooser.APPROVE_OPTION)
			{
				File[] files = fileChooser.getSelectedFiles();
				
				if (files.length > 0)
				{
					audioManager.setFiles(files);
				}

			}
		}
		else
		if (src == exitItem)	
		{
			System.exit(0);
		}
		else
		if (src == aboutItem)	
		{
		
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		System.out.println("entering stateChanged");

		Object src = e.getSource();
		long value;
		
		if (src == volumeSlider) 
		{
			value = volumeSlider.getValue();
			try
			{
				audioManager.setVolume(value);
			} catch (LineUnavailableException | IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			volumeValue.setText(Long.toString(value) + "%");
			this.repaint();
		}
		else
		if (src == scrubberSlider)
		{
			value = scrubberSlider.getValue();
			audioManager.setClipPosition(clipLengthMicroseconds * value/100);
		}
	}
	
	
	void initializeScrubber()
	{
		System.out.println("entering initializeScrubber");
        clipLengthMicroseconds = audioManager.getClipLength(); // Example clip length in microseconds
        
        // Convert microseconds to seconds
        clipLengthSeconds = clipLengthMicroseconds / 1_000_000;

        // Calculate minutes and seconds
        lengthMinutes = clipLengthSeconds / 60;
        lengthSeconds = clipLengthSeconds % 60;

        // Format minutes and seconds to ensure they are two digits long
        formattedLengthMinutes = String.format("%02d", lengthMinutes);
        formattedLengthSeconds = String.format("%02d", lengthSeconds);

        // Concatenate minutes and seconds with a colon
        formattedLengthTime = formattedLengthMinutes + ":" + formattedLengthSeconds;
        
        
        
        
        clipPositionMicroseconds = audioManager.getClipPosition(); // Example clip length in microseconds
        
        // Convert microseconds to seconds
        clipPositionSeconds = clipPositionMicroseconds / 1_000_000;

        // Calculate minutes and seconds
        positionMinutes = clipPositionSeconds / 60;
        positionSeconds = clipPositionSeconds % 60;

        // Format minutes and seconds to ensure they are two digits long
        formattedPositionMinutes = String.format("%02d", positionMinutes);
        formattedPositionSeconds = String.format("%02d", positionSeconds);

        // Concatenate minutes and seconds with a colon
        formattedPositionTime = formattedPositionMinutes + ":" + formattedPositionSeconds;
        
		
		scrubberSlider.removeChangeListener(this);
        scrubberSlider.setValue((int)(100 * (audioManager.getClipPosition()/audioManager.getClipLength())));
		scrubberSlider.addChangeListener(this);
		
		scrubberValue.setText(formattedPositionTime + "/" + formattedLengthTime);

	}
	
	void startScrubberTimer()
	{
		System.out.println("entering startScrubberTimer");
		
		scrubberTimer = new Timer();
		scrubberTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateScrubber();
            }
        }, 0, scrubberInterval);
	}
	
	void updateScrubber()
	{
//		System.out.println("entering updateScrubber");

		scrubberSlider.removeChangeListener(this);
		scrubberSlider.setValue((int)(100 * ((double)audioManager.getClipPosition()/(double)audioManager.getClipLength())));
		scrubberSlider.addChangeListener(this);

        clipPositionMicroseconds = audioManager.getClipPosition(); // Example clip length in microseconds
        
        // Convert microseconds to seconds
        clipPositionSeconds = clipPositionMicroseconds / 1_000_000;

        // Calculate minutes and seconds
        positionMinutes = clipPositionSeconds / 60;
        positionSeconds = clipPositionSeconds % 60;

        // Format minutes and seconds to ensure they are two digits long
        formattedPositionMinutes = String.format("%02d", positionMinutes);
        formattedPositionSeconds = String.format("%02d", positionSeconds);

        // Concatenate minutes and seconds with a colon
        formattedPositionTime = formattedPositionMinutes + ":" + formattedPositionSeconds;
        
		scrubberValue.setText(formattedPositionTime + "/" + formattedLengthTime);

	}
	
	void stopScrubberTimer()
	{
		System.out.println("entering stopScrubberTimer");

		if (scrubberTimer != null) {
        	scrubberTimer.cancel();
        	scrubberTimer = null;
        }
	}
	
	void rePaint()
	{
		System.out.println("entering rePaint");

		this.rePaint();
	}
	
}
