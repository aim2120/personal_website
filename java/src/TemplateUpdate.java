import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Updates template on all HTML pages
 * @author annaliseirene
 */
public class TemplateUpdate {
	private static final String KEY_SYMBOL = "<!--$";
	private static final String KEY_SYMBOL_START = "<!--$START";
	private static final String KEY_SYMBOL_END = "<!--$END";
	private static final String TEMPLATE_PAGE = "template.html";
	
	
	/**
	 * Reads through content HTML files and creates corresponding build files
	 * Uses template.html as template
	 * @param path
	 * 		root folder
	 * @throws IOException 
	 * 		if given path is not valid, or if no template file exists
	 */
	public static void update(String path) throws IOException {
		File rootDirectory = new File(path);
		File[] folderContents = rootDirectory.listFiles();
		File templateFile = null;
		List<File> contentFiles = new ArrayList<>();
		
		for(File item: folderContents) { // finding HTML files
			String name = item.getName();
			if(name.contains(TEMPLATE_PAGE)) {
				templateFile = item;
			} else if(name.contains("content.html")) {
				contentFiles.add(item);
			}
		}
		
		if (templateFile == null) {
			throw new FileNotFoundException("No template file found.");
		}

		String[] templateLines = readTemplateFile(templateFile);
	
		for(File content: contentFiles) {
			FileReader contentFR = new FileReader(content);
			BufferedReader contentBR = new BufferedReader(contentFR);
			StringBuilder pageText = new StringBuilder();
			HashMap<String, String> contentMap = new HashMap<>();
			String line, keyLine = "";

			while ((line = contentBR.readLine()) != null) {
				if(keyLine.contains(KEY_SYMBOL_START)) {
					StringBuilder lines = new StringBuilder();
					do {
						if (line.contains(KEY_SYMBOL_END)) {
							break;
						}
						line += System.lineSeparator();
						lines.append(line);

					} while((line = contentBR.readLine()) != null);
					contentMap.put(keyLine.trim(), lines.toString());
				} else if(keyLine.contains(KEY_SYMBOL)) {
					line += System.lineSeparator();
					contentMap.put(keyLine.trim(), line);
				}
				keyLine = line;
			}

			for (int i = 0; i < templateLines.length; i++) {
				line = templateLines[i];
				if(line.contains(KEY_SYMBOL_START)) { // multiple line
					pageText.append(contentMap.get(line.trim()));
					do {
						line = templateLines[++i];
					} while(!line.contains(KEY_SYMBOL_END));
				} else if(line.contains(KEY_SYMBOL)) { // single line
					pageText.append(contentMap.get(line.trim()));
					i++;
				} else {
					line += System.lineSeparator();
					pageText.append(line);
				}
			}

			contentFR.close();
			contentBR.close();
			
			String pageName = content.getName().replace("_content", "");
			File newPage = new File(rootDirectory, pageName);
			if (!newPage.exists()) {
				newPage.createNewFile();
			}
			FileWriter pageFW = new FileWriter(newPage);
			BufferedWriter pageBW = new BufferedWriter(pageFW);
			pageBW.write(pageText.toString());
			pageBW.flush();
			pageBW.close();
		}
	}
	
	/**
	 * Reads lines of template file
	 * @param templateFile
	 * @return
	 * 		array of lines from template (include \n)
	 * @throws IOException 
	 * 		if file not found
	 */
	private static String[] readTemplateFile(File templateFile) throws IOException {
		FileReader tempFR= new FileReader(templateFile);
		BufferedReader tempBR = new BufferedReader(tempFR);
		StringBuilder tempBuilder = new StringBuilder();
		String line;
		
		while((line = tempBR.readLine()) != null)  {
			line += System.lineSeparator();
			tempBuilder.append(line);
		}
	
		tempFR.close();
		tempBR.close();
		return tempBuilder.toString().split("\n");
	}
}

