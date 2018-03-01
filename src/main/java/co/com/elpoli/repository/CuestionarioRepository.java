package co.com.elpoli.repository;

import co.com.elpoli.domain.Cuestionario;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Cuestionario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Long> {
    @Query("select distinct cuestionario from Cuestionario cuestionario left join fetch cuestionario.nivels")
    List<Cuestionario> findAllWithEagerRelationships();

    @Query("select cuestionario from Cuestionario cuestionario left join fetch cuestionario.nivels where cuestionario.id =:id")
    Cuestionario findOneWithEagerRelationships(@Param("id") Long id);

}
