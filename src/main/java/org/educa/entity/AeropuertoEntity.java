package org.educa.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AeropuertoEntity implements Serializable {
    @SerializedName("_id")
    private Integer id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("ciudad")
    private String ciudad;
    @SerializedName("pais")
    private String pais;
    @SerializedName("codigo_IATA")
    private String codigoIATA;
}
