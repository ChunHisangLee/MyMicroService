package com.jack.accounts.service.impl;

import com.jack.accounts.constant.AccountsConstants;
import com.jack.accounts.dto.AccountsDto;
import com.jack.accounts.dto.CustomerDto;
import com.jack.accounts.entity.Accounts;
import com.jack.accounts.entity.Customer;
import com.jack.accounts.exception.CustomerAlreadyExistsException;
import com.jack.accounts.exception.ResourceNotFoundException;
import com.jack.accounts.mapper.AccountsMapper;
import com.jack.accounts.mapper.CustomerMapper;
import com.jack.accounts.repository.AccountsRepository;
import com.jack.accounts.repository.CustomerRepository;
import com.jack.accounts.service.AccountsService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {
  private CustomerRepository customerRepository;
  private AccountsRepository accountsRepository;

  @Override
  public void createAccount(CustomerDto customerDto) {
    Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
    Optional<Customer> optionalCustomer =
        customerRepository.findByMobileNumber(customer.getMobileNumber());
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already exists for mobile number " + customerDto.getMobileNumber());
    }

    Customer savedCustomer = customerRepository.save(customer);
    accountsRepository.save(createNewAccount(savedCustomer));
    }

  @Override
  public CustomerDto fetchAccount(String mobileNumber) {
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
    Accounts accounts =
        accountsRepository
            .findByCustomerId(customer.getCustomerId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "Accounts", "customerId", customer.getCustomerId().toString()));
    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
    return customerDto;
  }

  @Override
  public boolean updateAccount(CustomerDto customerDto) {
    boolean isUpdated = false;
    AccountsDto accountsDto = customerDto.getAccountsDto();
    if (accountsDto != null) {
      Accounts accounts =
          accountsRepository
              .findById(accountsDto.getAccountNumber())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Accounts", "accountNumber", accountsDto.getAccountNumber().toString()));
      AccountsMapper.mapToAccounts(accountsDto, accounts);
      accountsRepository.save(accounts);

      Long customerId = accounts.getCustomerId();
      Customer customer =
          customerRepository
              .findById(customerId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Customer", "CustomerID", customerId.toString()));
      CustomerMapper.mapToCustomer(customerDto, customer);
      customerRepository.save(customer);
      isUpdated = true;
    }

    return isUpdated;
  }

  @Override
  public boolean deleteAccount(String mobileNumber) {
    Customer customer =
        customerRepository
            .findByMobileNumber(mobileNumber)
            .orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
    accountsRepository.deleteByCustomerId(customer.getCustomerId());
    customerRepository.deleteById(customer.getCustomerId());
    return true;
  }

  private Accounts createNewAccount(Customer customer) {
    Accounts accounts = new Accounts();
    accounts.setCustomerId(customer.getCustomerId());
    long randomAccountNumber = (long) (Math.random() * 1000000000L);
    accounts.setAccountNumber(randomAccountNumber);
    accounts.setAccountType(AccountsConstants.SAVINGS);
    accounts.setBranchAddress(AccountsConstants.ADDRESS);
    return accounts;
  }
}
