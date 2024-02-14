import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

 
public class Player extends JFrame implements ActionListener, ChangeListener
{
	AudioManager audioManager;
		
	JLabel volumeLabel, currentSongLabel, scrubberLabel, currentSong;
	JLabel volumeValue, scrubberValue;
	JButton playButton, stopButton, prevButton, nextButton;
	JSlider volumeSlider, scrubberSlider;
	
	Player(AudioManager audioManager) throws IOException
	{
		this.audioManager = audioManager;
		
		this.setTitle("Music Player");
		this.setSize(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.cyan);
		this.setResizable(false);
		this.setLayout(null);
        Image originalImage = ImageIO.read(new File("src/logo.png")).getScaledInstance(10, 10, Image.SCALE_SMOOTH);

		ImageIcon image = new ImageIcon(originalImage);
				
	
		
		currentSongLabel = new JLabel("Current Song");
		currentSongLabel.setBounds(10,0,100,25);
		this.add(currentSongLabel);
		
		currentSong = new JLabel(audioManager.GetCurrentTitle());
		currentSong.setBounds(115,0,300,25);
		this.add(currentSong);
		

		
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

		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object src = e.getSource();
		
		if (src == playButton)
		{
			try
			{
				audioManager.PlayWavFile();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		if (src == stopButton)
		{
			audioManager.Stop();
		}
		else
		if (src == nextButton)
		{
			try
			{
				audioManager.NextSong();
				currentSong.setText(audioManager.GetCurrentTitle());
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
				audioManager.PreviousSong();
				currentSong.setText(audioManager.GetCurrentTitle());
				this.repaint();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
				
		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Object src = e.getSource();
		long value;
		
		if (src == volumeSlider) 
		{
			value = volumeSlider.getValue();
			try
			{
				audioManager.SetVolume(value);
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
			
		}
	}
	
	
	
	
	
}
