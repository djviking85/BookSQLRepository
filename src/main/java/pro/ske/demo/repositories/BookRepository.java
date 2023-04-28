package pro.ske.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.ske.demo.model.Book;

import java.util.Collection;

public interface BookRepository extends JpaRepository<Book, Long> {
    // фильтрация по названию книги
    Book findByNameContainingIgnoreCase(String name);

    // поиск книг по автору
    Collection<Book> findBookByAutorContainingIgnoreCase(String autor);

    // поиск по части названию
    Collection<Book> findAllByNameContainsIgnoreCase(String part);

    // ъотим получить все книги который содержит автора и часть книги

    Collection<Book> findBooksByNameAndAutorIgnoreCase(String name, String autor);
    // ъотим получить все книги который содержит автора ИЛИ часть книги по айди который содержит не более значение
    Collection<Book> findBooksByNameOrAutorAndIdGreaterThanIgnoreCase(String name, String autor, Long id);
}
