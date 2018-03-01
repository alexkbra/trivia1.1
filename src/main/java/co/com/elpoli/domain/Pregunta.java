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
 * A Pregunta.
 */
@Entity
@Table(name = "pregunta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pregunta")
public class Pregunta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10, max = 200)
    @Column(name = "corta_descripcion", length = 200, nullable = false)
    private String cortaDescripcion;

    @NotNull
    @Size(min = 10, max = 500)
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @OneToMany(mappedBy = "pregunta")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Respuesta> respuestas = new HashSet<>();

    @OneToMany(mappedBy = "pregunta")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Pista> pistas = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Cuestionario cuestionario;

    @ManyToOne(optional = false)
    @NotNull
    private TipoPregunta tipoPregunta;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCortaDescripcion() {
        return cortaDescripcion;
    }

    public Pregunta cortaDescripcion(String cortaDescripcion) {
        this.cortaDescripcion = cortaDescripcion;
        return this;
    }

    public void setCortaDescripcion(String cortaDescripcion) {
        this.cortaDescripcion = cortaDescripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Pregunta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Respuesta> getRespuestas() {
        return respuestas;
    }

    public Pregunta respuestas(Set<Respuesta> respuestas) {
        this.respuestas = respuestas;
        return this;
    }

    public Pregunta addRespuesta(Respuesta respuesta) {
        this.respuestas.add(respuesta);
        respuesta.setPregunta(this);
        return this;
    }

    public Pregunta removeRespuesta(Respuesta respuesta) {
        this.respuestas.remove(respuesta);
        respuesta.setPregunta(null);
        return this;
    }

    public void setRespuestas(Set<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public Set<Pista> getPistas() {
        return pistas;
    }

    public Pregunta pistas(Set<Pista> pistas) {
        this.pistas = pistas;
        return this;
    }

    public Pregunta addPista(Pista pista) {
        this.pistas.add(pista);
        pista.setPregunta(this);
        return this;
    }

    public Pregunta removePista(Pista pista) {
        this.pistas.remove(pista);
        pista.setPregunta(null);
        return this;
    }

    public void setPistas(Set<Pista> pistas) {
        this.pistas = pistas;
    }

    public Cuestionario getCuestionario() {
        return cuestionario;
    }

    public Pregunta cuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
        return this;
    }

    public void setCuestionario(Cuestionario cuestionario) {
        this.cuestionario = cuestionario;
    }

    public TipoPregunta getTipoPregunta() {
        return tipoPregunta;
    }

    public Pregunta tipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
        return this;
    }

    public void setTipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
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
        Pregunta pregunta = (Pregunta) o;
        if (pregunta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pregunta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pregunta{" +
            "id=" + getId() +
            ", cortaDescripcion='" + getCortaDescripcion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
