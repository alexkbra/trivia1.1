package co.com.elpoli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoPregunta.
 */
@Entity
@Table(name = "tipo_pregunta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tipopregunta")
public class TipoPregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "constant", length = 100, nullable = false)
    private String constant;

    @NotNull
    @Size(min = 10, max = 100)
    @Column(name = "descripcion", length = 100, nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "tipoPregunta")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pregunta> preguntas = new HashSet<>();

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

    public TipoPregunta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConstant() {
        return constant;
    }

    public TipoPregunta constant(String constant) {
        this.constant = constant;
        return this;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoPregunta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Pregunta> getPreguntas() {
        return preguntas;
    }

    public TipoPregunta preguntas(Set<Pregunta> preguntas) {
        this.preguntas = preguntas;
        return this;
    }

    public TipoPregunta addPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
        pregunta.setTipoPregunta(this);
        return this;
    }

    public TipoPregunta removePregunta(Pregunta pregunta) {
        this.preguntas.remove(pregunta);
        pregunta.setTipoPregunta(null);
        return this;
    }

    public void setPreguntas(Set<Pregunta> preguntas) {
        this.preguntas = preguntas;
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
        TipoPregunta tipoPregunta = (TipoPregunta) o;
        if (tipoPregunta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoPregunta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoPregunta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", constant='" + getConstant() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
