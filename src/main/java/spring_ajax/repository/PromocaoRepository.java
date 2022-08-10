package spring_ajax.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import spring_ajax.domain.Promocao;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    @Transactional(readOnly = false)
    @Modifying
    @Query("UPDATE Promocao p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void updateSomarLikes(@Param("id") Long id);

    @Query("SELECT p.likes FROM Promocao p WHERE p.id = :id")
    int findLikesById(@Param("id") Long id);

}
