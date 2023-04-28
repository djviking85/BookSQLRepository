package pro.ske.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.ske.demo.model.BookCover;

import java.util.Optional;

public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    Optional<BookCover> findBookById(Long bookId);
}
