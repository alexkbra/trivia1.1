package co.com.elpoli.repository;

import co.com.elpoli.domain.Publicidad;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Publicidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicidadRepository extends JpaRepository<Publicidad, Long> {

}
