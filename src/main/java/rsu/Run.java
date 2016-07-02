package rsu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Run {

	public static String MODE = "TRAIN";

	public static void main(String[] args) {
		if (args.length > 0) {
			MODE = args[0];
		}
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(Run.class, args);
	}

}
