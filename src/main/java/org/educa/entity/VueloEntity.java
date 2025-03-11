package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueloEntity implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("codigo_vuelo")
    private String codigoVuelo;
    @SerializedName("origen_id")
    private Integer origenId;
    @SerializedName("destino_id")
    private Integer destinoId;
    @SerializedName("duracion")
    private Integer duracion;
    @SerializedName("estado")
    private String estado;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("coste")
    private BigDecimal coste;
}
