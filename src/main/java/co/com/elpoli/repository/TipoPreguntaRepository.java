package co.com.elpoli.repository;

import co.com.elpoli.domain.TipoPregunta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TipoPregunta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoPreguntaRepository extends JpaRepository<TipoPregunta, Long> {

}
