package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;

import static org.junit.jupiter.api.Assertions.*;

public class ListOfSavingTest {
    private ListOfSavings savingList;
    private Saving saving1;
    private Saving saving2;
    private Saving saving3;

    @BeforeEach
    void runBefore() {
        savingList = new ListOfSavings();
        saving1 = new Saving("college fund", 200, 0.04, true);
        saving2 = new Saving("school", 100, 0, false);
        saving3 = new Saving("mp", 50, 0, false);
    }

    @Test
    void testAddAndRemoveSaving() {
        assertEquals(0, savingList.getLength());
        savingList.addSaving(saving1);
        savingList.addSaving(saving2);
        assertEquals(2, savingList.getLength());
        savingList.removeSaving("college fund");
        assertEquals(1, savingList.getLength());
        assertEquals(savingList.getSavingDetails("school"), "Saving name: school\n" +
                " saving amount: 100\n" +
                " ");
        assertEquals(savingList.getSavingDetails("non-existent"), "Saving not found!");
    }

    @Test
    void testGetAmount() {
        savingList.addSaving(saving1);
        savingList.addSaving(saving2);
        savingList.addSaving(saving3);
        assertEquals(savingList.getTotalAmount(), 350);
        assertEquals(savingList.getFutureTotalAmount(2), 366);
        assertEquals(savingList.getFutureTotalAmount(10), 446);
    }

    @Test
    void testContains() {
        savingList.addSaving(saving1);
        savingList.addSaving(saving3);
        assertTrue(savingList.containsSaving("college fund"));
        assertFalse(savingList.containsSaving("random"));
    }

    @Test
    void testIterator() {
        savingList.addSaving(saving1);
        savingList.addSaving(saving2);
        savingList.addSaving(saving3);
        assertNotNull(savingList.iterator());
    }
}
