package pro.ske.demo.services;

import org.springframework.stereotype.Service;
import pro.ske.demo.model.Book;
import pro.ske.demo.repositories.BookRepository;

import java.util.Collection;

@Service

public class BookService {

    //    private final HashMap<Long, Book> books = new HashMap<>();
//    private long lastId = 0;
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book  createBook (Book book) {
//        book.setId(++lastId);
//        books.put(lastId, book);
//        return book;
        return bookRepository.save(book);
    }

    public Book findBook(long id) {
//        return books.get(id);
        return bookRepository.findById(id).get();
    }

    public Book editBook(Book book) {
//        books.put(book.getId(), book);
//        return book;
        return bookRepository.save(book);
    }

    public void  deleteBook(long id) {
//        return books.remove(id);
        bookRepository.deleteById(id);

    }

    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // метод возвращает книгу по имени
    public Book findByNameContainingIgnoreCase(String name) {
        return bookRepository.findByNameContainingIgnoreCase(name);
    }
// метод поиск по автору
    public Collection<Book> findBookByAutorContainsIgnoreCase(String autor) {
        return bookRepository.findBookByAutorContainingIgnoreCase(autor);
    }
    // метод поиск по чамти названию
    public Collection<Book> findAllByNameContains(String part) {
        return bookRepository.findAllByNameContainsIgnoreCase(part);
    }
    public  Collection<Book> findBooksByNameAndAutorIgnoreCase(String name, String autor) {
        return bookRepository.findBooksByNameAndAutorIgnoreCase(name, autor);
    }
    public  Collection<Book> findBooksByNameOrAutorAndIdGreaterThanIgnoreCase(String name, String autor, Long id) {
        return bookRepository.findBooksByNameOrAutorAndIdGreaterThanIgnoreCase(name, autor, id);
    }
}
