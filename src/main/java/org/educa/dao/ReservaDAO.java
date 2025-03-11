package org.educa.dao;

import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.List;

public interface ReservaDAO {
    /**
     * Obtiene una lista de todas las reservas.
     *
     * @return una lista de objetos {@link ReservaEntity}.
     */
    List<ReservaEntity> findAll();

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return la entidad {@link ReservaEntity} encontrada.
     */
    ReservaEntity findById(int id);

    /**
     * Guarda una nueva reserva en la base de datos.
     *
     * @param reserva la entidad {@link ReservaEntity} a guardar.
     * @return el identificador generado para la nueva reserva.
     */
    Integer save(ReservaEntity reserva);

    /**
     * Actualiza los datos de una reserva existente.
     *
     * @param reservaToUpdate la entidad {@link ReservaEntity} con los datos actualizados.
     * @return el número de filas afectadas por la actualización.
     */
    Long update(ReservaEntity reservaToUpdate);

    /**
     * Elimina una reserva por su identificador.
     *
     * @param id el identificador de la reserva a eliminar.
     * @return el número de filas afectadas por la eliminación.
     */
    Long delete(int id);

    /**
     * Busca las reservas cuyo precio sea igual o superior al especificado.
     *
     * @param cantidad el precio a partir del cuál se filtran las reservas.
     * @return una lista de objetos {@link ReservaWithRelations} añadiendo información relevante de las reservas.
     */
    List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad);

    /**
     * Busca los vuelos asociados a un número de pasaporte específico.
     *
     * @param pasaporte el número de pasaporte del pasajero.
     * @return un objeto {@link InfoPasajero} con la información del pasajero y sus vuelos.
     */
    InfoPasajero findReservasByPasaporte(String pasaporte);

    /**
     * Busca reservas asociadas a un vuelo específico.
     *
     * @param vueloId el identificador del vuelo.
     * @return una lista de objetos {@link ReservaEntity} relacionados con el vuelo indicado.
     */
    List<ReservaEntity> findReservasByVueloId(Integer vueloId);
}
