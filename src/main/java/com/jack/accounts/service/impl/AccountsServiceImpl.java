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
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {
  private final CustomerRepository customerRepository;
  private final AccountsRepository accountsRepository;

  @Override
  @Transactional
  public void createAccount(CustomerDto customerDto) {
    validateCustomerDoesNotExist(customerDto.getMobileNumber());

    Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
    Customer savedCustomer = customerRepository.save(customer);
    accountsRepository.save(createNewAccount(savedCustomer));
  }

  @Override
  public CustomerDto fetchAccount(String mobileNumber) {
    Customer customer = findCustomerByMobileNumber(mobileNumber);
    Accounts accounts = findAccountsByCustomerId(customer.getCustomerId());

    CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
    customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
    return customerDto;
  }

  @Override
  @Transactional
  public boolean updateAccount(CustomerDto customerDto) {
    AccountsDto accountsDto = customerDto.getAccountsDto();
    if (accountsDto == null) {
      return false;
    }

    Accounts accounts = findAccountsByAccountNumber(accountsDto.getAccountNumber());
    AccountsMapper.mapToAccounts(accountsDto, accounts);
    accountsRepository.save(accounts);

    Customer customer = findCustomerById(accounts.getCustomerId());
    CustomerMapper.mapToCustomer(customerDto, customer);
    customerRepository.save(customer);
    return true;
  }

  @Override
  @Transactional
  public boolean deleteAccount(String mobileNumber) {
    Customer customer = findCustomerByMobileNumber(mobileNumber);
    accountsRepository.deleteByCustomerId(customer.getCustomerId());
    customerRepository.deleteById(customer.getCustomerId());
    return true;
  }

  private Accounts createNewAccount(Customer customer) {
    Accounts accounts = new Accounts();
    accounts.setCustomerId(customer.getCustomerId());
    accounts.setAccountNumber(generateRandomAccountNumber());
    accounts.setAccountType(AccountsConstants.SAVINGS);
    accounts.setBranchAddress(AccountsConstants.ADDRESS);
    return accounts;
  }

  private long generateRandomAccountNumber() {
    return 1000000000L + new Random().nextInt(900000000);
  }

  private void validateCustomerDoesNotExist(String mobileNumber) {
    Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
    if (optionalCustomer.isPresent()) {
      throw new CustomerAlreadyExistsException(
          "Customer already exists for mobile number " + mobileNumber);
    }
  }

  private Customer findCustomerByMobileNumber(String mobileNumber) {
    return customerRepository
        .findByMobileNumber(mobileNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
  }

  private Accounts findAccountsByCustomerId(Long customerId) {
    return accountsRepository
        .findByCustomerId(customerId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Accounts", "customerId", customerId.toString()));
  }

  private Accounts findAccountsByAccountNumber(Long accountNumber) {
    return accountsRepository
        .findById(accountNumber)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Accounts", "accountNumber", accountNumber.toString()));
  }

  private Customer findCustomerById(Long customerId) {
    return customerRepository
        .findById(customerId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
  }
}
