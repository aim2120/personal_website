import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Change one thing across all HTML files
 * @author AnnaliseMariottini
 *
 */
public class ChangeAThing {
	
	/**
	 * Change a given String, found in all (or most) .HTML files, to another String
	 * @param mainFolderPath
	 * 		the folder containing all HTML files
	 * @param thing
	 * 		the String to change
	 * @param newThing
	 * 		the String to replace it
	 * @throws IOException
	 * 		if the given path doesn't exist
	 */
	public static boolean change(String mainFolderPath, String thing, String newThing) throws IOException {
		File parentHTMLFolder = new File(mainFolderPath);
		File[] folderContents = parentHTMLFolder.listFiles();
		List<File> htmlFiles = new ArrayList<>();
		List<File> subDirectories = new ArrayList<>();
		boolean changed = false;
		
		for(File item: folderContents) { // finding HTML files
			String name = item.getName();
			if(name.contains(".") && name.substring(name.lastIndexOf('.')).equals(".html")) {
				htmlFiles.add(item);
			} else if(item.isDirectory()) {
				subDirectories.add(item);
			}
		}
		
		for(File subDirectory: subDirectories) { // recursively get all HTML files in sub-directories
			change(subDirectory.getPath(),thing,newThing);
		}
		
		for(File page: htmlFiles) {
			FileReader pageFR = new FileReader(page);
			BufferedReader pageBR = new BufferedReader(pageFR);
			StringBuffer pageText = new StringBuffer();
			String line;
			
			while((line = pageBR.readLine()) != null)  {
				if(line.contains(thing)) {
					line = line.replace(thing, newThing);
					changed = true;
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
		return changed;
	}
}
