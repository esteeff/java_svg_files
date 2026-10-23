package mx.unison.svg.util;

import java.awt.Graphics2D;

public interface SVGElement {
    
    /**
     * Dibuja el elemento gráfico en el contexto gráfico proporcionado.
     * @param g2d El objeto Graphics2D utilizado para pintar en la pantalla.
     */
    void draw(Graphics2D g2d);
}