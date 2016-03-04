/*
 * Germán Pérez         0844987
 * Juan Manuel Ospina   0840730
 */
package gui;

import vendedorviajero.AlgoritmoGenetico;
import vendedorviajero.Camino;
import vendedorviajero.Ciudad;

import java.awt.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Animacion extends JComponent implements Runnable {

    int numeroGeneracion;
    Camino caminoQueRecorre = null;
    AlgoritmoGenetico algoritmoGenetico;
    LinkedList<Ciudad> ciudadesAPintar;
    LinkedList<Ciudad> ciudadesRespuesta;
    int x = 0;
    Thread hilo = null;

    public Animacion(AlgoritmoGenetico algoritmoGenetico, LinkedList<Ciudad> ciudadesRespesta, int numeroGeneracionRespuesta) {
        ciudadesRespuesta = ciudadesRespesta;
        numeroGeneracion = numeroGeneracionRespuesta;
        this.algoritmoGenetico = algoritmoGenetico;
    }

    public void paint(Graphics g) {
        ciudadesAPintar = algoritmoGenetico.getCiudades();
        Font system = new Font("System", Font.BOLD, 16);
        g.setFont(system);
        g.setColor(Color.BLUE);
        g.drawString("NUMERO DE GENERACION:" + numeroGeneracion, 100, 600);
        g.drawString("DISTANCIA :" + algoritmoGenetico.getCromosomaSolucion().getDistanciaEuclidesCamino(), 450, 600);

        for (int i = 0; i < ciudadesAPintar.size(); i++) {
            Font courier = new Font("Courier", Font.PLAIN, 12);
            g.setFont(courier);
            g.setColor(Color.BLACK);
            g.drawString(ciudadesAPintar.get(i).getNombre() + " (" + (int) ciudadesAPintar.get(i).getCoordenadas().getX() + ", " + (int) ciudadesAPintar.get(i).getCoordenadas().getY() + ")", (int) ciudadesAPintar.get(i).getCoordenadas().getX(), (int) ciudadesAPintar.get(i).getCoordenadas().getY());
            g.setColor(Color.RED);
            g.fillOval((int) ciudadesAPintar.get(i).getCoordenadas().getX(), (int) ciudadesAPintar.get(i).getCoordenadas().getY(), 5, 5);
            g.setColor(Color.BLACK);

            if (ciudadesRespuesta != null) {
            }
        }
        for (int j = 0; j < ciudadesRespuesta.size(); j++) {
            if (j != ciudadesRespuesta.size() - 1) {
                g.drawLine((int) ciudadesRespuesta.get(j).getCoordenadas().getX(), (int) ciudadesRespuesta.get(j).getCoordenadas().getY(), (int) ciudadesRespuesta.get(j + 1).getCoordenadas().getX(), (int) ciudadesRespuesta.get(j + 1).getCoordenadas().getY());

            } else {
                g.drawLine((int) ciudadesRespuesta.get(0).getCoordenadas().getX(), (int) ciudadesRespuesta.get(0).getCoordenadas().getY(), (int) ciudadesRespuesta.get(j).getCoordenadas().getX(), (int) ciudadesRespuesta.get(j).getCoordenadas().getY());
            }
        }
    }

    @Override
    public void run() {
        while (hilo != null) {
            x++;

            try {
                this.repaint();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
    }

    public void Start() {
        if (this.hilo == null) {
            this.hilo = new Thread(this);
        }

        if (!this.hilo.isAlive()) {
            this.hilo.start();
        }
    }

    public void Stop() {
        this.hilo.stop();
    }
}
