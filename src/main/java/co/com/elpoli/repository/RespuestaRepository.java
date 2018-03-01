package co.com.elpoli.repository;

import co.com.elpoli.domain.Respuesta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Respuesta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

}
