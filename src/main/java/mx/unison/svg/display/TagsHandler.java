package mx.unison.svg.display;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagsHandler  extends DefaultHandler {
    public static final Logger LOG = Logger.getLogger( TagsHandler.class.getName() );

    private ArrayList<Properties> shapes;
    private Properties currentShape = null;
    private StringBuilder textoActual = null;

    public TagsHandler() {
        super();
        shapes = new ArrayList<>();
    }

    public ArrayList<Properties> getShapes() {
        return shapes;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        super.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
            super.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        super.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String tagName = null;

        if(localName == null || localName.isEmpty()) {
            tagName = qName.toLowerCase();
        } else {
            tagName = localName.toLowerCase();
        }

        if  (tagName.equals("line") ) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "line");

            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
        }

        if  (tagName.equals("circle") ) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "circle");

            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
        }

        if (tagName.equals("polyline")) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "polyline");
            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
        }

                if (tagName.equals("rect")) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "rect");
            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
        }

                if (tagName.equals("ellipse")) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "ellipse");
            copyAttributes(atts, currentShape);
            shapes.add(currentShape);
        }

                if (tagName.equals("text")) {
            LOG.info("Starting element " + tagName);
            currentShape = new Properties();
            currentShape.put("type", "text");
            copyAttributes(atts, currentShape);
            textoActual = new StringBuilder(); // Preparamos para leer el texto
            shapes.add(currentShape);
        }
    }

    private void copyAttributes(Attributes atts, Properties myShape) {
        if(atts.getLength() != 0) {
            for(int i = 0; i < atts.getLength(); i++) {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i);
                myShape.put(name, value);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String tagName = (localName == null || localName.isEmpty()) ? qName.toLowerCase() : localName.toLowerCase();
        
        if (tagName.equals("text") && currentShape != null) {
            // Guardamos el texto que estaba entre las etiquetas
            currentShape.put("content", textoActual.toString().trim());
            textoActual = null;
        }
        currentShape = null;
    }

       @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // Si estamos dentro de un <text>, acumulamos las letras
        if (textoActual != null) {
            textoActual.append(ch, start, length);
        }
    }    

    @Override
    public void error(SAXParseException e) throws SAXException {
        LOG.log(Level.SEVERE, "OOPS" + e.getMessage());
    }
}
