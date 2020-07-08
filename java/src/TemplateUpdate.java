import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author annaliseirene
 */
public class TemplateUpdate {
	private static final String PAGE_CONTENT_START = "$pageContentStart";
	private static final String PAGE_CONTENT_END = "$pageContentEnd";
	private static final String TEMPLATE_PAGE = "template";
	
	private static StringBuilder templateStart = new StringBuilder();
	private static StringBuilder templateEnd = new StringBuilder();
	
	public static void update(String path, File template) throws IOException {
		File rootDirectory = new File(path);
		File templateFile = template;
		File[] folderContents = rootDirectory.listFiles();
		List<File> htmlFiles = new ArrayList<>();
		List<File> subDirectories = new ArrayList<>();
		
		for(File item: folderContents) { // finding HTML files
			String name = item.getName();
			if(name.contains(TEMPLATE_PAGE)) {
				templateFile = item;
			} else if(name.contains(".") && 
				name.substring(name.lastIndexOf('.')).equals(".html")) {
				htmlFiles.add(item);
			} else if(item.isDirectory()) {
				subDirectories.add(item);
			}
		}
		
		if (templateFile == null) {
			throw new FileNotFoundException("No template file found.");
		} else {
			buildTemplateStrings(templateFile);
		}
		
		// recursively goes down each folder to find all HTML files
		for(File subDirectory: subDirectories) { 
			update(subDirectory.getPath(), templateFile);
		}
		
		for(File page: htmlFiles) {
			FileReader pageFR = new FileReader(page);
			BufferedReader pageBR = new BufferedReader(pageFR);
			StringBuilder pageText = new StringBuilder();
			String line;
			
			pageText.append(templateStart.toString());
			while((line = pageBR.readLine()) != null)  { // looking for content start
				if(line.contains(PAGE_CONTENT_START)) {
					line += System.lineSeparator();
					pageText.append(line);
					break;
				}
			}
			while((line = pageBR.readLine()) != null) { // reading content until end
				line += System.lineSeparator();
				pageText.append(line);
				if(line.contains(PAGE_CONTENT_END)) {
					break;
				}
			}
			pageText.append(templateEnd.toString());
			
			pageFR.close();
			pageBR.close();
			
			FileWriter pageFW = new FileWriter(page);
			BufferedWriter pageBW = new BufferedWriter(pageFW);
			pageBW.write(pageText.toString());
			pageBW.flush();
			pageBW.close();
		}
		
	}
	
	private static void buildTemplateStrings(File templateFile) throws IOException {
		FileReader tempFR= new FileReader(templateFile);
		BufferedReader tempBR = new BufferedReader(tempFR);
		String line;
		
		//reading template start
		while((line = tempBR.readLine()) != null)  {
			if(line.contains(PAGE_CONTENT_START)) {
				break;
			}
			line += System.lineSeparator();
			templateStart.append(line);
		}

		//skipping filler lines
		while((line = tempBR.readLine()) != null) {
			if(line.contains(PAGE_CONTENT_END)) {
				break;
			}
		}

		//reading template end
		while((line = tempBR.readLine()) != null) {
			line += System.lineSeparator();
			templateStart.append(line);
		}
		
		tempFR.close();
		tempBR.close();
	}
}
