package co.com.elpoli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Nivel.
 */
@Entity
@Table(name = "nivel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "nivel")
public class Nivel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_final", nullable = false)
    private LocalDate fechaFinal;

    @NotNull
    @Min(value = 1)
    @Column(name = "numero_preguntas", nullable = false)
    private Integer numeroPreguntas;

    @ManyToMany(mappedBy = "nivels")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Cuestionario> cuestionarios = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Nivel nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Nivel fechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public Nivel fechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
        return this;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Integer getNumeroPreguntas() {
        return numeroPreguntas;
    }

    public Nivel numeroPreguntas(Integer numeroPreguntas) {
        this.numeroPreguntas = numeroPreguntas;
        return this;
    }

    public void setNumeroPreguntas(Integer numeroPreguntas) {
        this.numeroPreguntas = numeroPreguntas;
    }

    public Set<Cuestionario> getCuestionarios() {
        return cuestionarios;
    }

    public Nivel cuestionarios(Set<Cuestionario> cuestionarios) {
        this.cuestionarios = cuestionarios;
        return this;
    }

    public Nivel addCuestionario(Cuestionario cuestionario) {
        this.cuestionarios.add(cuestionario);
        cuestionario.getNivels().add(this);
        return this;
    }

    public Nivel removeCuestionario(Cuestionario cuestionario) {
        this.cuestionarios.remove(cuestionario);
        cuestionario.getNivels().remove(this);
        return this;
    }

    public void setCuestionarios(Set<Cuestionario> cuestionarios) {
        this.cuestionarios = cuestionarios;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Nivel nivel = (Nivel) o;
        if (nivel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nivel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Nivel{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFinal='" + getFechaFinal() + "'" +
            ", numeroPreguntas='" + getNumeroPreguntas() + "'" +
            "}";
    }
}
