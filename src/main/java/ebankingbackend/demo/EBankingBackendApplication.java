package ebankingbackend.demo;

import ebankingbackend.demo.dtos.BankAccountDTO;
import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.AccountOperationService;
import ebankingbackend.demo.services.BankAccountService;
import ebankingbackend.demo.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}


}
