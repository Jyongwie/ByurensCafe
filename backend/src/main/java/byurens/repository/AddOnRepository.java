package byurens.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import byurens.entities.AddOn;

@Repository
public interface AddOnRepository extends JpaRepository<AddOn, UUID> {
    Optional<AddOn> findByName(String name);
}
