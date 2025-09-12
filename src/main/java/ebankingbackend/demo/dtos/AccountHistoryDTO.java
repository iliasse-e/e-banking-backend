package ebankingbackend.demo.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOList;
}
