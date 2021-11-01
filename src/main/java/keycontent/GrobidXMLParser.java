package keycontent;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class GrobidXMLParser extends DefaultHandler {
    private String sectionNum = null;
    private String currentSection = "";
    private Map<String, String> titles = new HashMap<>();
    private Map<String, String> contents = new HashMap<>();
    private StringBuilder currentValue = new StringBuilder();
    private boolean inSection = false;
    private boolean inHeaderDiv = false;

    @Override
    public void startDocument() {
        System.out.println("Start Document");
    }

    @Override
    public void endDocument() {
        System.out.println("End Document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        /**
         * We want to know whether we are in a big <DIV>
         */

        // DIV contains our wanted titles and the content

        final String xmlnsVal;
        if (qName.equalsIgnoreCase("div") && ((xmlnsVal = attributes.getValue("xmlns")) != null)
                && xmlnsVal.equalsIgnoreCase("http://www.tei-c.org/ns/1.0")) {
            System.out.println("Start: Header DIV");
            inHeaderDiv = true;
        }

        // in HEAD, we have our titles
        if (qName.equalsIgnoreCase("head")) {

            // String titleNumber = attributes.getValue(0);
            // System.out.println("Title number: " + titleNumber);
            final String nNumber = attributes.getValue("n");
            inSection = (nNumber != null);
            if (inSection) {
                System.out.println("N = " + nNumber);
                sectionNum = nNumber;
                System.out.printf("Start Element : %s%n", qName);
                // System.out.println("URI: " + uri);
                // System.out.println("Localname: " + localName);
                // System.out.println("Attr: " + attributes.toString());
            }
        }

        if (!(inSection || inHeaderDiv)) {
            // reset the tag value
            currentValue.setLength(0);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (inSection && qName.equalsIgnoreCase("head")) {
            // System.out.println(currentValue);
            System.out.println("End: head");
            currentSection = currentValue.toString().strip();

            System.out.println(currentSection);
            titles.put(sectionNum, currentSection);
            currentValue.setLength(0);
        }

        if (inHeaderDiv && qName.equalsIgnoreCase("div")) {
            // finishing a DIV, so if that div encompassed a section's title, the
            // currentValue should contain its text!
            System.out.println("End: div");
            if (inSection) {
                contents.put(currentSection, currentValue.toString());
                System.out.println("CONTENT[" + currentSection + "]: "
                        + currentValue.toString().substring(0, Math.min(50, currentValue.toString().length())));
            }
            inHeaderDiv = false;
            currentValue.setLength(0);
        }

        // System.out.printf("End Element : %s%n", qName);
        // System.out.println("URI: " + uri);
        // System.out.println("Localname: " + localName);
        // System.out.println("Current value: " + currentValue.toString());

        // if (qName.equalsIgnoreCase("name")) {
        // System.out.printf("Name : %s%n", currentValue.toString());
        // }

        // if (qName.equalsIgnoreCase("role")) {
        // System.out.printf("Role : %s%n", currentValue.toString());
        // }

        // if (qName.equalsIgnoreCase("salary")) {
        // System.out.printf("Salary : %s%n", currentValue.toString());
        // }

        // if (qName.equalsIgnoreCase("bio")) {
        // System.out.printf("Bio : %s%n", currentValue.toString());
        // }

    }

    // http://www.saxproject.org/apidoc/org/xml/sax/ContentHandler.html#characters%28char%5B%5D,%20int,%20int%29
    // SAX parsers may return all contiguous character data in a single chunk,
    // or they may split it into several chunks
    @Override
    public void characters(char ch[], int start, int length) {
        // The characters() method can be called multiple times for a single text node.
        // Some values may missing if assign to a new string

        // avoid doing this
        // value = new String(ch, start, length);

        // better append it, works for single or multiple calls
        currentValue.append(ch, start, length);
    }

}
