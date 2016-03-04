/*
 * Germán Pérez         0844987
 * Juan Manuel Ospina   0840730
 */
package main;

import gui.Animacion;
import java.awt.Point;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import vendedorviajero.AlgoritmoGenetico;
import vendedorviajero.Camino;
import vendedorviajero.Ciudad;

/**
 *
 * @author juan
 */
public class Main {

    public static void main(String[] args) {
        int numeroCiudades = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de ciudades que desee generar\n(Mínimo 5 ciudades)"));
        int numeroGeneracionesMinimas = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el numero de generaciones para el algoritmo"));

        int poblacionInicial = 100;
        int tamanoMatingPool = 30;
        int constanteParada = 1500;
        int numeroGeneracion = 0;
        if (numeroCiudades >= 5) {
            AlgoritmoGenetico algoritmoGenetico = new AlgoritmoGenetico(poblacionInicial, numeroCiudades, tamanoMatingPool, constanteParada);

            // Mientras no se encuentre solucion
            while ((!algoritmoGenetico.getSolucion() || (numeroGeneracion < numeroGeneracionesMinimas))) {

                algoritmoGenetico.presionSelectiva();  // Se hace seleccion por torneo
                algoritmoGenetico.reproducirCromosomas();// Se realiza Cruce y Mutacion
                algoritmoGenetico.reemplazoInmediato(); // de los hijos se selecciona un numero  N por torneo y se reemplaza la poblacion inicial en su totalidad
                numeroGeneracion++;             //aumentamos la generacion
                System.out.println("numero de generacion" + numeroGeneracion);
            }


            if (algoritmoGenetico.getSolucion()) {
                LinkedList<Ciudad> ciudadesSolucion = null;
                ciudadesSolucion = algoritmoGenetico.getCiudades();
                Animacion animacion = new Animacion(algoritmoGenetico, ciudadesSolucion, numeroGeneracion);
                JFrame f = new JFrame();
                f.setSize(700, 700);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setResizable(false);
                f.setLocationRelativeTo(null);
                f.add(animacion);
                //        animacion.setLocation(25, 25);
                f.setVisible(true);
                animacion.Start();


            }
        }
    }
}
