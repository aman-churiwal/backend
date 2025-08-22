package viteezy.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilityServiceImplTest {

    private final UtilityService utilityService = new UtilityServiceImpl();
    private final String EXPECTED_FIRST_NAME = "Bas";
    private final String EXPECTED_FIRST_NAME_WITH_ACCENT = "Bás";

    @Test
    void lastNameInFirstnameCapitalLetter() {
        final String firstName = "Bas Jong";
        final String lastName = "Jong";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void lastNamesInFirstnameCapitalLetter() {
        final String firstName = "Bas De Jong";
        final String lastName = "De Jong";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void lastNameInFirstnameNoCapitalLetter() {
        final String firstName = "bas de jong";
        final String lastName = "De Jong";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void lastNameInFirstnameNoCapitalLetterWithAccent() {
        final String firstName = "bas dë jong";
        final String lastName = "dë Jòng";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void lastNameInFirstnameSomeLetterCapitalWithAccent() {
        final String firstName = "Bas dë Jong";
        final String lastName = "DE jòng";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void fullNameInFirstname() {
        final String firstName = "Bas Jong";
        final String lastName = "Bas Jong";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void fullNameInFirstnameSomeLetterCapital() {
        final String firstName = "bas De Jong";
        final String lastName = "Bas de jong";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void fullNameInFirstnameSomeLetterCapitalWithAccent() {
        final String firstName = "bas De Jöng";
        final String lastName = "Bas de jöng";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME, replacedFirstName);
    }

    @Test
    void fullNameInFirstnameSomeLetterCapitalWithAccentFirstName() {
        final String firstName = "bás De Jöng";
        final String lastName = "Bás de jöng";
        final String replacedFirstName = utilityService.getFirstName(firstName, lastName);
        assertEquals(EXPECTED_FIRST_NAME_WITH_ACCENT, replacedFirstName);
    }
}
