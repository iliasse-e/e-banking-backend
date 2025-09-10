package ebankingbackend.demo;

import ebankingbackend.demo.dtos.CustomerDTO;
import ebankingbackend.demo.entities.*;
import ebankingbackend.demo.exceptions.BalanceNotSufficientException;
import ebankingbackend.demo.exceptions.BankAccountNotFoundException;
import ebankingbackend.demo.exceptions.CustomerNotFoundException;
import ebankingbackend.demo.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});

			bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.floor(Math.random() * 10000), 5000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.floor(Math.random() * 1000), 5.5, customer.getId());

					List<BankAccount> bankAccounts = bankAccountService.bankAccountList();

					bankAccounts
							.forEach(bankAccount -> {
								for (int i = 0; i < 5; i++) {
									try {
										bankAccountService.credit(bankAccount.getId(), Math.floor(Math.random() * 1000), "Credit");
										bankAccountService.debit(bankAccount.getId(), Math.floor(Math.random() * 100), "Debit");

									} catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
										throw new RuntimeException(e);
									}
								}
							});

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

			bankAccountService.bankAccountList().forEach(bankAccount -> {
			});

		};
	}

}
