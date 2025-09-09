package ebankingbackend.demo;

import ebankingbackend.demo.entities.CurrentAccount;
import ebankingbackend.demo.entities.Customer;
import ebankingbackend.demo.entities.SavingAccount;
import ebankingbackend.demo.enums.AccountStatus;
import ebankingbackend.demo.repositories.BankAccountRepository;
import ebankingbackend.demo.repositories.CurrentAccountRepository;
import ebankingbackend.demo.repositories.CustomerRepository;
import ebankingbackend.demo.repositories.SavingAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository) {
		return args -> {
			Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepository.save(customer);
			});

			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random() * 100);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setCurrency("DH");
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(5000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				currentAccount.setCurrency("DH");
				savingAccount.setBalance(Math.random() * 100);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(2.3);
				bankAccountRepository.save(savingAccount);
			});
		};
	}

}
