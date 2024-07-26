package pwav2.com.pw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwav2.com.pw.domain.Sandalia;

import java.util.List;

@Repository
public interface SandaliaRepository extends JpaRepository<Sandalia, String> {
    List<Sandalia> findByIsDeletedIsFalse();
}