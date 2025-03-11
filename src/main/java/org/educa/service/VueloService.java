package org.educa.service;

import org.educa.dao.VueloDAO;
import org.educa.dao.VueloDAOImpl;
import org.educa.entity.ReservaEntity;
import org.educa.entity.VueloWithRelations;
import org.educa.wrappers.BeneficioVuelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class VueloService {
    private final VueloDAO vueloDAO = new VueloDAOImpl();
    private final ReservaService reservaService = new ReservaService();

    /**
     * Obtiene la información de los vuelos junto a la información de su aeropuerto de origen y aeropuerto de destino.
     * Después, calcula las ganancias totales, el beneficio, y el número de pasajeros de cada vuelo.
     *
     * @return una lista de objetos {@link BeneficioVuelo}.
     */
    public List<BeneficioVuelo> getBeneficioVuelo() {
        List<BeneficioVuelo> beneficioVuelos = new ArrayList<>();
        List<VueloWithRelations> vueloWithRelations = vueloDAO.getBeneficioVuelo();
        for (VueloWithRelations vuelo : vueloWithRelations) {
            List<ReservaEntity> reservasVuelo = reservaService.findReservasByVueloId(vuelo.getId());
            BeneficioVuelo beneficioVuelo = new BeneficioVuelo();
            beneficioVuelo.setCodigoVuelo(vuelo.getCodigoVuelo());
            beneficioVuelo.setOrigen(vuelo.getOrigen().getNombre());
            beneficioVuelo.setDestino(vuelo.getDestino().getNombre());
            beneficioVuelo.setCoste(vuelo.getCoste());
            Integer numPasajeros = 0;
            BigDecimal total = BigDecimal.ZERO;
            for (ReservaEntity reserva : reservasVuelo) {
                if (reserva.getEstado() != null && reserva.getPrecio() != null) {
                    if (reserva.getEstado().equals("Confirmada")) {
                        numPasajeros++;
                        total = total.add(reserva.getPrecio());
                    } else {
                        total = total.add(reserva.getPrecio().divide(BigDecimal.valueOf(2), RoundingMode.HALF_DOWN));
                    }
                }
            }
            beneficioVuelo.setNumPasajeros(numPasajeros);
            beneficioVuelo.setTotal(total);
            beneficioVuelo.setBeneficio(total.subtract(vuelo.getCoste()));

            beneficioVuelos.add(beneficioVuelo);
        }
        return beneficioVuelos;
    }
}
