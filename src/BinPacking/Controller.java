package BinPacking;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Quiroz Aguilar Juan Carlos
 * @author Aguirre Mendoza Jessica
 * @author Mendoza Mendoza Yehosuah Eli
 */
public class Controller {

    Random r = new Random();

    /**
     *
     * Inicializa los paquetes con un peso aleatorio
     *
     * @param cantidad numenro de paquetes a crear
     * @return Items Lista de paquetes creados
     */
    public LinkedList<Item> Inicializar(int cantidad) {

        LinkedList<Item> items = new LinkedList<>();
        double peso;
        Item item;
        for (int i = 0; i < cantidad; i++) {
            peso = r.nextDouble();
            item = new Item(i, peso);
            items.add(item);
        }
        return items;
    }

    /**
     *
     * Genera la poblacion inicial del algoritmo
     *
     * @param cantidad cantidad de cromosomas en la poblacion incial
     * @param items los paquetes que se generaron
     * @return generacion Coleccion de cromosomas
     */
    public LinkedList<Cromosoma> PoblacionInicial(int cantidad,
            LinkedList<Item> items) {
        LinkedList<Cromosoma> generacion = new LinkedList<>();
        //generacion de cromosomas
        int num;
        int sizeItems;
        for (int i = 0; i < cantidad; i++) {
            int contcaja = 0;
            sizeItems = items.size();
            Cromosoma cromo = new Cromosoma(i);
            Caja c1 = new Caja(contcaja);
            cromo.ConfiguracionCajas.add(c1);
            while (sizeItems > 0) {
                num = r.nextInt(items.size());
                if (items.get(num).Disponible) {
                    if (items.get(num).Peso
                            <= cromo.ConfiguracionCajas.get(contcaja).CapacidadRestante) {
                        cromo.ConfiguracionCajas.get(contcaja).Objetos.add(items.get(num));
                        items.get(num).Disponible = false;
                        cromo.ConfiguracionCajas.get(contcaja).CapacidadRestante
                                = cromo.ConfiguracionCajas.get(contcaja).CapacidadRestante
                                - items.get(num).Peso;
                        sizeItems--;
                    } else {
                        contcaja++;
                        cromo.ConfiguracionCajas.add(new Caja(contcaja));
                        cromo.ConfiguracionCajas.get(contcaja).Objetos.add(items.get(num));
                        items.get(num).Disponible = false;
                        cromo.ConfiguracionCajas.get(contcaja).CapacidadRestante
                                = cromo.ConfiguracionCajas.get(contcaja).CapacidadRestante
                                - items.get(num).Peso;
                        sizeItems--;

                    }
                }
            }
            for (int j = 0; j < items.size(); j++) {
                items.get(j).Disponible = true;
            }

            fitness(cromo);
            generacion.add(cromo);

        }
        return generacion;

    }

    /**
     * Calcula el fitness para cada cromosoma de una generacion
     *
     * @param cromo cromosoma que se desea calcular el fitness
     */
    public void fitness(Cromosoma cromo) {
        double sum = 0;
        double F, f, c, k;

        k = 2;

        for (int i = 0; i < cromo.ConfiguracionCajas.size(); i++) {
            f = cromo.ConfiguracionCajas.get(i).CapacidadRestante;
            c = cromo.ConfiguracionCajas.get(i).capacidad;
            sum = sum + Math.pow(f / c, k);
        }

        F = sum / cromo.ConfiguracionCajas.size();

        cromo.fitnes = F;
    }

    /**
     * Recibe un cromosoma y lo muta eligiendo al azar una caja
     *
     * @param cromo cromosoma a mutar
     * @return El cromosoma mutado
     */
    public Cromosoma Mutacion(Cromosoma cromo) {
        int indexmut = r.nextInt(cromo.ConfiguracionCajas.size());
        LinkedList<Item> ItemsLibres = new LinkedList<>();
        Item item;
        boolean añadido = true;
        int indexcaja;
        Caja c2;
        Caja c = cromo.ConfiguracionCajas.get(indexmut);
        for (int i = 0; i < cromo.ConfiguracionCajas.get(indexmut).Objetos.size(); i++) {
            ItemsLibres.add(cromo.ConfiguracionCajas.get(indexmut).Objetos.get(i));
        }
        cromo.ConfiguracionCajas.remove(c);

        for (int i = 0; i < ItemsLibres.size(); i++) {
            item = ItemsLibres.get(i);
            indexcaja = 0;
            while (añadido) {
                if (indexcaja == cromo.ConfiguracionCajas.size()) {
                    c2 = new Caja(10);
                    c2.Objetos.add(item);
                    c2.CapacidadRestante = c2.CapacidadRestante - item.Peso;
                    cromo.ConfiguracionCajas.add(c2);
                    añadido = false;
                }
                c2 = cromo.ConfiguracionCajas.get(indexcaja);
                if (item.Peso < c2.CapacidadRestante) {
                    c2.Objetos.add(item);
                    c2.CapacidadRestante = c2.CapacidadRestante - item.Peso;
                    añadido = false;
                } else {
                    indexcaja++;
                }

            }
        }

        return cromo;
    }

    /**
     * Ordena los cromosomas deacuerdo a su fitness
     *
     * @param poblacion conjunto de cromosomas a ordenar
     * @return conjunto de cromosomas ordenado
     */
    public LinkedList<Cromosoma> OrdenaFitness(LinkedList<Cromosoma> poblacion) {
        Collections.sort(poblacion);
        return poblacion;
    }

    /**
     * Selecciona un conjunto de cromosomas de acuerdo al metodo seleccionado
     *
     * @param Poblacion conjunto de cromosomas aptos para ser seleccionados
     * @param opc 0 para torneo y 1 para amplitud
     * @param probabilidad probabilidad que un cromosoma sea seleccionado
     * @return un conjunto de cromosomas padres
     */
    public LinkedList<Cromosoma> Seleccion(LinkedList<Cromosoma> Poblacion, int opc, double probabilidad) {
        LinkedList<Cromosoma> Padres = new LinkedList<>();
        int tam = (int) (Poblacion.size() * probabilidad);
        if (opc == 0) {
            //Seleccion Por Torneo
            Cromosoma padre1;
            Cromosoma padre2;
            int indexpadre1, indexpadre2;
            while (Padres.size() != tam) {
                do {
                    indexpadre1 = r.nextInt(Poblacion.size());
                    indexpadre2 = r.nextInt(Poblacion.size());
                } while (indexpadre1 == indexpadre2);
                padre1 = Poblacion.get(indexpadre1);
                padre2 = Poblacion.get(indexpadre2);
                Poblacion.remove(padre1);
                Poblacion.remove(padre2);

                if (padre1.fitnes > padre2.fitnes) {
                    Padres.add(padre1);
                } else {
                    Padres.add(padre2);
                }

            }

        } else {
            double sumfit = 0.0;
            double count = 0.0;
            double aptitud;
            int index = 0;
            boolean encontrado;
            int numapt;
            for (int i = 0; i < Poblacion.size(); i++) {
                sumfit = sumfit + Poblacion.get(i).fitnes;
            }
            for (int i = 0; i < Poblacion.size(); i++) {
                aptitud = (Poblacion.get(i).fitnes * 360) / sumfit;
                Poblacion.get(i).proporcioninferior = count;
                Poblacion.get(i).proporcionsuperior = count + aptitud;
                count = count + aptitud;
            }
            while (Padres.size() != tam) {
                index = 0;
                encontrado = true;
                numapt = r.nextInt(361);
                while (encontrado && index < Poblacion.size()) {
                    if ((Poblacion.get(index).proporcioninferior <= numapt)
                            && Poblacion.get(index).proporcionsuperior > numapt
                            && Poblacion.get(index).disponible) {
                        Padres.add(Poblacion.get(index));
                        encontrado = false;
                        Poblacion.get(index).disponible = false;
                    } else {
                        index++;
                    }
                }

            }

        }

        return Padres;
    }

    /**
     * Toma dos cromosomas padres, elige un punto de cruze y genera una solucion
     * valida apartir de cromosomas inciales
     *
     * @param padres par de cromosomas del cual se obtendran dos hijos nuevos
     * @return generacion descendiente de los padres
     */
    public LinkedList<Cromosoma> Cruzamiento(LinkedList<Cromosoma> padres) {
        LinkedList<Cromosoma> Generacion = new LinkedList<>();
        Cromosoma padre1, padre2, hijo1, hijo2;
        int puntodecruze1, puntodecruze2;
        LinkedList<Caja> Cajamutada1, Cajamutada2;
        LinkedList<Item> itemslibres = new LinkedList<>();
        int id;
        LinkedList<Caja> eliminar = new LinkedList<>();
        while (!(padres.isEmpty())) {
            if (padres.size() == 1) {
                Generacion.add(padres.pollFirst());
            } else {
                padre1 = padres.pollLast();
                padre2 = padres.pollLast();
                do {
                    puntodecruze1 = r.nextInt(padre1.ConfiguracionCajas.size());
                    puntodecruze2 = r.nextInt(padre2.ConfiguracionCajas.size());
                } while ((puntodecruze1 == puntodecruze2) || (puntodecruze1 > puntodecruze2) || puntodecruze1 == 0);
                hijo1 = new Cromosoma(1);
                hijo2 = new Cromosoma(2);
                Cajamutada1 = new LinkedList<>();
                Cajamutada2 = new LinkedList<>();
                for (int i = 0; i < puntodecruze1; i++) {
                    hijo1.ConfiguracionCajas.add(padre1.ConfiguracionCajas.get(i));
                }

                for (int i = 0; i < puntodecruze2; i++) {
                    hijo1.ConfiguracionCajas.add(padre2.ConfiguracionCajas.get(i));
                    Cajamutada1.add(padre2.ConfiguracionCajas.get(i));
                }
                for (int i = puntodecruze1; i < padre1.ConfiguracionCajas.size(); i++) {
                    hijo1.ConfiguracionCajas.add(padre1.ConfiguracionCajas.get(i));
                }

                for (int i = puntodecruze1; i <= puntodecruze2; i++) {
                    for (int j = 0; j < hijo1.ConfiguracionCajas.get(i).Objetos.size(); j++) {
                        Item itemverificar = hijo1.ConfiguracionCajas.get(i).Objetos.get(j);
                        for (int k = 0; k < puntodecruze1; k++) {
                            for (int l = 0; l < hijo1.ConfiguracionCajas.get(k).Objetos.size(); l++) {
                                Item itemcveri = hijo1.ConfiguracionCajas.get(k).Objetos.get(l);
                                if (itemverificar.Id == itemcveri.Id) {
                                    eliminar.add(hijo1.ConfiguracionCajas.get(k));
                                    itemcveri.Disponible = false;
                                    break;
                                }
                            }

                        }
                        for (int k = puntodecruze2 + 1; k < hijo1.ConfiguracionCajas.size(); k++) {
                            for (int l = 0; l < hijo1.ConfiguracionCajas.get(k).Objetos.size(); l++) {
                                Item itemcveri = hijo1.ConfiguracionCajas.get(k).Objetos.get(l);
                                if (itemverificar.Id == itemcveri.Id) {
                                    eliminar.add(hijo1.ConfiguracionCajas.get(k));
                                    itemcveri.Disponible = false;
                                    break;
                                }
                            }

                        }

                    }
                }

                for (int i = 0; i < eliminar.size(); i++) {
                    for (int j = 0; j < eliminar.get(i).Objetos.size(); j++) {
                        if (eliminar.get(i).Objetos.get(j).Disponible) {
                            itemslibres.add(eliminar.get(i).Objetos.get(j));
                        }
                    }
                    hijo1.ConfiguracionCajas.remove(eliminar.get(i));
                }
                for (int i = 0; i < itemslibres.size(); i++) {
                    boolean añadido = true;
                    int indexcaja = 0;
                    while (añadido) {
                        if (indexcaja == hijo1.ConfiguracionCajas.size()) {
                            hijo1.ConfiguracionCajas.add(new Caja(10 * indexcaja));

                            hijo1.ConfiguracionCajas.get(hijo1.ConfiguracionCajas.size() - 1).CapacidadRestante
                                    = hijo1.ConfiguracionCajas.get(hijo1.ConfiguracionCajas.size() - 1).CapacidadRestante
                                    - itemslibres.get(i).Peso;
                            hijo1.ConfiguracionCajas.get(hijo1.ConfiguracionCajas.size() - 1).Objetos.add(itemslibres.get(i));
                            añadido = false;

                        } else if (itemslibres.get(i).Peso < hijo1.ConfiguracionCajas.get(indexcaja).CapacidadRestante) {
                            hijo1.ConfiguracionCajas.get(indexcaja).CapacidadRestante
                                    = hijo1.ConfiguracionCajas.get(indexcaja).CapacidadRestante - itemslibres.get(i).Peso;
                            añadido = false;
                            hijo1.ConfiguracionCajas.get(indexcaja).Objetos.add(itemslibres.get(i));
                        } else {
                            indexcaja++;
                        }
                    }
                }
                itemslibres.clear();
                for (int i = 0; i < puntodecruze2; i++) {
                    hijo2.ConfiguracionCajas.add(padre2.ConfiguracionCajas.get(i));
                }
                hijo2.ConfiguracionCajas.add(padre1.ConfiguracionCajas.get(puntodecruze2));
                Cajamutada2.add(padre1.ConfiguracionCajas.get(puntodecruze2));

                for (int i = puntodecruze2; i < padre2.ConfiguracionCajas.size(); i++) {
                    hijo2.ConfiguracionCajas.add(padre2.ConfiguracionCajas.get(i));
                }
                for (int i = 0; i < hijo2.ConfiguracionCajas.get(puntodecruze2).Objetos.size(); i++) {
                    Item itemverificar = hijo2.ConfiguracionCajas.get(puntodecruze2).Objetos.get(i);
                    for (int j = 0; j < puntodecruze2; j++) {
                        for (int k = 0; k < hijo2.ConfiguracionCajas.get(j).Objetos.size(); k++) {
                            Item itemcveri = hijo2.ConfiguracionCajas.get(j).Objetos.get(k);
                            if (itemverificar.Id == itemcveri.Id) {
                                eliminar.add(hijo2.ConfiguracionCajas.get(j));
                                itemcveri.Disponible = false;
                                break;
                            }
                        }
                    }
                    for (int j = puntodecruze2 + 1; j < hijo2.ConfiguracionCajas.size(); j++) {
                        for (int k = 0; k < hijo2.ConfiguracionCajas.get(j).Objetos.size(); k++) {
                            Item itemcveri = hijo2.ConfiguracionCajas.get(j).Objetos.get(k);
                            if (itemverificar.Id == itemcveri.Id) {
                                eliminar.add(hijo2.ConfiguracionCajas.get(j));
                                itemcveri.Disponible = false;
                                break;
                            }
                        }
                    }

                }
                for (int i = 0; i < eliminar.size(); i++) {
                    for (int j = 0; j < eliminar.get(i).Objetos.size(); j++) {
                        if (eliminar.get(i).Objetos.get(j).Disponible) {
                            itemslibres.add(eliminar.get(i).Objetos.get(j));
                        }
                    }
                    hijo2.ConfiguracionCajas.remove(eliminar.get(i));
                }
                for (int i = 0; i < itemslibres.size(); i++) {
                    boolean añadido = true;
                    int indexcaja = 0;
                    while (añadido) {
                        if (indexcaja == hijo2.ConfiguracionCajas.size()) {
                            hijo2.ConfiguracionCajas.add(new Caja(10 * indexcaja));

                            hijo2.ConfiguracionCajas.get(hijo2.ConfiguracionCajas.size() - 1).CapacidadRestante
                                    = hijo2.ConfiguracionCajas.get(hijo2.ConfiguracionCajas.size() - 1).CapacidadRestante
                                    - itemslibres.get(i).Peso;
                            hijo2.ConfiguracionCajas.get(hijo2.ConfiguracionCajas.size() - 1).Objetos.add(itemslibres.get(i));
                            añadido = false;

                        } else if (itemslibres.get(i).Peso < hijo2.ConfiguracionCajas.get(indexcaja).CapacidadRestante) {
                            hijo2.ConfiguracionCajas.get(indexcaja).CapacidadRestante
                                    = hijo2.ConfiguracionCajas.get(indexcaja).CapacidadRestante - itemslibres.get(i).Peso;
                            añadido = false;
                            hijo2.ConfiguracionCajas.get(indexcaja).Objetos.add(itemslibres.get(i));
                        } else {
                            indexcaja++;
                        }
                    }
                }
                itemslibres.clear();
                Generacion.add(hijo1);
                Generacion.add(hijo2);
            }

        }

        return Generacion;
    }

}
