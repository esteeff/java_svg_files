package mx.unison.svg.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Properties;

public class SvgText implements SVGElement {
    private Properties shape;
    
        private Color parseColor(String colorStr) {
        if (colorStr == null || colorStr.equalsIgnoreCase("none")) return Color.BLACK;
        try {
            // Manejo de colores básicos en inglés
            switch (colorStr.toLowerCase()) {
                case "black": return Color.BLACK;
                case "white": return Color.WHITE;
                case "red": return Color.RED;
                case "green": return Color.GREEN;
                case "blue": return Color.BLUE;
                case "yellow": return Color.YELLOW;
                case "cyan": return Color.CYAN;
                case "magenta": return Color.MAGENTA;
                case "gray": return Color.GRAY;
                case "lightgray": return Color.LIGHT_GRAY;
                case "lightblue": return new Color(173, 216, 230);
                case "lightgreen": return new Color(144, 238, 144);
                case "pink": return Color.PINK;
                case "orange": return Color.ORANGE;
                default: 
                    // Por si es un código HEX (#FF0000)
                    return Color.decode(colorStr);
            }
        } catch (Exception e) { 
            return Color.BLACK; 
        }
    }

    public SvgText(Properties shape) {
        this.shape = shape;
    }

    @Override
    public void draw(Graphics2D g2d) {
        String xStr = shape.getProperty("x");
        String yStr = shape.getProperty("y");
        String contenido = shape.getProperty("content");

        if (xStr == null || yStr == null || contenido == null) return;

        try {
            int x = (int) Double.parseDouble(xStr);
            int y = (int) Double.parseDouble(yStr);

            String fontName = shape.getProperty("font-family");
            if (fontName == null) fontName = "Arial";
            
            int fontSize = 12;
            String fontSizeStr = shape.getProperty("font-size");
            if (fontSizeStr != null) fontSize = (int) Double.parseDouble(fontSizeStr);

            g2d.setFont(new Font(fontName, Font.PLAIN, fontSize));
            g2d.setColor(parseColor(shape.getProperty("fill")));
            
            g2d.drawString(contenido, x, y);

        } catch (Exception e) {
            System.out.println("Error al dibujar text");
        }
    }
}