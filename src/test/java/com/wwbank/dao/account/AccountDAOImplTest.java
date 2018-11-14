package com.wwbank.dao.account;

import com.wwbank.config.PersistenceConfig;
import com.wwbank.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("unit-test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {PersistenceConfig.class, AccountDAOImpl.class},
        loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountDAOImplTest {

    @Autowired
    AccountDAO accountDAO;

    private Account account = new Account(1,1, 99999.0);

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllByClientId() {
        assertEquals(Collections.singletonList(account), accountDAO.findAllByClientId(account.getIdClient()));
    }

    @Test
    void findById() {
        assertTrue(accountDAO.findById(account.getId()).isPresent());
    }

    @Test
    void findByIdNull() {
        assertFalse(accountDAO.findById(account.getId() + 1).isPresent());
    }

    @Test
    void save() {
        Account newAccount = new Account(2,1, 1111.1);
        assertFalse(accountDAO.findAllByClientId(newAccount.getIdClient()).contains(newAccount));
        accountDAO.save(newAccount);
        assertTrue(accountDAO.findAllByClientId(newAccount.getIdClient()).contains(newAccount));
    }

    @Test
    void update() {
        Double money = account.getMoney();

        assertTrue(accountDAO.findById(account.getId()).isPresent());
        assertEquals(money, accountDAO.findById(account.getId()).get().getMoney());

        money -= 1000;
        account.setMoney(money);
        accountDAO.update(account);

        assertTrue(accountDAO.findById(account.getId()).isPresent());
        assertEquals(money, accountDAO.findById(account.getId()).get().getMoney());
    }
}