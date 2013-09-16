import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		if(args.length < 3){
			System.out.println("Numero de parametros invalidos.");
			System.out.println("Parametros esperados : Tipo_Digest Caminho_ArqListaDigest Caminho_Arq1... Caminho_ArqN");
			System.exit(1);
		}
		
		List<String> paths = Arrays.asList( args );
		String digestType = paths.remove(0);
		String pathFileNDigest = paths.remove(0);
		
		FileManager filMng = new FileManager(paths);
		
		DigestCalculator digCalc = new DigestCalculator(digestType, filMng.getMapByteFile());
		
		digCalc.Calculate();
		
		Map<String,String> notFounds = digCalc.CheckDigest(FileManager.getDisgestMap(pathFileNDigest));
		
		
		
		

	}

}
