package pro.ske.demo.services;

import org.apache.tomcat.jni.Buffer;
import org.hibernate.result.Output;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.ske.demo.model.Book;
import pro.ske.demo.model.BookCover;
import pro.ske.demo.repositories.BookCoverRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class BookCoverService {
    @Value("covers")
    private String coverDir;
//    название папки в которой будут храниться обложки

    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    public BookCoverService(BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }

    public void uploadCover (Long bookId, MultipartFile file) throws IOException {
        Book book = bookService.findBook(bookId);

        Path filePath = Path.of(coverDir, bookId + "." + getExtension(file.getOriginalFilename()));

//        метод который проверит папку и если ее нет то он ее создаст
        Files.createDirectories(filePath.getParent());

//        метод который удаляет файл если он существует (если к примеру более новая версия файла)
        Files.deleteIfExists(filePath);

//        мы открываем поток данныйх и потом его закрываем
        try (
//            ОТКУДА ЧЕРПАТЬ   открываем поток и читаем из нашего файла, который был загружен пользователем
                InputStream is = file.getInputStream();
//         КУДА ЧЕРПАТЬ        создаем пустой файл в который будем записывать и передаем пути и в этот пустой файл будем записывать
             OutputStream os  = Files.newOutputStream(filePath, CREATE_NEW);
//          СКОЛЬКО ИНФОРМАЦИИ БРАТЬ ЗА РАЗ      и чтоб не черпать чайной ложкой эти файлы - мы создаем буферезированные потоки и сколько байт за раз (1024) мы будем забирать
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
//          СКОЛЬКО ИНФОРМАЦИИ ЗАПИСЫВАТЬ ЗА РАЗ      точно такую же операцияю делаем запись по буферу 1024
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
             ) {
//            передача информации из вВХОДНОГО потока в ВЫХОДНОЙ поток bis -> bos
            bis.transferTo(bos);
        }
//        если загружэаем книгу - сначала изем по айди если нет создаем
        BookCover bookCover = findBookCover(bookId);
//        ДАННЫЕ ДЛЯ СОХРАНЕНИЯ НА ЛОКАЛЬНЫЙ ДИСК
//        указываем книгу где загружаем обложку
        bookCover.setBook(book);
//        указываем путь к файлу который созранили на диске
        bookCover.setFilePath(filePath.toString());
//        указываем его размер
        bookCover.setFileSize(file.getSize());
//        указываем тип контент
        bookCover.setMediaType(file.getContentType());
//        --------------------------------------

//         в этом методе иметь копию уменьшенную в размере до 100 пикселей которая будет храниться в базе данных
//        и мы получаем массив байт и записываем в переменную ПРЕВЬЮ и записывает в базу данных

        bookCover.setPreview(generateImagePreview(filePath));

        bookCoverRepository.save(bookCover);
    }

    public BookCover findBookCover(Long bookId) {
        return bookCoverRepository.findBookById(bookId).orElse(new BookCover());
    }
    public byte [] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }


    }


//    метод который находит последнюю точку в нашей строке и возращает все что после точки
private String getExtension (String fileName) {
    return fileName.substring(fileName.lastIndexOf(".") + 1);
}
}
