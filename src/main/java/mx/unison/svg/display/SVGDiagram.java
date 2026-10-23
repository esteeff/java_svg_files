package mx.unison.svg.display;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import mx.unison.svg.util.SvgPolyline;
import mx.unison.svg.util.SvgRect;

import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import mx.unison.svg.util.SVGElement;
import mx.unison.svg.util.SvgLine;
import mx.unison.svg.util.SvgPolyline;
import mx.unison.svg.util.SvgRect;
import mx.unison.svg.util.SvgEllipse;
import mx.unison.svg.util.SvgText;

public class SVGDiagram extends JComponent {

    public static final Logger LOG = Logger.getLogger(SVGDiagram.class.getName());

    private SAXParser parser = null;

    private int svgW; // ancho del dibujo
    private int svgH;  // alto del dibujo

    private Properties webColors;

    // parametros iniciales par dibujar las figuras
    private Color strokeColor;
    private Color fillColor;
    private BasicStroke currentStroke;

    // propiedades de las figuras leidas del archivo SVG
    private ArrayList<Properties> shapes = null;
    private Properties currentShape = null;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(svgW, svgH);
    }


    public SVGDiagram(File file) {
        super();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            LOG.severe("Error creating SAX parser: " + e.getMessage());
        }
        try {
            // Parsear el archivo SVG usando un manejador de eventos anónimo
            // Este manejador solo captura el elemento <svg> para obtener sus atributos de ancho y alto
            LOG.info("Parsing SVG file: " + file.getAbsolutePath());
            parser.parse(file, new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (localName.equals("svg")) {
                        String width = attributes.getValue("width");
                        String height = attributes.getValue("height");
                        svgW = Integer.parseInt(width);
                        svgH = Integer.parseInt(height);
                        LOG.info("SVG Document\n\tWidth: " + width + " , Height: " + height);
                    }
                }
            });
        } catch (Exception e) {
            LOG.severe("Error getting SVG dimensions: " + e.getMessage());
        }

        shapes = new ArrayList<>();
        strokeColor = Color.BLACK;
        fillColor = Color.LIGHT_GRAY;
        currentStroke = new BasicStroke(1.0f);

        TagsHandler tagsHandler = new TagsHandler();

        try {
            parser.reset();
            parser.parse(file, tagsHandler);
        } catch (Exception e) {
            LOG.severe("Error in SVG file: " + e.getMessage());
            e.printStackTrace();
        }
        shapes = tagsHandler.getShapes();
        // Cargar los colores web desde el archivo colors.properties
        loadColors();
    }

          @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        LOG.info("Painting SVG Diagram");
        Graphics2D g2d = (Graphics2D) g;

        for (Properties shape : shapes) {
            String type = shape.getProperty("type");
            if (type == null) continue;

            // Valores por defecto para cada figura
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.BLACK);

            SVGElement element = null;

            if (type.equals("line")) {
                element = new SvgLine(shape);
            } else if (type.equals("polyline")) {
                element = new SvgPolyline(shape);
            } else if (type.equals("rect")) {
                element = new SvgRect(shape);
            } else if (type.equals("ellipse")) {
                element = new SvgEllipse(shape);
            } else if (type.equals("text")) {
                element = new SvgText(shape);
            } else if (type.equals("circle")) {
                // El círculo lo dejamos pendiente o lo agregamos si el profe lo pide después
            }

            if (element != null) {
                element.draw(g2d);
            }
        }
    }

    // https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Values/named-color
    private void loadColors() {
        try {
            String userDir = System.getProperty("user.dir");
            FileReader reader = new FileReader(userDir + "/colors.properties");
            webColors = new Properties();
            webColors.load(reader);
            //System.out.println(webColors.size() + " " + webColors.getProperty("maroon"));
        } catch (IOException ex) {
            LOG.severe(ex.getMessage());
        }
    }

    // Metodo para parsear un color desde un string, considerando el formato hexadecimal, rgb o nombre de color
    private Color parseColor(String colorString, float opacity) {
        String colorCode = colorString.toLowerCase();
        Color newColor = null;

        //System.out.println("param " + colorCode);
        if (colorCode.startsWith("#")) {
            // Codigo de color "#00ffff"
            colorCode = colorCode.substring(1);
        } else if (colorCode.startsWith("0x")) {
            colorCode = colorCode.substring(2);
        } else if (colorCode.startsWith("rgb")) {
            // Codigo de color "rgb(00,255,0)"
            if (colorCode.startsWith("(", 3)) {
                return Color.BLACK;
            } else if (colorCode.startsWith("a(", 3)) {
                return Color.BLACK;
            }
        } else {
            // nombre de color e.g. "red
            //Color namedColor = Color.getColor(colorString);
            colorCode = webColors.getProperty(colorCode).substring(1).trim();
        }
        try {
            int r;
            int g;
            int b;
            int a;
            int len = colorCode.length();

            if (len == 6) {
                //System.out.println(colorCode);
                r = Integer.parseInt(colorCode.substring(0, 2), 16);
                g = Integer.parseInt(colorCode.substring(2, 4), 16);
                b = Integer.parseInt(colorCode.substring(4, 6), 16);
                //System.out.println(r + "," + g + "," + b);
              // newColor = new Color(r, g, b, opacity);
                newColor = new Color(r, g, b);
            }

        } catch (NumberFormatException nfe) {
           LOG.warning("Invalid color format: " + nfe.getMessage() );
           newColor = Color.BLACK;
        }
        return newColor;
    }



    private Color parseColor(String colorStr) {
        Color color = null;
        switch (colorStr) {
            case "black":
                color = Color.BLACK;
                break;
            case "white":
                color = Color.WHITE;
                break;
            case "red":
                color = Color.RED;
                break;
            case "green":
                color = Color.GREEN;
                break;
            case "blue":
                color = Color.BLUE;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
            case "cyan":
                color = Color.CYAN;
                break;
            case "magenta":
                color = Color.MAGENTA;
                break;
            case "gray":
                color = Color.GRAY;
                break;
            default:
                LOG.warning("Unknown color: " + colorStr);
        }
        return color;
    }

}
