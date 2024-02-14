import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager
{
	
    List<File> fileList = new ArrayList<>();

    String directoryPath = "src/Audio";
    
    int fileIndex = 0; 
    
    AudioInputStream stream;
    Clip clip;
    

    AudioManager()
    {
    	ReadFileList();
    }
    
    private void ReadFileList()
    {
        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory()) 
        {
            // Get the list of files in the directory
            File[] files = directory.listFiles();

            // Add files to the list
            for (File file : files) 
            {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".wav")) 
                {
                	fileList.add(file);
                }            
            }
        } 
        else 
        {
            System.out.println("Directory does not exist or is not a directory.");
        }

    }
    
    public List<File> GetFiles()
    {
    	return fileList;
    }
    
    public void PlayWavFile() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
    	if (clip != null)
    	{
    		clip.stop();
    		clip.close();    		
    	}
    	if (stream != null)
    	{
    		stream.close();
    	}
    	
    	stream = AudioSystem.getAudioInputStream(fileList.get(fileIndex));
    	clip = AudioSystem.getClip();
    	clip.open(stream);
    	
    	clip.start();
    	
    	Control[] controls = clip.getControls();
    	for (Control c : controls)
    	{
    		System.out.print(c.getType());
    	}
    }
    
    public void Stop()
    {
    	clip.stop();
    	clip.close();
    }
    
    public void Position(long position)
    {
    	clip.setMicrosecondPosition(position);
    }
    
    public void NextSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
    	Stop();
    	fileIndex++;
    	if (fileIndex > fileList.size() -1)
    	{
    		fileIndex = 0;
    	}
    	
    	PlayWavFile();
    }
    
    public void PreviousSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
    	Stop();
    	fileIndex--;
    	if (fileIndex < 0)
    	{
    		fileIndex = fileList.size() - 1;
    	}
    	
    	PlayWavFile();
    }
    
    public void Pause()
    {
    }
    
    public void SetVolume(float level) throws LineUnavailableException, IOException
    {
//        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        if (volume != null) {
//            volume.setValue(level / 100.0f);     
//        }
    }
    
    public long GetClipLength()
    {
    	return clip.getMicrosecondLength();
    }
    
    public long GetClipPosition()
    {
    	return clip.getMicrosecondPosition();
    }
    
    public void SetClipPosition(long fileIndex)
    {
    	clip.setMicrosecondPosition(fileIndex);
    }
    
 
    public String GetCurrentTitle()
    {
    	return fileList.get(fileIndex).getName();
    }
}
