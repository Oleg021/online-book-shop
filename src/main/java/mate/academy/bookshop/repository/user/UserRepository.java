package mate.academy.bookshop.repository.user;

import java.util.Optional;
import mate.academy.bookshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    @Query(value = "SELECT id, email, first_name, last_name, shipping_address "
            + "FROM users WHERE UPPER(email) LIKE UPPER(:email)",
            nativeQuery = true)
    Optional<User> findUserByEmail(String email);
}
