import java.io.IOException;

/**
 * Basic class to run updates on website
 * TODO: make an actual UI/GUI interface for this stuff
 * @author AnnaliseMariottini
 *
 */
public class UpdateRunner {
	
	public static final String[] ART_PAGES = { // all pages that need art updates
			"./index.html"
			,"./art.html"
			};
	public static final String ROOT_FOLDER = ".";
	public static final String IMAGE_FOLDER = "./images";

	public static void main(String[] args) {
		
		// Updating art stuff
		String[] pages = ART_PAGES;
		if (args.length > 0) { // if args present, use args instead of given ART_PAGES
			pages = args;
		}
		
		ArtUpdate a = new ArtUpdate(IMAGE_FOLDER,"art starts here","art ends here");
		a.compileArtFiles();
		
		for(int i = 0; i < pages.length; i++) {
			try {
				a.rebuildPage(pages[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Updating copyright
		try{
			CopyrightUpdate.update(ROOT_FOLDER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Change a thing!
		try{
			ChangeAThing.change(".", "", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
