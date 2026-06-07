package com.portfolio.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class PortfolioBackendApplication {

	private static final Logger log = LoggerFactory.getLogger(PortfolioBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PortfolioBackendApplication.class, args);
	}

	// Startup logger to help debug DB connectivity in environments like Render.
	@Bean
	public CommandLineRunner logDatabaseInfo(Environment env) {
		return args -> {
			String host = env.getProperty("DB_HOST", "localhost");
			String port = env.getProperty("DB_PORT", "3306");
			String name = env.getProperty("DB_NAME", "portfolio_app");
			String user = env.getProperty("DB_USER", "root");
			String useSsl = env.getProperty("DB_USE_SSL", "false");
			String requireSsl = env.getProperty("DB_REQUIRE_SSL", "false");
			String allowPublicKeyRetrieval = env.getProperty("DB_ALLOW_PUBLIC_KEY_RETRIEVAL", "true");

			log.info("Resolved database connection (will NOT log password): host={} port={} name={} user={} useSSL={} requireSSL={} allowPublicKeyRetrieval={}",
					host, port, name, user, useSsl, requireSsl, allowPublicKeyRetrieval);
		};
	}

}