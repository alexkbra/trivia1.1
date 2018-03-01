package co.com.elpoli.repository;

import co.com.elpoli.domain.Expedicion;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Expedicion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpedicionRepository extends JpaRepository<Expedicion, Long> {
    @Query("select distinct expedicion from Expedicion expedicion left join fetch expedicion.publicidads")
    List<Expedicion> findAllWithEagerRelationships();

    @Query("select expedicion from Expedicion expedicion left join fetch expedicion.publicidads where expedicion.id =:id")
    Expedicion findOneWithEagerRelationships(@Param("id") Long id);

}
