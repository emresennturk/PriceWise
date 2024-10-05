package Project.PriceWise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceWiseApplication {


	public static void main(String[] args) {
		SpringApplication.run(PriceWiseApplication.class, args);
	}

}
