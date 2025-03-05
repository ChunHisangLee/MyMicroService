package com.jack.accounts.service;

import com.jack.accounts.dto.CustomerDto;

public interface AccountsService {
  public void createAccount(CustomerDto customerDto);

  CustomerDto fetchAccount(String mobileNumber);

  boolean updateAccount(CustomerDto customerDto);

  boolean deleteAccount(String mobileNumber);
}
