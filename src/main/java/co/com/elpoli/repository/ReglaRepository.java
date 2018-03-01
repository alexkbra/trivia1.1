package co.com.elpoli.repository;

import co.com.elpoli.domain.Regla;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Regla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReglaRepository extends JpaRepository<Regla, Long> {

}
