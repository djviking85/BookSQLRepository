package pro.ske.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.ske.demo.model.Book;
import pro.ske.demo.services.BookCoverService;
import pro.ske.demo.services.BookService;

import javax.persistence.Entity;
import java.io.IOException;

@RestController
@RequestMapping("/books")
public class BooksController {


    @Autowired
    private BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("{id}")
    public Book getBookInfo(@PathVariable Long id) {
        return bookService.findBook(id);
    }

    @GetMapping
//    (required = false) параметр или может ыбть указан или нет
    public ResponseEntity findBooks(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String autor,
                                    @RequestParam(required = false) String part,
                                    @RequestParam(required = false) Long id) {

        // если имя, автор, частьслова не налл и имя, автьор или часть слова не состоящаая из пробелов и пустоты
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(bookService.findByNameContainingIgnoreCase(name));
        }
        if (autor != null && !autor.isBlank()) {
            return ResponseEntity.ok(bookService.findBookByAutorContainsIgnoreCase(autor));
        }
        if (part != null && !part.isBlank()) {
            return ResponseEntity.ok(bookService.findAllByNameContains(part));

        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(bookService.findBooksByNameAndAutorIgnoreCase(name, autor));
        }
        if (name != null && !name.isBlank() && id < 100 && id != null) {
            return ResponseEntity.ok(bookService.findBooksByNameOrAutorAndIdGreaterThanIgnoreCase(name, autor, id));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping()
    public Book createBook(Book book) {
        return bookService.createBook(book);
    }

    @PutMapping
    public ResponseEntity<Book> editBook(@RequestBody Book book) {
        Book foundBook = bookService.editBook(book);
        if (foundBook == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundBook);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

//    метод чтоб загружать файлы
    @PostMapping(value = "{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upLoadCover(@PathVariable Long id, @RequestParam MultipartFile cover) throws IOException {
        if (cover.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("File is to BIG");
        }

        bookCoverService.uploadCover(id, cover);
        return ResponseEntity.ok().build();
    }
}
