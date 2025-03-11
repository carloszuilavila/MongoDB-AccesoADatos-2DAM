package org.educa.dao;

import org.educa.entity.VueloWithRelations;

import java.util.List;

public interface VueloDAO {
    /**
     * Obtiene la información de los vuelos junto a la información de su aeropuerto de origen y aeropuerto de destino.
     *
     * @return una lista de objetos {@link VueloWithRelations}.
     */
    List<VueloWithRelations> getBeneficioVuelo();
}
