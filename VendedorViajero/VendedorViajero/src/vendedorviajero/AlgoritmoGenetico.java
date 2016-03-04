/*
 * Germán Pérez         0844987
 * Juan Manuel Ospina   0840730
 */
package vendedorviajero;

import java.awt.Point;
import java.util.*;

public class AlgoritmoGenetico {

    LinkedList<Ciudad> ciudades;
    LinkedList<Camino> poblacion;
    LinkedList<Camino> matingPool;
    LinkedList<Camino> poblacionCruzada;
    LinkedList<Camino> poblacionMutada;
    LinkedList<Camino> poblacionReemplazo;
    LinkedList<Camino> cromosomasSolucion;
    Camino cromosomaSolucion;
    Point[] puntosAUsar = new Point[100];
    int indiceCaminoAPintar = 0;
    int poblacionInicial = 0;
    int tamanoMatingPool;
    int constanteParada = 0; // Constante de parada D, sirve para saber si un camino tiene menor distancia a ella
    boolean solucion = false;

    public AlgoritmoGenetico(int poblacionInicial, int numeroCiudades, int tamanoMatingPool, int constanteParada) {
        this.tamanoMatingPool = tamanoMatingPool;
        this.poblacionInicial = poblacionInicial;
        this.constanteParada = constanteParada;
        cromosomasSolucion = new LinkedList<Camino>();
        poblacion = new LinkedList<Camino>();
        matingPool = new LinkedList<Camino>();
        ciudades = new LinkedList<Ciudad>();

        generarPuntosAleatorios();
        generarCiudadesAleatoriamente(numeroCiudades);
        generarPoblacionInicial(poblacionInicial, numeroCiudades);
    }

    // Puntos aleatorios iniciales
    public void generarPuntosAleatorios() {
        int acum = 0;

        for (Point punto : puntosAUsar) {
            int coordenadaX = (int) (Math.random() * 500);
            int coordenadaY = (int) (Math.random() * 500);
            punto = new Point(coordenadaX, coordenadaY);
            puntosAUsar[acum] = punto;
            acum++;

        }
    }
// imprimir puntos aleatorios

    // Ciudades generadas aleatoriamente
    public void generarCiudadesAleatoriamente(int numeroCiudades) {
        int acumCiudadesAñadidas = 0;
        String[] nombreCiudades = devolverNombresCiudades();

        while (acumCiudadesAñadidas < numeroCiudades) {

            try {
                int indiceCiudades = (int) (Math.random() * nombreCiudades.length);
                int indicePuntos = (int) (Math.random() * numeroCiudades);
                Ciudad nuevaCiudad = new Ciudad(puntosAUsar[indicePuntos], nombreCiudades[indiceCiudades]);

                añadirCiudad(nuevaCiudad);
                acumCiudadesAñadidas++;
            } catch (Exception excepcion) {
            }
        }
    }

    public void generarPoblacionInicial(int poblacionInicial, int numeroCiudades) {
        int indiceCiudadATomar = 0;

        for (int i = 0; i < poblacionInicial; i++) {
            //       System.out.println("Entrando camino " + (i + 1));
            Camino caminoNuevo = new Camino();
            Vector vectorIndices = new Vector();

            for (int j = 0; j < numeroCiudades; j++) {
                if (j == 0) {
                    indiceCiudadATomar = (int) (Math.random() * numeroCiudades);

                    vectorIndices.add(indiceCiudadATomar);
                } //else if ((j > 0) && (j < numeroCiudades-1))
                else {
                    do {
                        indiceCiudadATomar = (int) (Math.random() * numeroCiudades);
                    } while (vectorIndices.contains(indiceCiudadATomar));

                    vectorIndices.add(indiceCiudadATomar);
                }

                try {
                    Ciudad ciudadNueva = ciudades.get(indiceCiudadATomar);
                    caminoNuevo.añadirCiudad(ciudadNueva);
                } catch (Exception excepcion) {
                }
            }

            caminoNuevo.calcularDistanciaEuclidesCamino();
            poblacion.add(caminoNuevo);

        }
    }

    // Selección mediante torneo de un grupo de cromosomas dependiendo de su aptitud, en este caso la distancia total de cada camino, y se llevan al mating pool
    public void presionSelectiva() {

        Camino cromosoma1;
        Camino cromosoma2;

        for (int i = 0; i < tamanoMatingPool; i++) {
            int indice1 = (int) Math.floor(Math.random() * (poblacion.size()));
            cromosoma1 = (Camino) poblacion.get(indice1);

            int indice2 = 0;

            do {
                indice2 = (int) Math.floor(Math.random() * (poblacion.size()));
            } while (indice1 == indice2);

            cromosoma2 = (Camino) poblacion.get(indice2);

            if (cromosoma1.getDistanciaEuclidesCamino() < cromosoma2.getDistanciaEuclidesCamino()) {
                matingPool.add(poblacion.get(indice1));
            } else {
                matingPool.add(poblacion.get(indice2));
            }
        }
    }

    // Cruce por orden
    public void cruce(Camino caminoPadre, Camino caminoMadre) {
        Camino hijo1 = new Camino();
        Camino hijo2 = new Camino();

        int puntoCorte1 = (int) Math.floor(Math.random() * (caminoPadre.getCamino().size()));
        int puntoCorte2 = 0;
        int aux = 0;

        do {
            puntoCorte2 = (int) Math.floor(Math.random() * (caminoPadre.getCamino().size()));
        } while ((puntoCorte1 == puntoCorte2) || (puntoCorte2 == puntoCorte1 + 1) || (puntoCorte2 == puntoCorte1 - 1));

        if (puntoCorte1 > puntoCorte2) {
            aux = puntoCorte2;
            puntoCorte2 = puntoCorte1;
            puntoCorte1 = aux;
        }

        for (int i = 0; i <= caminoPadre.getCamino().size(); i++) {
            if ((i >= puntoCorte1) && (i <= puntoCorte2)) {
                hijo1.añadirCiudad(caminoMadre.getCiudadDeCamino(i));
                hijo2.añadirCiudad(caminoPadre.getCiudadDeCamino(i));
            } else {
                hijo1.añadirCiudad(new Ciudad(new Point(0, 0), "x"));
                hijo2.añadirCiudad(new Ciudad(new Point(0, 0), "x"));
            }
        }


        Camino caminoAux1 = new Camino();
        Camino caminoAux2 = new Camino();

        // Se crean los dos vectores auxiliares
        for (int i = puntoCorte2; i < caminoPadre.getCamino().size(); i++) {
            if (i != caminoPadre.getCamino().size() - 1) {
                caminoAux1.añadirCiudad(caminoPadre.getCiudadDeCamino(i + 1));
                caminoAux2.añadirCiudad(caminoMadre.getCiudadDeCamino(i + 1));
            }
        }

        for (int i = 0; i <= puntoCorte2; i++) {
            caminoAux1.añadirCiudad(caminoPadre.getCiudadDeCamino(i));
            caminoAux2.añadirCiudad(caminoMadre.getCiudadDeCamino(i));
        }

        // Se crea el hijo 1       
        for (int i = 0; i < caminoAux1.getCamino().size(); i++) {
            if (!hijo1.getCamino().contains(caminoAux1.getCamino().get(i))) {
                for (int j = 0; j < hijo1.getCamino().size(); j++) {
                    if (hijo1.getCiudadDeCamino(j).getNombre().equals("x")) {
                        hijo1.setCambioCiudad(caminoAux1.getCamino().get(i), j);
                    }
                }
            }
        }

        // Se crea el hijo 2
        for (int i = 0; i < caminoAux2.getCamino().size(); i++) {
            if (!hijo2.getCamino().contains(caminoAux2.getCamino().get(i))) {
                for (int j = 0; j < hijo2.getCamino().size(); j++) {
                    if (hijo2.getCiudadDeCamino(j).getNombre().equals("x")) {
                        hijo2.setCambioCiudad(caminoAux2.getCamino().get(i), j);
                    }
                }
            }
        }

        hijo1.calcularDistanciaEuclidesCamino();
        hijo2.calcularDistanciaEuclidesCamino();
        poblacionCruzada.add(hijo1);
        poblacionCruzada.add(hijo2);
    }

    // Mutación
    public Camino mutacion(Camino camino) {
        Camino caminoAMutar = camino;
        int puntoAReemplazar = (int) (Math.floor(Math.random() * (camino.getCamino().size())));
        int puntoReemplazo = 0;

        // Gen que se va a reemplazar en el cromosoma
        Ciudad ciudadAReemplazar = camino.getCiudadDeCamino(puntoAReemplazar);

        // Gen utilizado para reemplazarlo
        do {
            puntoReemplazo = (int) Math.floor(Math.random() * (camino.getCamino().size()));
        } while (puntoAReemplazar == puntoReemplazo);

        Ciudad ciudadReemplazo = camino.getCiudadDeCamino(puntoReemplazo);

        caminoAMutar.setCambioCiudad(ciudadReemplazo, puntoAReemplazar);
        caminoAMutar.setCambioCiudad(ciudadAReemplazar, puntoReemplazo);

        caminoAMutar.calcularDistanciaEuclidesCamino();

        return caminoAMutar;
    }

    // Reproducción de los cromosomas del mating pool
    public void reproducirCromosomas() {
        poblacionCruzada = new LinkedList<Camino>();
        poblacionMutada = new LinkedList<Camino>();

        // Hacemos primo el cruce
        int numeroCruces = (int) tamanoMatingPool / 2;

        for (int i = 0; i < numeroCruces; i++) {
            int punto1 = (int) Math.floor(Math.random() * (tamanoMatingPool));
            int punto2 = 0;

            do {
                punto2 = (int) Math.floor(Math.random() * (tamanoMatingPool));
            } while (punto1 == punto2);

            cruce(matingPool.get(punto1), matingPool.get(punto2));
        }

        // Hacemos la mutación a cada cromosoma
        for (int i = 0; i < poblacionCruzada.size(); i++) {
            int probabilidad = (int) Math.floor(Math.random() * 100);

            if (probabilidad < 10) {
                Camino caminoMutado = mutacion(poblacionCruzada.get(i));
                poblacionMutada.add(caminoMutado);
            } else {
                poblacionMutada.add(poblacionCruzada.get(i));
            }
        }
    }

    // Reemplazamos los viejos cromosomas por los nuevos
    public void reemplazoInmediato() {
        poblacionReemplazo = new LinkedList();

        // Reemplazamos los k primeros cromosomas de la poblacion inicial
        for (int i = 0; i < tamanoMatingPool; i++) {
            poblacion.set(i, poblacionMutada.get(i));
        }

        for (int i = 0; i < poblacion.size(); i++) {
            if (poblacion.get(i).getDistanciaEuclidesCamino() <= constanteParada) {
                setSolucion();
                // System.out.println("Se encontro la solución " + i + " : " + poblacion.get(i).getDistanciaEuclidesCamino());
                cromosomaSolucion = poblacion.get(i);
                cromosomasSolucion.add(cromosomaSolucion);
                //return;
            }
        }
        cromosomaSolucion = devolverMejorCromosoma();
    }

    public Camino devolverMejorCromosoma() {
        double puntuacion = Double.MAX_EXPONENT;
        Camino aux = null;
        for (Camino camino : cromosomasSolucion) {
            if (camino.getDistanciaEuclidesCamino() < puntuacion) {
                puntuacion = camino.getDistanciaEuclidesCamino();
                aux = camino;
            }
        }
        System.out.println(" El mejor cromosoma va a devolver la distancia " + aux.getDistanciaEuclidesCamino());
        return aux;
    }

    public void setSolucion() {
        solucion = true;
    }

    public boolean getSolucion() {
        return solucion;
    }

    public Camino getCromosomaSolucion() {
        return cromosomaSolucion;
    }

    public String[] devolverNombresCiudades() {
        String[] nombresCiudades = new String[101];

        nombresCiudades[0] = "Cali";
        nombresCiudades[1] = "Pereira";
        nombresCiudades[2] = "Pasto";
        nombresCiudades[3] = "Bogota";
        nombresCiudades[4] = "Quibdo";
        nombresCiudades[5] = "Medellin";
        nombresCiudades[6] = "Barranquilla";
        nombresCiudades[7] = "Cartagena de Indias";
        nombresCiudades[8] = "Cúcuta";
        nombresCiudades[9] = "Soledad";
        nombresCiudades[10] = "Ibagué";
        nombresCiudades[11] = "Bucaramanga";
        nombresCiudades[12] = "Soacha";
        nombresCiudades[13] = "Santa Marta";
        nombresCiudades[14] = "Pereira";
        nombresCiudades[15] = "Villavicencio";
        nombresCiudades[16] = "Bello";
        nombresCiudades[17] = "Valledupar";
        nombresCiudades[18] = "Pasto";
        nombresCiudades[19] = "Montería";
        nombresCiudades[20] = "Manizales";
        nombresCiudades[21] = "Buenaventura ";
        nombresCiudades[22] = "Neiva";
        nombresCiudades[23] = "Abejorral";
        nombresCiudades[24] = "Alejandria";
        nombresCiudades[25] = "Altamira";
        nombresCiudades[26] = "Amaga";
        nombresCiudades[27] = "Andes";
        nombresCiudades[28] = "Angelopolis";
        nombresCiudades[29] = "Anza";
        nombresCiudades[30] = "Apartado";
        nombresCiudades[31] = "Arboletes";
        nombresCiudades[32] = "Armenia";
        nombresCiudades[33] = "Barbosa";
        nombresCiudades[34] = "Bello";
        nombresCiudades[35] = "Betania";
        nombresCiudades[36] = "Briceño";
        nombresCiudades[37] = "Pereira";
        nombresCiudades[38] = "Cañasgordas";
        nombresCiudades[39] = "Caldas";
        nombresCiudades[40] = "Carepa";
        nombresCiudades[41] = " Carmen De Viboral";
        nombresCiudades[42] = "Carolina";
        nombresCiudades[43] = "Casabe";
        nombresCiudades[44] = "Caucasia ";
        nombresCiudades[45] = "Chigorodo";
        nombresCiudades[46] = "Cisneros";
        nombresCiudades[47] = "Cocorna";
        nombresCiudades[48] = "Concepción";
        nombresCiudades[49] = "Concordia";
        nombresCiudades[50] = "Copacabana";
        nombresCiudades[51] = "Dabeiba";
        nombresCiudades[52] = "Don Matias";
        nombresCiudades[53] = "Ebejico";
        nombresCiudades[54] = "El Bagre";
        nombresCiudades[55] = " El Poblado ";
        nombresCiudades[56] = "Entrerios";
        nombresCiudades[57] = "Envigado";
        nombresCiudades[58] = "Fredonia";
        nombresCiudades[59] = "Frontino";
        nombresCiudades[60] = "Girardota";
        nombresCiudades[61] = " Gomez Plata ";
        nombresCiudades[62] = "Granada";
        nombresCiudades[63] = "Guadalupe";
        nombresCiudades[64] = "Guarne";
        nombresCiudades[65] = "Guatape";
        nombresCiudades[66] = "Heliconia";
        nombresCiudades[67] = "Hispania ";
        nombresCiudades[68] = "Itagüi";
        nombresCiudades[69] = "Jardín";
        nombresCiudades[70] = "Jerico";
        nombresCiudades[71] = "La Ceja";
        nombresCiudades[72] = "La Estrella";
        nombresCiudades[73] = "La Pintada";
        nombresCiudades[74] = "La Union";
        nombresCiudades[75] = "Maceo";
        nombresCiudades[76] = "Marinilla";
        nombresCiudades[77] = "Montebello ";
        nombresCiudades[78] = "Murindo";
        nombresCiudades[79] = "Mutata";
        nombresCiudades[80] = "Nechi";
        nombresCiudades[81] = "Necocli";
        nombresCiudades[82] = "Olaya";
        nombresCiudades[83] = "Peñol";
        nombresCiudades[84] = "Puerto Berrio";
        nombresCiudades[85] = "Puerto Triunfo";
        nombresCiudades[86] = "Remedios";
        nombresCiudades[87] = "Retiro";
        nombresCiudades[88] = "Ríonegro";
        nombresCiudades[89] = "Sabaneta";
        nombresCiudades[90] = "San Andres Isla";
        nombresCiudades[91] = "San Carlos";
        nombresCiudades[92] = "Santa Marta";
        nombresCiudades[93] = "San Cristóbal ";
        nombresCiudades[94] = "San Francisco";
        nombresCiudades[95] = "Segovia";
        nombresCiudades[96] = "Taraza";
        nombresCiudades[97] = "Venecia";
        nombresCiudades[98] = "Yolombo";
        nombresCiudades[99] = "Zaragoza";
        nombresCiudades[100] = "Tamesis ";

        return nombresCiudades;
    }

    public void añadirCiudad(Ciudad ciudad) throws Exception {
        Ciudad ciudadNueva = null;

        for (int i = 0; i < ciudades.size(); i++) {
            ciudadNueva = ciudades.get(i);

            if (ciudad.getNombre().equalsIgnoreCase(ciudadNueva.getNombre()) || ((ciudadNueva.getCoordenadas().getX() == ciudad.getCoordenadas().getX()) && (ciudadNueva.getCoordenadas().getY() == ciudad.getCoordenadas().getY()))) {
                throw new Exception("La ciudad con esos parametros ya existe");
            }
        }

        ciudades.add(ciudad);
        //  System.out.println("se añadio la ciudad" + ciudad.getNombre());
    }

    public LinkedList<Ciudad> getCiudades() {
        return ciudades;
    }
}