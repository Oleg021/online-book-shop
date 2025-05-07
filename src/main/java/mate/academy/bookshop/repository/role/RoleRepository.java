package mate.academy.bookshop.repository.role;

import java.util.Optional;
import mate.academy.bookshop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>,
        JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(Role.RoleName name);
}
