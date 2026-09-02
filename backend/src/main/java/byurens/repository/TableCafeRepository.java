package byurens.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.TableCafe;

@Repository
public interface TableCafeRepository extends JpaRepository<TableCafe, UUID> {
    List<TableCafe> findByCapacity(Integer capacity);
    boolean existsByTableIdentifier(String identifier);
}
