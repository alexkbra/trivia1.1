package co.com.elpoli.repository;

import co.com.elpoli.domain.Pista;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PistaRepository extends JpaRepository<Pista, Long> {

}
