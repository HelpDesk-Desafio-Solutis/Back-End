package br.com.ticket.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
		"br.com.ticket.microservice.infra.persistence.entity",
		"br.com.user.microservice.infra.persistence.entity"
})
@EnableJpaRepositories(basePackages = {
		"br.com.ticket.microservice.infra.persistence.repo",
		"br.com.user.microservice.infra.persistence.repo"
})
public class TicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketApplication.class, args);
	}

}
