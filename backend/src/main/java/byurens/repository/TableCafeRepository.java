package byurens.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.TableCafe;

@Repository
public interface TableCafeRepository extends JpaRepository<TableCafe, UUID> {
    Optional<TableCafe> findByCapacity(Integer capacity);
}
