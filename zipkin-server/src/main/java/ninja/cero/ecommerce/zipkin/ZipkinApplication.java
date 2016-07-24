package ninja.cero.ecommerce.zipkin;

import org.elasticsearch.Version;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import zipkin.server.EnableZipkinServer;
import zipkin.storage.StorageComponent;
import zipkin.storage.elasticsearch.ElasticsearchStorage;

@SpringBootApplication
@EnableZipkinServer
public class ZipkinApplication {
	public static void main(String[] args) {
		System.out.println(Version.CURRENT);
		SpringApplication.run(ZipkinApplication.class, args);
	}

	@Bean
	StorageComponent storage() {
		return ElasticsearchStorage.builder().build();
	}

}
