package mx.unison.svg.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;

public class SvgLine implements SVGElement {
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

    public SvgLine(Properties shape) {
        this.shape = shape;
    }

    @Override
    public void draw(Graphics2D g2d) {
        String x1Str = shape.getProperty("x1");
        String y1Str = shape.getProperty("y1");
        String x2Str = shape.getProperty("x2");
        String y2Str = shape.getProperty("y2");

        if (x1Str == null || y1Str == null || x2Str == null || y2Str == null) return;

        try {
            int x1 = (int) Double.parseDouble(x1Str);
            int y1 = (int) Double.parseDouble(y1Str);
            int x2 = (int) Double.parseDouble(x2Str);
            int y2 = (int) Double.parseDouble(y2Str);

            String strokeWidthStr = shape.getProperty("stroke-width");
            if (strokeWidthStr != null) {
                g2d.setStroke(new BasicStroke(Float.parseFloat(strokeWidthStr)));
            }

            g2d.setColor(parseColor(shape.getProperty("stroke")));
            g2d.drawLine(x1, y1, x2, y2);

        } catch (Exception e) {
            System.out.println("Error al dibujar line");
        }
    }
}
