package pro.ske.demo.model;

import javax.persistence.*;
import java.util.Objects;
@Entity

public class Book {
    @Id
    @GeneratedValue
    private long id;
    private String autor;
    private String name;
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && Objects.equals(autor, book.autor) && Objects.equals(name, book.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, autor, name);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", autor='" + autor + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
