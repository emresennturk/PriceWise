package Project.PriceWise.repositories;

import Project.PriceWise.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirebaseUid(String firebaseUid);
}
