import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to update different aspects of website pertaining to art images
 * All art images must be stored in directories organized by type (genre),
 * 		contained in one parent directory
 * File names as follows: Art_Image_Title_YYYYMMDD
 * 		add _(unfinished) instead of dateID if not finished
 * 		must be .jpg or .png files
 * @author Annalise Mariottini
 */
public class ArtUpdate {
	private static final String HTML_ART_START = "$artContentStart";
	private static final String HTML_ART_END = "$artContentEnd";
	private static final String HOME_PAGE = "index";
	private static final String ART_PAGE = "art";
	private static final String THUMB_HEIGHT = "400";
	private static final String THUMB_WIDTH = "400";
	
	private File parentArtFolder; // the directory holding all art image subdirectories
	private List<ArtFileInfo> artList; // List to contain all art objects
	private List<String> artTypes; // all currently found art types (determined by directory names)
	
	public ArtUpdate(String path) {
		parentArtFolder = new File(path);
		artList = new ArrayList<>();
		artTypes = new ArrayList<>();
	}

	/**
	 * Collects and stores all image files in ArtFileInfo objects
	 * Compiles all ArtFileInfo objects into a List
	 * Also creates a list of all current art types (e.g. Painting, etc.)
	 */
	public void compileArtFiles() {
		File[] folderContents = parentArtFolder.listFiles();
		List<File> artTypeFolders = new ArrayList<>(); //list of art directories
		
		for(File folderItem: folderContents) { // adding correct directories
			if(folderItem.isDirectory() && !folderItem.getName().equals("thumbs")) {
				artTypeFolders.add(folderItem);
			}
		}

		for(File currentFolder: artTypeFolders) { //goes through all art types
			artTypes.add(currentFolder.getName());
			File[] imageFiles = currentFolder.listFiles();
			for(File currentFile: imageFiles) { //goes through all image files
				String name = currentFile.getName();
				if((name.substring(name.lastIndexOf('.')).toLowerCase().equals(".jpg") ||
				name.substring(name.lastIndexOf('.')).toLowerCase().equals(".png")) &&
				!name.toLowerCase().contains("unfinished")) {
					String dateID = name.substring(name.lastIndexOf('_') + 1, name.lastIndexOf('.'));
					name = name.substring(0, name.lastIndexOf('_'));
					name = name.replace('_', ' ');
					String type = currentFolder.getName();
					ArtFileInfo item = new ArtFileInfo(type,name,dateID,currentFile);
					artList.add(item);
				}
			}
		}
		
		Collections.sort(artList);
	}
	
	/**
	 * Inserts tags for all completed art images into the art page
	 * HTML must have the comments "art starts here" and "art ends here" for proper placement
	 * @param pagePath - the relative file path to the art page
	 * 		(usually "./art.html")
	 */
	public void rebuildPage(String pagePath) throws IOException {
		File pageFile = new File(pagePath);
		FileReader pageFR = new FileReader(pageFile);
		BufferedReader pageBR = new BufferedReader(pageFR);
		StringBuffer pageText = new StringBuffer();
		String line;
		
		while((line = pageBR.readLine()) != null) {
			pageText.append(line+System.lineSeparator());
			if(line.contains(HTML_ART_START)) {
				if(pagePath.contains(HOME_PAGE)) {
					pageText.append(generateJsImageArrays());
				} else if (pagePath.contains(ART_PAGE)) {
					pageText.append(generateArtTags());
				}
				
				while(!line.contains(HTML_ART_END)) {
					line = pageBR.readLine();
				}
				pageText.append(line+System.lineSeparator());
			}
		}
		pageFR.close();
		pageBR.close();
		
		FileWriter pageFW = new FileWriter(pageFile);
		BufferedWriter pageBW = new BufferedWriter(pageFW);
		pageBW.write(pageText.toString());
		pageBW.flush();
		pageBW.close();
	}
	
	/**
	 * Creates arrays for use in js script to randomize images on homepage
	 * @return inputText - the arrays containing all image info
	 */
	private StringBuffer generateJsImageArrays() {
		StringBuffer inputText = new StringBuffer();

		inputText.append("var thumbSRC = [");
		for(int i = 0; i < artList.size(); i++) {
			if(i != 0) {
				inputText.append(", ");
			}
			inputText.append("\"");
			ArtFileInfo currentArt = artList.get(i);
			String path = currentArt.file.getPath();
			path = path.replace(currentArt.type, "thumbs");
			path = path.replace("\\", "/");
			path = path.substring(path.indexOf("images"));
			inputText.append(path);
			inputText.append("\"");
		}
		inputText.append("];"+System.lineSeparator());
		
		inputText.append("var imageHREF = [");
		for(int i = 0; i < artList.size(); i++) {
			if(i != 0) {
				inputText.append(", ");
			}
			inputText.append("\"");
			ArtFileInfo currentArt = artList.get(i);
			String path = currentArt.file.getPath();
			path = path.substring(path.indexOf("images"));
			path = path.replace("\\", "/");
			inputText.append(path);
			inputText.append("\"");
		}
		inputText.append("];"+System.lineSeparator());
		
		inputText.append("var imageALT = [");
		for(int i = 0; i < artList.size(); i++) {
			if(i != 0) {
				inputText.append(", ");
			}
			inputText.append("\"");
			ArtFileInfo currentArt = artList.get(i);
			String altText = currentArt.name;
			inputText.append(altText);
			inputText.append("\"");
		}
		inputText.append("];"+System.lineSeparator());
		
		inputText.append("var id = [");
		for(int i = 0; i < artList.size(); i++) {
			if(i != 0) {
				inputText.append(", ");
			}
			ArtFileInfo currentArt = artList.get(i);
			int id = currentArt.dateID;
			inputText.append(id);
		}
		inputText.append("];"+System.lineSeparator());
		
		return inputText;
	}
	
	/*
	 * Creates <div>, <a>, and <img> HTML tags for all art images
	 * 		and <h2> tages for art type titles
	 * Image thumbs must be 400px x 400px
	 * Use class "art_box" to style <div> tags
	 * @return inputText - a StringBuffer containing all image tags
	 */
	private StringBuffer generateArtTags() {
		StringBuffer inputText = new StringBuffer();
		
		String lastType = "";
		String currentType;
		
		for(ArtFileInfo currentArt: artList) {
			currentType = currentArt.type;
			if(!currentType.equals(lastType)) {
				if(!lastType.equals("")) {
					inputText.append(System.lineSeparator()+"</div>");
				}
				inputText.append(System.lineSeparator()+"<div>"+System.lineSeparator());
				inputText.append("<a name=\"");
				inputText.append(currentType);
				inputText.append("\"></a><h2>");
				inputText.append(currentArt.type);
				inputText.append("</h2>"+System.lineSeparator());
			}
			String path = currentArt.file.getPath();
			path = path.substring(path.indexOf("images"));
			path = path.replace("\\", "/");
			String thumbPath = path.replace(currentType, "thumbs");
			inputText.append("<a class=\"art_box\" href=\"");
			inputText.append(path);
			inputText.append("\" onmouseover=\"toggleTitle(this)\" ");
			inputText.append("onmouseout=\"toggleTitle(this)\" >");
			inputText.append("<img src=\"");
			inputText.append(thumbPath);
			inputText.append("\" alt=\"");
			inputText.append(currentArt.type+" - "+currentArt.name);
			inputText.append("\" height=\"");
			inputText.append(THUMB_HEIGHT);
			inputText.append("\" width=\"");
			inputText.append(THUMB_WIDTH);
			inputText.append("\" />");
			inputText.append("<span class=\"hidden\">");
			inputText.append(currentArt.name);
			inputText.append("</span>");
			inputText.append("</a>"+System.lineSeparator());
			lastType = currentType;
		}
		inputText.append(System.lineSeparator()+"</div>"+System.lineSeparator());
		
		return inputText;
	}
	
	/**
	 * Stores basic info about each art file
	 * @author Annalise Mariottini
	 */
	public class ArtFileInfo implements Comparable<ArtFileInfo> {
		
		private String type;
		private int typeOrder;
		private String name;
		private int dateID; // format: YYYYMMDD
		private File file;
		
		private ArtFileInfo(String t, String n, String d, File f) throws NumberFormatException {
			type = t;
			switch(type) {
				case "Painting": typeOrder = 0; break;
				case "Sculpture": typeOrder = 1; break;
				case "Drawing": typeOrder = 2; break;
				case "Design": typeOrder = 3; break;
			}
			name = n;
			dateID = Integer.parseInt(d);
			file = f;
		}
		
		public int compareTo(ArtFileInfo other) { // type order, then reverse date order
			// sort by type first
			if (this.typeOrder < other.typeOrder) {
				return -1;
			}
			if (this.typeOrder > other.typeOrder) {
				return 1;
			}
			if (this.dateID > other.dateID) {
				return -1;
			}
			if (this.dateID < other.dateID)
				return 1;
			
			return 0;
		}
	}
}
