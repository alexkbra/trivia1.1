package co.com.elpoli.repository;

import co.com.elpoli.domain.Galerias;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Galerias entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GaleriasRepository extends JpaRepository<Galerias, Long> {

}
