package xyz.xisyz.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByProviderId(String providerId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.following WHERE u.id = :id")
    Optional<User> findByIdWithFollowing(@Param("id") UUID id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followers LEFT JOIN FETCH u.following WHERE u.id = :id")
    Optional<User> findByIdWithFollowersAndFollowing(@Param("id") UUID id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followers LEFT JOIN FETCH u.following WHERE u.username = :username")
    Optional<User> findByUsernameWithFollowersAndFollowing(@Param("username") String username);

    Page<User> findAllByActiveTrue(Pageable pageable);

    @Query("""
            SELECT u
            FROM User u
            WHERE (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :q, '%'))
                OR
                LOWER(u.displayName) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            AND u.active = true
            """
    )
    Page<User> findAllActiveUsersByUsernameOrDisplayName(Pageable pageable, @Param("q") String q);

    @Query("SELECT u FROM User u WHERE u.createdAt < :nowMinus7Days AND u.active = false")
    List<User> findUsersWithExpiredActivationTime(@Param("nowMinus7Days") Instant nowMinus7Days);

    @EntityGraph(attributePaths = {"followers", "following"})
    Page<User> findAllByIdIn(Pageable pageable, List<UUID> ids);

}