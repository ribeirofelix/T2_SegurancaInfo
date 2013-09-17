import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
	
	public static void UpdateFileListDigest(String pathToListDigestFile, Map<String, String> map){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(pathToListDigestFile));
			List<String> lines = new ArrayList<String>();
			
			String fileLine;
			while ((fileLine = reader.readLine()) != null){
				
				lines.add(fileLine);				
			}
			
			reader.close();			
			
			List<String> fileContent = new ArrayList<String>();
			for (String line : lines) {
				
				String[] args = line.split(" ");
				if(map.containsKey(args[0])){
					
				    fileContent.add(line.concat(" " + map.get(args[0])));
				}
				else{
					fileContent.add(line);
				}
				
				map.keySet().remove(args[0]);
			}			
			
			for (String key : map.keySet()) {
				fileContent.add(key + " " + map.get(key));
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(pathToListDigestFile, false));
			for (String line : fileContent) {
				writer.write(line + System.getProperty("line.separator"));
			}
			
			writer.close();			
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Map<String, HashMap<String, String>> getDigestMap(String filePath){
	
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
			System.out.println("");
			System.exit(1);
			
			return null;
		}
	}
	
	private Map<String, byte[]> GenerateMapByteFile(){
		this.map = new HashMap<String, byte[]>();
		
		for (String path : this.paths){
			this.map.put(GetNameFromPath(path), this.readFile(path));
		}
		
		return this.map;
	}
	
	private byte[] readFile(String path){
		File file = new File(path);
		
		byte[] fileBytes = getBytes(file);
		
		return fileBytes;
	}
	
	private static String GetNameFromPath(String path){
		int lastSlashIndex = path.replaceAll("\\", "/").lastIndexOf("/");
		
		return lastSlashIndex >= 0 ? path : path.substring(lastSlashIndex + 1);
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




