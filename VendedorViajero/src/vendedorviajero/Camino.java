/*
 * Germán Pérez         0844987
 * Juan Manuel Ospina   0840730
 */
package vendedorviajero;

import java.util.LinkedList;

public class Camino {

    double distanciaEuclidesCamino;
    LinkedList<Ciudad> camino;

    public Camino() {
        distanciaEuclidesCamino = 0;
        camino = new LinkedList<Ciudad>();
    }

    public Ciudad getCiudadDeCamino(int indice) {
        return camino.get(indice);
    }

    public LinkedList<Ciudad> getCamino() {
        return camino;
    }

    public double getDistanciaEuclidesCamino() {
        return distanciaEuclidesCamino;
    }

    public void setCambioCiudad(Ciudad ciudad, int indice) {
        camino.set(indice, ciudad);
    }

    public void añadirCiudad(Ciudad ciudad) {
        camino.add(ciudad);
    }

    public void calcularDistanciaEuclidesCamino() {
        double distanciaEuclides = 0;

        for (int i = 0; i < camino.size(); i++) {
            if (i != camino.size() - 1) {
                distanciaEuclides += Math.sqrt(Math.pow((camino.get(i + 1).getCoordenadas().getX() - camino.get(i).getCoordenadas().getX()), 2)
                        + Math.pow((camino.get(i + 1).getCoordenadas().getY() - camino.get(i).getCoordenadas().getY()), 2));
            } else {
                distanciaEuclides += Math.sqrt(Math.pow((camino.get(0).getCoordenadas().getX() - camino.get(i).getCoordenadas().getX()), 2)
                        + Math.pow((camino.get(0).getCoordenadas().getY() - camino.get(i).getCoordenadas().getY()), 2));
            }

            distanciaEuclidesCamino = distanciaEuclides;
        }
    }

    public void mutarUnaRuta(int indice, Ciudad ciudad) {
        camino.set(indice, ciudad);
    }

    public void imprimirCamino() {
        for (int i = 0; i < camino.size(); i++) {
            if (i != camino.size() - 1) {
                System.out.print(camino.get(i).nombre + ", ");
            } else {
                System.out.print(camino.get(i).nombre);
            }
        }
    }
}