package spring_ajax.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import spring_ajax.domain.Promocao;

import java.math.BigDecimal;
import java.util.List;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Promocao p WHERE p.site LIKE :site")
    Page<Promocao> findBySite(@Param("site") String site, Pageable pageRequest);

    @Transactional(readOnly = false)
    @Modifying
    @Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void updateSomarLikes(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query("SELECT p.likes FROM Promocao p WHERE p.id = :id")
    int findLikesById(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT p.site FROM Promocao p WHERE p.site LIKE %:site%")
    List<String> findSitesByTermo(@Param("site") String site);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Promocao p WHERE p.titulo like %:search% or " +
            "p.site like %:search% or " +
            "p.categoria.titulo like %:search%")
    Page<Promocao> findByTituloOrSiteOrCategoria(@Param("search") String search, Pageable pageRequest);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Promocao p WHERE p.preco = :preco")
    Page<Promocao> findByPreco(@Param("preco") BigDecimal preco, Pageable pageable);
}
