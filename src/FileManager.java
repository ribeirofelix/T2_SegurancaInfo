import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileManager {
	
	private List<String> paths;
	private Map<String,byte[]> map;
	
	public FileManager (List<String> paths){
		this.paths = paths;
		this.map = null;
	}
	
	public Map<String, byte[]> getMapByteFile(){
		return this.map != null ? this.map : this.GenerateMapByteFile();		
	}
	
	public static Map<String, HashMap<String, String>> getDisgestMap(String filePath){
	
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			
			Map<String, HashMap<String, String>> digestMap = new HashMap<String, HashMap<String,String>>();
			String fileLine;
						
			while ((fileLine = reader.readLine()) != null)
			{
				String[] args = fileLine.split(" ");
				for (int i = 0; i < args.length; i++)
				{
					HashMap<String, String> typeHexa = new HashMap<String, String>();	
					String fileName = args[i];
					typeHexa.put(args[++i], args[++i]);
					
					// Tem dois digest
					if (args[i+1] == "MD5" || args[i+1] == "SHA"){
						typeHexa.put(args[++i], args[++i]);
					}
					
					digestMap.put(fileName, typeHexa);
				}
			}
			
			reader.close();
			return digestMap;
		}
		catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private Map<String, byte[]> GenerateMapByteFile(){
		this.map = new HashMap<String, byte[]>();
		
		for (String path : this.paths){
			this.map.put(path, this.readFile(path));
		}
		
		return this.map;
	}
	
	private byte[] readFile(String path){
		File file = new File(path);
		
		byte[] fileBytes = getBytes(file);
		
		return fileBytes;
	}
	
	private byte[] getBytes (File file){
		try{
			 return Files.readAllBytes(file.toPath());
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
}




