package pro.ske.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.ske.demo.model.Book;
import pro.ske.demo.model.BookCover;
import pro.ske.demo.services.BookCoverService;
import pro.ske.demo.services.BookService;

import javax.persistence.Entity;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/books")
public class BooksController {


    @Autowired
    private BookService bookService;

    @Autowired
    private BookCoverService bookCoverService;

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

    //    2 метода - 1 превью загружает уменьшенную версию, 2 - ориганал в гет методах ниже
    @GetMapping(value = "{id}/cover/preview")
    public ResponseEntity<byte[]> downloacCover(@PathVariable Long id) {
        BookCover bookCover = bookCoverService.findBookCover(id);

//        работа с загаловками
        HttpHeaders headers = new HttpHeaders();
//        заголовок что за тип данных возрвщается - медиатайп
        headers.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
//        заголовок длины контента (сколько загружено, сколько всего и определить сколько осталось)
        headers.setContentLength(bookCover.getPreview().length);

//        указываем статус - окей, указываем заголовки о правильности данных и сами данные массивы байт
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bookCover.getPreview());
    }

    @GetMapping(value = "{id}/cover")
//    тперь 2ой метод
    public void downloadCover (@PathVariable Long id, HttpServletResponse response) throws IOException {
//        получаем инфу об обложке
        BookCover bookCover = bookCoverService.findBookCover(id);

//        получаем путь к файлу  и метод оф получаем путь в виделе обьекта Патх
        Path path = Path.of(bookCover.getFilePath());

//        так же обьявляем переменные на вход и выход
//                берем класс файлс вызываем метод стрим и забираем по одному байту
        try   (InputStream is = Files.newInputStream(path);
//               берем обьект респонс и вызываем пакет оаут оф стрим

               OutputStream os = response.getOutputStream();) {

//            покажет что все у нах хорошо
            response.setStatus(200);

//            заголовки по типу контента и длине контента
            response.setContentType(bookCover.getMediaType());
            response.setContentLength((int) bookCover.getFileSize());

//            вызываем метод трансферт ту на сверер - из жесткого диска и отправляем в браузер пользователя
            is.transferTo(os);
        }


    }


}
