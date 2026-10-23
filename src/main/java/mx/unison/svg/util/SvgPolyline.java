package mx.unison.svg.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;

public class SvgPolyline implements SVGElement {
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

    public SvgPolyline(Properties shape) {
        this.shape = shape;
    }

    @Override
    public void draw(Graphics2D g2d) {
        String pointsStr = shape.getProperty("points");
        if (pointsStr == null) return;

        String[] pares = pointsStr.trim().split("\\s+");
        int[] xPoints = new int[pares.length];
        int[] yPoints = new int[pares.length];

        try {
            for (int i = 0; i < pares.length; i++) {
                String[] coords = pares[i].split(",");
                xPoints[i] = (int) Double.parseDouble(coords[0]);
                yPoints[i] = (int) Double.parseDouble(coords[1]);
            }

            String fill = shape.getProperty("fill");
            if (fill != null && !fill.equals("none")) {
                g2d.setColor(parseColor(fill));
                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
            }

            String strokeWidthStr = shape.getProperty("stroke-width");
            if (strokeWidthStr != null) {
                g2d.setStroke(new BasicStroke(Float.parseFloat(strokeWidthStr)));
            }

            g2d.setColor(parseColor(shape.getProperty("stroke")));
            g2d.drawPolyline(xPoints, yPoints, xPoints.length);

        } catch (Exception e) {
            System.out.println("Error al dibujar polyline");
        }
    }
}