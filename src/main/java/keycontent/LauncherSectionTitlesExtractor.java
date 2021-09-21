package keycontent;

import java.util.Arrays;

import org.grobid.core.data.BiblioItem;
import org.grobid.core.engines.Engine;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;

public class LauncherSectionTitlesExtractor {
	public static void main(String[] args) {
		final String pdfFolder = "./papers/";//"C:\\Users\\wf7467\\Desktop\\JabRef\\Papers\\2021";
		final String filename = "2008.05190.pdf";
		// "2101.09969.pdf";
		String pdfPath = pdfFolder + "\\" + filename;

		try {
			String pGrobidHome = "../grobid/grobid-home";//"C:\\Users\\wf7467\\Desktop\\GitHub\\KIT\\grobid\\grobid-home";

			// The GrobidHomeFinder can be instantiate without parameters to verify the
			// grobid home in the standard
			// location (classpath, ../grobid-home, ../../grobid-home)

			// If the location is customised:
			GrobidHomeFinder grobidHomeFinder = new GrobidHomeFinder(Arrays.asList(pGrobidHome));

			// The grobid yaml config file needs to be instantiate using the correct
			// grobidHomeFinder or it will use the default
			// locations
			GrobidProperties.getInstance(grobidHomeFinder);

			System.out.println(">>>>>>>> GROBID_HOME=" + GrobidProperties.getGrobidHome());

			Engine engine = GrobidFactory.getInstance().createEngine();

			// Biblio object for the result
			BiblioItem resHeader = new BiblioItem();
			String tei = engine.processHeader(pdfPath, 1, resHeader);
		} catch (Exception e) {
			// If an exception is generated, print a stack trace
			e.printStackTrace();
		}
	}
}
