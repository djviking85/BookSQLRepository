package pro.ske.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pro.ske.demo.controllers.BooksController;
import pro.ske.demo.model.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class DemoApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private BooksController booksController;

	@Autowired
	private TestRestTemplate restTemplate;
//	тестовый шаблон запроса (готовый класс для теста)

//тест который проверяем загрузились ли все наши бины, контексты,
//	контроллеры репозитории строк и пр (ИЛИ генерируется ошибка)
	@Test
	public void contextLoad() throws Exception {
		Assertions.assertThat(booksController).isNotNull();
	}

//	проверяем этим тестом что у нас на меине появляется веб и з воркинг и оно запустилось
//	и возвращает все то что нужно

@Test
	public void testDefaultMessage() throws  Exception {
	Assertions
			.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
			.isEqualTo("Web is working!");
}

//тест на образщнеие к методу /books - выполнить метод ГЕТТ и посмотреть что нам пришло, String.class- ожидание строчки и не пусто (isNotEmpty) там
// или isNotNull  - чтоб там не было нала
@Test
	public void testGetBooks() throws  Exception {
	Assertions
			.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/books", String.class))
			.isNotNull();
}

//тест на создание книги, и что она у нас запостилась и без ошибок - и не стала наллом
	@Test
	public void testPostBooks() throws  Exception {
		Book book = new Book();
		book.setAutor("Putin");
		book.setName("Attack!");
		Assertions
				.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/books", book, String.class))
				.isNotNull();
		Assertions
				.assertThat(this.restTemplate.delete("http://localhost:" + port + "/books/" + id, book, String.class);)
				.isNotNull();

	}



}
