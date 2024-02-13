package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {

    Optional<Directory> findByPath(String path);
}
