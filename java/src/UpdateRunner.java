import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Basic class to run updates on website
 * TODO: make an actual UI/GUI interface for this stuff
 * @author AnnaliseMariottini
 *
 */
public class UpdateRunner {
	
	public static final String[] ART_PAGES = { // all pages that have art updates
		"../index_content.html"
		,"../art_content.html"
	};
	public static final String ROOT_FOLDER = "..";
	public static final String IMAGE_FOLDER = "../images";

	public static void main(String[] args) {
		
		System.out.println("Choose an update # from the list:");
		System.out.println("0. Find/Replace All update");
		System.out.println("1. Art images update");
		System.out.println("2. Copyright update");
		System.out.println("3. Template update");
		
		Scanner in = new Scanner(System.in);
		
		try {
			int choice = in.nextInt();
			
			switch(choice) {
			case 0: findreplace(); break;
			case 1: artUpdate(); templateUpdate(); break;
			case 2: copyrightUpdate(); templateUpdate(); break;
			case 3: templateUpdate(); break;
			}
		} catch (InputMismatchException e) {
			System.out.println("That input is not valid.");
		} finally {
			in.close();
		}
	}
	
	public static void findreplace() {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter the String to find:");
		String find = in.nextLine();
		System.out.println("Enter the String to replace:");
		String replace = in.nextLine();

		try{
			if(!ChangeAThing.change(ROOT_FOLDER, find, replace)) {
				System.out.println("The String to find was not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}
	
	public static void artUpdate() {
		Scanner in = new Scanner(System.in);
		System.out.println("Possible pages to update:");
		for(int i = 0; i < ART_PAGES.length; i++) {
			System.out.println(i+". "+ART_PAGES[i]);
		}
		System.out.println("How many would you like to update?");
		
		try {
			int[] pages = new int[in.nextInt()];
			
			if(pages.length >= ART_PAGES.length) {
				for(int i = 0; i < pages.length; i++) {
					pages[i] = i;
				}
			} else {
				for(int i = 0; i < pages.length; i++) {
					System.out.println("Enter a page #");
					pages[i] = in.nextInt();
				}
			}
			
			ArtUpdate a = new ArtUpdate(IMAGE_FOLDER);
			a.compileArtFiles();
			
			for(int i = 0; i < pages.length; i++) {
				a.rebuildPage(ART_PAGES[pages[i]]);
			}
		} catch (InputMismatchException e) {
			System.out.println("That input is not valid.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}
	}
	
	public static void copyrightUpdate() {
		try{
			CopyrightUpdate.update(ROOT_FOLDER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void templateUpdate() {
		try{
			TemplateUpdate.update(ROOT_FOLDER);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
