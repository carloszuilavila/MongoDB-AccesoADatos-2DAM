package org.educa.service;

import org.educa.dao.ReservaDAO;
import org.educa.dao.ReservaDAOImpl;
import org.educa.entity.ReservaEntity;
import org.educa.entity.ReservaWithRelations;
import org.educa.wrappers.InfoPasajero;

import java.math.BigDecimal;
import java.util.List;

public class ReservaService {
    private final ReservaDAO reservaDAO = new ReservaDAOImpl();

    /**
     * Busca reservas asociadas a un vuelo específico.
     *
     * @param vueloId el identificador del vuelo.
     * @return una lista de objetos {@link ReservaEntity} relacionados con el vuelo indicado.
     */
    public List<ReservaEntity> findReservasByVueloId(Integer vueloId) {
        return reservaDAO.findReservasByVueloId(vueloId);
    }

    /**
     * Busca los vuelos asociados a un número de pasaporte específico.
     *
     * @param pasaporte el número de pasaporte del pasajero.
     * @return un objeto {@link InfoPasajero} con la información del pasajero y sus vuelos.
     */
    public InfoPasajero findReservasByPasaporte(String pasaporte) {
        return reservaDAO.findReservasByPasaporte(pasaporte);
    }

    /**
     * Busca las reservas cuyo precio sea igual o superior al especificado.
     *
     * @param cantidad el precio a partir del cuál se filtran las reservas.
     * @return una lista de objetos {@link ReservaWithRelations} añadiendo información relevante de las reservas.
     */
    public List<ReservaWithRelations> findReservasByCantidad(BigDecimal cantidad) {
        return reservaDAO.findReservasByCantidad(cantidad);
    }

    /**
     * Guarda una nueva reserva en la base de datos.
     *
     * @param reserva la entidad {@link ReservaEntity} a guardar.
     * @return el identificador generado para la nueva reserva.
     */
    public Integer save(ReservaEntity reserva) {
        return reservaDAO.save(reserva);
    }

    /**
     * Busca una reserva por su identificador único.
     *
     * @param id el identificador de la reserva.
     * @return la entidad {@link ReservaEntity} encontrada.
     */
    public ReservaEntity findById(int id) {
        return reservaDAO.findById(id);
    }

    /**
     * Actualiza los datos de una reserva existente.
     *
     * @param reservaToUpdate la entidad {@link ReservaEntity} con los datos actualizados.
     * @return el número de filas afectadas por la actualización.
     */
    public Long update(ReservaEntity reservaToUpdate) {
        return reservaDAO.update(reservaToUpdate);
    }

    /**
     * Elimina una reserva por su identificador.
     *
     * @param id el identificador de la reserva a eliminar.
     * @return el número de filas afectadas por la eliminación.
     */
    public Long delete(int id) {
        return reservaDAO.delete(id);
    }

    /**
     * Obtiene una lista de todas las reservas.
     *
     * @return una lista de objetos {@link ReservaEntity}.
     */
    public List<ReservaEntity> findAll() {
        return reservaDAO.findAll();
    }
}
