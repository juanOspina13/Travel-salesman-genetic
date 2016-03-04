/*
 * Germán Pérez         0844987
 * Juan Manuel Ospina   0840730
 */

package vendedorviajero;

import java.awt.Point;

public class Ciudad
{
    Point coordenadas;
    String nombre;

    public Ciudad(Point coordenadas, String nombre)
    {
        this.coordenadas = coordenadas;
        this.nombre = nombre;
    }    
    
    public Point getCoordenadas()
    {
        return coordenadas;
    }    
       
    public String getNombre()
    {
        return nombre;
    }   
}