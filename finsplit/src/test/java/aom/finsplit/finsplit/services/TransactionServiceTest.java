package aom.finsplit.finsplit.services;

import aom.finsplit.finsplit.entities.TransactionLedger;
import aom.finsplit.finsplit.interfaces.TransactionLedgerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionLedgerRepository repository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<TransactionLedger> list = List.of(
            new TransactionLedger(1L, 2L, 100.0, true),
            new TransactionLedger(2L, 3L, 50.0, false)
        );
        when(repository.findAll()).thenReturn(list);

        List<TransactionLedger> result = transactionService.getAll();
        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testGetByIdFound() {
        TransactionLedger txn = new TransactionLedger(1L, 2L, 100.0, true);
        when(repository.findById("abc123")).thenReturn(Optional.of(txn));

        Optional<TransactionLedger> result = transactionService.getById("abc123");

        assertTrue(result.isPresent());
        assertEquals(100.0, result.get().getAmount());
        verify(repository).findById("abc123");
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById("xyz")).thenReturn(Optional.empty());

        Optional<TransactionLedger> result = transactionService.getById("xyz");

        assertFalse(result.isPresent());
        verify(repository).findById("xyz");
    }

    @Test
    void testCreateSuccess() {
        TransactionLedger txn = new TransactionLedger(1L, 2L, 100.0, true);
        when(repository.save(any())).thenReturn(txn);

        boolean created = transactionService.create(1L, 2L, 100.0, true);

        assertTrue(created);
        verify(repository).save(any());
    }

    @Test
    void testCreateFailure() {
        when(repository.save(any())).thenThrow(new RuntimeException("DB down"));

        boolean created = transactionService.create(1L, 2L, 100.0, true);

        assertFalse(created);
        verify(repository).save(any());
    }

    @Test
    void testDeleteSuccess() {
        doNothing().when(repository).deleteById("id123");

        transactionService.delete("id123");

        verify(repository).deleteById("id123");
    }

    @Test
    void testDeleteFailure() {
        doThrow(new RuntimeException("Delete failed")).when(repository).deleteById("id123");

        assertDoesNotThrow(() -> transactionService.delete("id123"));
        verify(repository).deleteById("id123");
    }
}
