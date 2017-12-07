package com.ash.sample.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.NoSuchFileException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
/**
 * @author Ash Izadi
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = FileReaderServiceImpl.class)
public class FileReaderServiceImplTest {

	/**
	 *To check the Service class, we need to have an instance of Service class created and available as
	 * a @Bean so that we can @Autowire it in our test class. This configuration is achieved 
	 * by using the @TestConfiguration annotation.
	 */
	@TestConfiguration
	static class DateServiceImplTestContextConfiguration{
		@Bean
		public FileReaderService fileReaderService() {
			return new FileReaderServiceImpl();
		}
	}
	@Autowired
	private FileReaderService fileReaderService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Optional<String> mockResult = Optional.of("28 10 1981 , 20 11 1990");
	
	 
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	 
	
	@Test(expected = RuntimeException.class)
	public void given_file_to_make_sure_to_read() throws Exception{
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
		Optional<String> text = fileReaderService.readTextFile(file.getName());			  
	}
	
}








