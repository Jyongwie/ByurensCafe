package byurens.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.AddOnGroup;

@Repository
public interface AddOnGroupRepository extends JpaRepository<AddOnGroup, UUID> {
    Optional<AddOnGroup> findByName(String name);
}
