import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioManagerMP3
{
	
    List<File> fileList = new ArrayList<>();

    String directoryPath = "src/Audio";
    
    int fileIndex = 0; 
   

    AudioManagerMP3()
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
                if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) 
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
    
}
