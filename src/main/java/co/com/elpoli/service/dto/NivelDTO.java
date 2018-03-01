package co.com.elpoli.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Nivel entity.
 */
public class NivelDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String nombre;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFinal;

    @NotNull
    @Min(value = 1)
    private Integer numeroPreguntas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Integer getNumeroPreguntas() {
        return numeroPreguntas;
    }

    public void setNumeroPreguntas(Integer numeroPreguntas) {
        this.numeroPreguntas = numeroPreguntas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NivelDTO nivelDTO = (NivelDTO) o;
        if(nivelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nivelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NivelDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFinal='" + getFechaFinal() + "'" +
            ", numeroPreguntas='" + getNumeroPreguntas() + "'" +
            "}";
    }
}
