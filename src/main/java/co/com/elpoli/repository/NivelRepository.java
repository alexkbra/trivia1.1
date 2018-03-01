package co.com.elpoli.repository;

import co.com.elpoli.domain.Nivel;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Nivel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

}
