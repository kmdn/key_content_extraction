package keycontent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.grobid.core.document.Document;
import org.grobid.core.engines.Engine;
import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.main.GrobidHomeFinder;
import org.grobid.core.utilities.GrobidProperties;
import org.xml.sax.SAXException;

public class LauncherSectionTitlesExtractor {
	public static void main(String[] args) {
		final String pdfFolder = "./papers";// "C:\\Users\\wf7467\\Desktop\\JabRef\\Papers\\2021";
		final String filename = "2008.05190.pdf";
		// "2101.09969.pdf";
		String pdfPath = pdfFolder + "/" + filename;
		final String outPath = pdfPath.replace(".pdf", ".tei");

		final File teiFile = new File(outPath);

		final boolean CREATE_TEI = false;

		try {
			if (CREATE_TEI) {

				String pGrobidHome = "../grobid/grobid-home";// "C:\\Users\\wf7467\\Desktop\\GitHub\\KIT\\grobid\\grobid-home";

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

				// Java example: https://grobid.readthedocs.io/en/latest/Grobid-java-library/
				Engine engine = GrobidFactory.getInstance().createEngine();

				final long startTime = System.currentTimeMillis();
				System.out.println("Processing...");
				// String tei = engine.processHeader(pdfPath, 1, resHeader);
				final Document teiDoc = engine.fullTextToTEIDoc(new File(pdfPath),
						GrobidAnalysisConfig.defaultInstance());
				final long endTime = System.currentTimeMillis();
				System.out.println("Process duration: " + (endTime - startTime));

				// System.out.println(teiDoc.getBlockDocumentHeaders());
				System.out.println(teiDoc.getTei());
				Files.write(Path.of(teiFile.getPath()), teiDoc.getTei().getBytes(), StandardOpenOption.CREATE,
						StandardOpenOption.WRITE);
			}

			// Process TEI file

			SAXParserFactory factory = SAXParserFactory.newInstance();
			try {

				SAXParser saxParser = factory.newSAXParser();

				final GrobidXMLParser handler = new GrobidXMLParser();
				saxParser.parse(teiFile, handler);

			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			// System.out.println("TEI: " + tei);
		} catch (Exception e) {
			// If an exception is generated, print a stack trace
			e.printStackTrace();
		}
	}
}
