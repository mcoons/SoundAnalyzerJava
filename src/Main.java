import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main
{

	public static void main(String[] args) 
	throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		
		AudioManager audioManager = new AudioManager();
		AudioManagerMP3 audioManagerMP3 = new AudioManagerMP3();
		
		Player player = new Player(audioManager);

//        for (File file : audioManager.GetFiles()) 
//        {
//            System.out.println(file.getName());
//        }	
//        
//        for (File file : audioManagerMP3.GetFiles()) 
//        {
//            System.out.println(file.getName());
//        }	
        
//        audioManager.PlayWavFile();
        
        
//		Scanner scanner = new Scanner(System.in);

//        String response = "";
//        
//        while (!response.equals("Q"))
//        {
//        	System.out.println("(P)lay, (S)top, p(R)evious, (N)ext, (Q)uit");
//        
//        	response = scanner.next();
//        	response = response.toUpperCase();
//        	
//        	switch (response)
//        	{
//        		case ("P"):
//        			audioManager.PlayWavFile();
//        		break;
//        		case ("S"):
//        			audioManager.Stop();
//        		break;
//        		case ("R"):
//        			audioManager.PreviousSong();
//        		break;
//        		case ("N"):
//        			audioManager.NextSong();
//        		break;
//        	}
//        
//        }
//        
//        scanner.close();
        
    }

}
