package co.com.elpoli.repository;

import co.com.elpoli.domain.Participar;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Participar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticiparRepository extends JpaRepository<Participar, Long> {

}
