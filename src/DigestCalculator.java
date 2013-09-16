
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DigestCalculator {

	private MessageDigest msgDigest ;
	private  Map<String, byte[]> byteArrayFileMap ;
	private Map<String,String> calcDigest ;
	
	public DigestCalculator(String typeMsg , Map<String, byte[]> byteArrayFileMap ){
		
		try {
			this.msgDigest = MessageDigest.getInstance(typeMsg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		this.byteArrayFileMap = byteArrayFileMap;
		
	}
	
	public void Calculate(){
		
		calcDigest = new HashMap<String, String>();
		
		String hexaDig = null;
		byte[] byteArray = null ;
		for (String fileName : this.byteArrayFileMap.keySet()) {
			
			byteArray = this.byteArrayFileMap.get(fileName);
			this.msgDigest.update(byteArray, 0 , byteArray.length ) ;
			
			hexaDig = convertToHex(this.msgDigest.digest());
			
			calcDigest.put(fileName, hexaDig);			
		
		}		
		
	}
	

	
	public Map<String,String> CheckDigest(Map<String,HashMap<String,String>> knownDigests) {
		
		List<String> outPuts = new ArrayList<String>();
		String typeDigest = this.msgDigest.getAlgorithm();
		Map< String , String  > mapReturn = new HashMap<String,String >();
		
		
		
		
		/*For each calculated digest */
		for(String fileName : this.calcDigest.keySet()){
			
			String hexaCalcDigst = this.calcDigest.get(fileName);
			
			
			
			/* First of all : verify collision */
			for(java.util.Map.Entry<String, HashMap<String,String>>  entryDigest : knownDigests.entrySet() ){
				if(!entryDigest.getKey().equals(fileName)){
					if(entryDigest.getValue().containsKey(typeDigest)
					  && entryDigest.getValue().get(typeDigest).equals(hexaCalcDigst) ){
						
						String outPut = String.format("%s %s %s (COLLISION)",fileName,typeDigest, hexaCalcDigst );	
						System.out.println(outPut);
						
					}
				}
				
			}
			
			
			if(knownDigests.containsKey(fileName)){/*There's some digest for this file */
				Map<String,String> currMap = knownDigests.get(fileName) ;
				
				/*For this file, verify if exists a digest for the current digest type*/
				if(currMap.containsKey(typeDigest)){ 	
					
					/* If the calculated digest is equals, we are OK */
					if(currMap.get(typeDigest).equals(hexaCalcDigst) ){
						String outPut = String.format("%s %s %s (OK)",fileName,typeDigest, hexaCalcDigst );	
						outPuts.add(outPut);
					}
					else{/*The digest are not equal... NOT OK */
						String outPut = String.format("%s %s %s (NOT OK)",fileName,typeDigest, hexaCalcDigst );	
						System.out.println(outPut);
					}					
				}
				else{/* There isn't a digest in file for the current digest type */
					String outPut = String.format("%s %s %s (NOT FOUND)",fileName,this.msgDigest.getAlgorithm(), this.calcDigest.get(fileName) );				
					mapReturn.put(fileName, typeDigest + " " +hexaCalcDigst );
					System.out.println(outPut);	
				}				
			}
			else{ /* There's no digest for the file */
				String outPut = String.format("%s %s %s (NOT FOUND)",fileName,this.msgDigest.getAlgorithm(), this.calcDigest.get(fileName) );				
				mapReturn.put(fileName, typeDigest + " " +hexaCalcDigst );
				System.out.println(outPut);
			}
		}
		
				
		return mapReturn;
		
	}
	
	
	
	
	
	private String convertToHex(byte[] byteArray) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			String hex = Integer.toHexString(0x0100 + (byteArray[i] & 0x00FF)).substring(1);
			buffer.append((hex.length() < 2 ? "0" : "") + hex);
		}
		return buffer.toString();
	}
	
	

}
