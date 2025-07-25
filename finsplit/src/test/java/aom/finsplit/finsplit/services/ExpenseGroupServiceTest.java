package aom.finsplit.finsplit.services;

import aom.finsplit.finsplit.DTO.DTO1;
import aom.finsplit.finsplit.entities.DebtGraph;
import aom.finsplit.finsplit.entities.ExpenseGroup;
import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.interfaces.ExpenseGroupRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseGroupServiceTest {

    @InjectMocks
    private ExpenseGroupService expenseGroupService;

    @Mock
    private ExpenseGroupRepository expenseGroupRepository;

    @Mock
    private UserService userService;

    @Mock
    private DebtGraph debtGraph;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExpenseGroup_Success() {
        long creatorId = 1L;
        List<Long> others = List.of(2L, 3L);
        List<Double> amounts = List.of(100.0, 200.0);
        int totalAmount = 300;

        User mockUser = new User();
        mockUser.setId(creatorId);
        when(userService.getUserFromID(creatorId)).thenReturn(Optional.of(mockUser));

        ExpenseGroup savedGroup = new ExpenseGroup(mockUser,
                List.of(new DTO1(2L, 100.0, false), new DTO1(3L, 200.0, false)),
                totalAmount);

        when(expenseGroupRepository.save(any(ExpenseGroup.class))).thenReturn(savedGroup);

        Optional<ExpenseGroup> result = expenseGroupService.create(creatorId, others, amounts, totalAmount);

        assertTrue(result.isPresent());
        verify(debtGraph, times(2)).addDebt(anyLong(), anyLong(), anyDouble());
        verify(expenseGroupRepository, times(1)).save(any(ExpenseGroup.class));
    }

    @Test
    void testCreateExpenseGroup_UserNotFound() {
        long creatorId = 999L;
        when(userService.getUserFromID(creatorId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> expenseGroupService.create(creatorId, List.of(), List.of(), 0));
    }

    @Test
    void testDeleteExpenseGroup_Success() {
        long creatorId = 1L;
        String groupId = "grp123";

        ExpenseGroup group = mock(ExpenseGroup.class);
        User testUser = new User();
        testUser.setId(creatorId);
        when(group.getCreator()).thenReturn(testUser);
        when(group.getOthers()).thenReturn(List.of(new DTO1(2L, 100.0, false)));

        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        boolean result = expenseGroupService.delete(groupId, creatorId);
        assertTrue(result);
        verify(debtGraph, times(1)).settleDebt(creatorId, 2L, 100.0);
        verify(expenseGroupRepository).deleteById(groupId);
    }

    @Test
    void testDeleteExpenseGroup_UnauthorizedUser() {
        long creatorId = 1L;
        long wrongUserId = 2L;
        String groupId = "grp123";

        ExpenseGroup group = mock(ExpenseGroup.class);
        User testUser = new User();
        testUser.setId(creatorId);
        when(group.getCreator()).thenReturn(testUser);
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        boolean result = expenseGroupService.delete(groupId, wrongUserId);
        assertFalse(result);
    }

    @Test
    void testDeleteExpenseGroup_NotFound() {
        String groupId = "invalid";
        when(expenseGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        boolean result = expenseGroupService.delete(groupId, 1L);
        assertFalse(result);
    }
}
