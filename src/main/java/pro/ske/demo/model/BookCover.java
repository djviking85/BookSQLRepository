package pro.ske.demo.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity

public class BookCover {
    @Id
    @GeneratedValue
    private long id;


//    файл хранится на жестком диске с путь файла и сайз файла

    private String filePath;
    private long fileSize;
    private String mediaType;


//    а это уменьшенная копия которая будет храниться в базе данныйх
    @Lob
    private byte[] preview;

//    ван ту ван - у только одной книги только одна обложка
    @OneToOne
    private Book book;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCover bookCover = (BookCover) o;
        return id == bookCover.id && fileSize == bookCover.fileSize && Objects.equals(filePath, bookCover.filePath) && Objects.equals(mediaType, bookCover.mediaType) && Arrays.equals(preview, bookCover.preview) && Objects.equals(book, bookCover.book);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, fileSize, mediaType, book);
        result = 31 * result + Arrays.hashCode(preview);
        return result;
    }
}
