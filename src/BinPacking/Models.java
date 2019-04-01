package BinPacking;

import java.util.LinkedList;

/**
 *
 * @author Quiroz Aguilar Juan Carlos
 * @version 1.0.0
 */
public class Models {

}

class Caja {

    int id;
    double capacidad;
    LinkedList<Item> Objetos;
    double CapacidadRestante;

    Caja(int id) {
        this.id = id;
        capacidad = 1.0;
        Objetos = new LinkedList<>();
        CapacidadRestante = 1.0;
    }
}

class Item {

    int Id;
    double Peso;
    boolean Disponible;

    Item(int id, double peso) {
        this.Peso = peso;
        this.Id = id;
        this.Disponible = true;

    }
}

class Cromosoma implements Comparable<Cromosoma> {

    int Id;
    LinkedList<Caja> ConfiguracionCajas;
    double fitnes;
    double proporcioninferior;
    double proporcionsuperior;
    boolean disponible;

    Cromosoma(int id) {
        this.Id = id;
        this.ConfiguracionCajas = new LinkedList<>();
        this.proporcioninferior = 0.0;
        this.proporcionsuperior = 0.0;
        this.disponible = true;
    }

    @Override
    public int compareTo(Cromosoma o) {
        if (fitnes > o.fitnes) {
            return -1;
        }
        if (fitnes == o.fitnes) {
            return 0;
        }
        return 1;
    }

}

class Generacion {

    int id;
    LinkedList<Cromosoma> Generacion;

    Generacion(int id) {
        this.id = id;
        this.Generacion = new LinkedList<>();
    }
}
