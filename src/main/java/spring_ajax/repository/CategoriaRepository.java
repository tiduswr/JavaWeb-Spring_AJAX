package spring_ajax.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_ajax.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
