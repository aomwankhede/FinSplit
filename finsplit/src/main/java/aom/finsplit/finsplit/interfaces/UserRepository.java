package aom.finsplit.finsplit.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import aom.finsplit.finsplit.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    User findByUserName(String username);

    User findByEmail(String email);
}
