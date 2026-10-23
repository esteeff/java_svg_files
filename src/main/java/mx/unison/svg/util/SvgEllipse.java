package mx.unison.svg.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;

public class SvgEllipse implements SVGElement {
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

    public SvgEllipse(Properties shape) {
        this.shape = shape;
    }

    @Override
    public void draw(Graphics2D g2d) {
        String cxStr = shape.getProperty("cx");
        String cyStr = shape.getProperty("cy");
        String rxStr = shape.getProperty("rx");
        String ryStr = shape.getProperty("ry");

        if (cxStr == null || cyStr == null || rxStr == null || ryStr == null) return;

        try {
            int cx = (int) Double.parseDouble(cxStr);
            int cy = (int) Double.parseDouble(cyStr);
            int rx = (int) Double.parseDouble(rxStr);
            int ry = (int) Double.parseDouble(ryStr);

            if (rx <= 0 || ry <= 0) return; // Validar radios

            String fill = shape.getProperty("fill");
            if (fill != null && !fill.equals("none")) {
                g2d.setColor(parseColor(fill));
                g2d.fillOval(cx - rx, cy - ry, rx * 2, ry * 2);
            }

            String strokeWidthStr = shape.getProperty("stroke-width");
            if (strokeWidthStr != null) {
                g2d.setStroke(new BasicStroke(Float.parseFloat(strokeWidthStr)));
            }

            g2d.setColor(parseColor(shape.getProperty("stroke")));
            g2d.drawOval(cx - rx, cy - ry, rx * 2, ry * 2);

        } catch (Exception e) {
            System.out.println("Error al dibujar ellipse");
        }
    }
}