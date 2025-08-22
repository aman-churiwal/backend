package viteezy.service;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class UtilityServiceImpl implements UtilityService {

    private static final String EMPTY_STRING = "";

    @Override
    public String buildReferralCode(String firstName) {
        final String firstNameUntilSpace = firstName.split("\\s+")[0];
        final String randomNumber = new DecimalFormat("00000").format(new Random().nextInt(99999));
        return firstNameUntilSpace.toUpperCase().concat(randomNumber);
    }

    @Override
    public String getFirstName(String firstName, String lastName) {
        final String replacedFirstName = StringUtils.stripAccents(firstName).replaceAll("(?i)"+ Pattern.quote(StringUtils.stripAccents(lastName)), EMPTY_STRING).trim();
        boolean lastNameContainsFirstName = StringUtils.stripAccents(firstName).toLowerCase().contains(StringUtils.stripAccents(lastName).toLowerCase());
        if (lastNameContainsFirstName && !replacedFirstName.isEmpty()) {
            return StringUtils.capitalize(replacedFirstName);
        } else if (lastNameContainsFirstName && firstName.contains(" ")) {
            return StringUtils.capitalize(firstName.substring(0, firstName.indexOf(" ")));
        } else {
            return StringUtils.capitalize(firstName);
        }
    }
}
