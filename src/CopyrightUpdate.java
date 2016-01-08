import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Updates copyright year to current year when run
 * @author AnnaliseMariottini
 */
public class CopyrightUpdate {
	
	/**
	 * Searches through the root and all sub-directories of the given root for .HTML files
	 * Updates the year of the line containing "&copy;"
	 * @param path
	 * 		the folder containing all HTML files
	 * @throws IOExceptions
	 * 		if the given path is not valid
	 */
	public static void update(String path) throws IOException {
		File rootDirectory = new File(path);
		File[] folderContents = rootDirectory.listFiles();
		List<File> htmlFiles = new ArrayList<>();
		List<File> subDirectories = new ArrayList<>();
		
		for(File item: folderContents) { // finding HTML files
			String name = item.getName();
			if(name.contains(".") && name.substring(name.lastIndexOf('.')).equals(".html")) {
				htmlFiles.add(item);
			} else if(item.isDirectory()) {
				subDirectories.add(item);
			}
		}
		
		for(File subDirectory: subDirectories) { // recursively goes down each folder to find all HTML files
			update(subDirectory.getPath());
		}
		
		for(File page: htmlFiles) {
			FileReader pageFR = new FileReader(page);
			BufferedReader pageBR = new BufferedReader(pageFR);
			StringBuffer pageText = new StringBuffer();
			String line;
			
			while((line = pageBR.readLine()) != null)  {
				if(line.contains("&copy;")) {
					int i = line.indexOf("20");
					String lastYear = line.substring(i, i+4);
					String currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
					line = line.replace(lastYear, currentYear);
				}
				pageText.append(line+System.lineSeparator());
			}
			
			pageFR.close();
			pageBR.close();
			
			FileWriter pageFW = new FileWriter(page);
			BufferedWriter pageBW = new BufferedWriter(pageFW);
			pageBW.write(pageText.toString());
			pageBW.flush();
			pageBW.close();
		}
		
	}

}
