package co.com.elpoli.repository;

import co.com.elpoli.domain.Empresas;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Empresas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresasRepository extends JpaRepository<Empresas, Long> {

}
