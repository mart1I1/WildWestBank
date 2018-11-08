package com.wwbank.dao.account;

import com.wwbank.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {

    List findAllByClientId(Integer id_client);
    Optional<Account> findById(Integer id);
    void save(Account account);
    void update(Account account);

}
