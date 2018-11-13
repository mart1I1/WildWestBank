package com.wwbank.dao.transaction;

import com.wwbank.config.PersistenceConfigTest;
import com.wwbank.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {PersistenceConfigTest.class},
        loader = AnnotationConfigContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionDAOImplTest {

    @Autowired
    private TransactionDAO transactionDAO;

    private Double money = 1000.0;
    private Transaction transaction1 = new Transaction(1, 2, new Date(), money);
    private Transaction transaction2 = new Transaction(1, 2, new Date(), 0.5 * money);

    @BeforeEach
    void setUp() {
        transactionDAO.save(transaction1);
        transactionDAO.save(transaction2);
    }

    @Test
    void findAll() {
        assertEquals(Arrays.asList(transaction1, transaction2), transactionDAO.findAll());
    }

    @Test
    void findAllBetweenDates() {
        Calendar calendarMiddle = new GregorianCalendar(1995, 11, 29);
        Calendar calendarLeft = new GregorianCalendar(1995, 10, 28);
        Calendar calendarRight = new GregorianCalendar(1996, 10, 28);
        Transaction transactionFromPast = new Transaction(1, 2, calendarMiddle.getTime(), money);

        assertEquals(Collections.emptyList(), transactionDAO.findAllBetweenDates(calendarLeft.getTime(), calendarRight.getTime()));
        transactionDAO.save(transactionFromPast);
        assertEquals(Collections.singletonList(transactionFromPast), transactionDAO.findAllBetweenDates(calendarLeft.getTime(), calendarRight.getTime()));

    }

    @Test
    void findById() {
        assertTrue(transactionDAO.findById(transaction1.getId()).isPresent());
        assertEquals(transaction1, transactionDAO.findById(transaction1.getId()).get());
    }

    @Test
    void findByIdNotFound() {
        assertFalse(transactionDAO.findById(transaction1.getId() + 100).isPresent());
    }

    @Test
    void save() {
        Transaction newTransaction = new Transaction(1, 1, new Date(), money);
        Integer predictedId = transaction2.getId() + 1;
        assertFalse(transactionDAO.findById(predictedId).isPresent());
        transactionDAO.save(newTransaction);
        assertEquals(newTransaction.getId(), predictedId);
        assertTrue(transactionDAO.findById(newTransaction.getId()).isPresent());
    }

    @Test
    void findAllBySenderId() {
        assertAll(
                () -> assertEquals(Arrays.asList(transaction1, transaction2), transactionDAO.findAllBySenderId(transaction1.getIdAccSender())),
                () -> assertEquals(Collections.emptyList(), transactionDAO.findAllBySenderId(transaction1.getIdAccSender() + 1)));
    }

    @Test
    void findAllByReceiverId() {
        assertAll(
                () -> assertEquals(Arrays.asList(transaction1, transaction2), transactionDAO.findAllByReceiverId(transaction1.getIdAccReceiver())),
                () -> assertEquals(Collections.emptyList(), transactionDAO.findAllByReceiverId(transaction1.getIdAccReceiver() + 1)));
    }
}