package mx.unison.svg.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;

/**
 * Clase que representa un rectángulo SVG.
 */
public class SvgRect implements SVGElement {
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

    /**
     * Constructor del rectángulo.
     * @param shape Propiedades extraídas del archivo XML.
     */
    public SvgRect(Properties shape) {
        this.shape = shape;
    }

    /**
     * Dibuja el rectángulo en el contexto gráfico.
     * @param g2d El objeto Graphics2D utilizado para pintar.
     */
    @Override
    public void draw(Graphics2D g2d) {
        String xStr = shape.getProperty("x");
        String yStr = shape.getProperty("y");
        String wStr = shape.getProperty("width");
        String hStr = shape.getProperty("height");

        if (xStr == null || yStr == null || wStr == null || hStr == null) return;

        try {
            int x = (int) Double.parseDouble(xStr);
            int y = (int) Double.parseDouble(yStr);
            int w = (int) Double.parseDouble(wStr);
            int h = (int) Double.parseDouble(hStr);

            if (w < 0 || h < 0) return; // Validar dimensiones

            String rxStr = shape.getProperty("rx");
            String ryStr = shape.getProperty("ry");
            int rx = (rxStr != null) ? (int) Double.parseDouble(rxStr) : 0;
            int ry = (ryStr != null) ? (int) Double.parseDouble(ryStr) : 0;

            String fill = shape.getProperty("fill");
            if (fill != null && !fill.equals("none")) {
                g2d.setColor(parseColor(fill));
                if (rx > 0 && ry > 0) {
                    g2d.fillRoundRect(x, y, w, h, rx * 2, ry * 2);
                } else {
                    g2d.fillRect(x, y, w, h);
                }
            }

            String strokeWidthStr = shape.getProperty("stroke-width");
            if (strokeWidthStr != null) {
                g2d.setStroke(new BasicStroke(Float.parseFloat(strokeWidthStr)));
            }

            g2d.setColor(parseColor(shape.getProperty("stroke")));
            if (rx > 0 && ry > 0) {
                g2d.drawRoundRect(x, y, w, h, rx * 2, ry * 2);
            } else {
                g2d.drawRect(x, y, w, h);
            }

        } catch (Exception e) {
            System.out.println("Error al dibujar rect");
        }
    }
}