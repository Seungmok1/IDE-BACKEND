package everyide.webide.fileSystem;

import everyide.webide.fileSystem.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByPath(String path);
}
