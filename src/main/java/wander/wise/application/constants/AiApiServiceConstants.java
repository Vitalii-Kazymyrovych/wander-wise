package wander.wise.application.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AiApiServiceConstants {
    public static final String CLIMATE_LIST = "Tropical|Polar|Temperate";
    public static final String FULL_NAME_EXAMPLES =
            "Examples: Central park|New York|New York state|USA|North America, "
                    + "Freedom Square|Kharkiv|Kharkiv Oblast|Ukraine|Europe, "
                    + "Louvre|Paris|Île-de-France|France|Europe)\",";
    public static final String FULL_NAME_RULES = "(Double check this field. "
            + "Fill in each point. Use | between points. ";
    public static final String FULL_NAME_TEMPLATE = "\"location name|populated locality|"
            + "region|country|continent";
    public static final String LIST_FORMATING_RULES = "Give me the list of "
            + "location. Return information as "
            + "json object. Use this format: ";
    public static final String LOCATION_NAMES_FIELD_FORMAT = "\"locationNames\": "
            + "[\"Location name1|Populated locality|Region|Country|Continent\", "
            + "\"Location name 2|Populated locality|Region|Country|Continent\", "
            + "\"Location name 3|Populated locality|Region|Country|Continent\", ect.]";
    public static final String NON_EXISTING_RESTRICT = "It is important that"
            + " the locations exist. "
            + "I will be in danger if travel to non-existing location.";
    public static final String SPECIAL_REQUIREMENTS_LIST = "With pets|With kids|"
            + "LGBTQ friendly|Disability.";
    public static final String SPECIFIC_LOCATION_EXAMPLES = "Examples: square, museum, market, "
            + "mall, park, certain mountain, bridge, theater, lake, "
            + "embankment, castle etc.";
    public static final String TRIP_TYPES_LIST = "Active|Chill|Native culture|"
            + "Family|Culture|Spiritual|Extreme|Corporate|Nature|Shopping|Romantic|Party";
    public static final int TOTAL_REQUIRED_RESPONSES_AMOUNT = 30;
    public static final String REMOVE_DUPLICATES_FIRST_RULE = "1. Delete locations that "
            + "are present in the first list. Remove the "
            + "same names and the names, that are very similar (if the first list "
            + "is empty, skip this step).";
    public static final String REMORE_DUPLICATES_SECOND_RULE = "2. If the location "
            + "has several names and one of them is in the first "
            + "list, remove this location from the second list (if the first list is "
            + "empty, skip this step).";
    public static final String REMOVE_DUPLICATES_THIRD_RULE = "3. Use rules above "
            + "to also delete duplicates,"
            + " that the second list contains.";
    public static final String CHECK_LOCATIONS_FIRST_RULE = "1. If the location doesn't exist "
            + "remove it from the list.";
    public static final String CHECK_LOCATIONS_SECOND_RULE = "2. Locations are coupled with places "
            + "in which they situated. Carefully "
            + "check is each location really situated "
            + "in this place. If not, fix the mistake.";
    public static final String CHECK_LOCATIONS_THIRD_RULE = "3. Each location should match "
            + "this format: Location name|Populated locality|Region|Country|Continent";
    public static final String REGION_EXAMPLES = "Examples: Kharkiv oblast for Kharkiv, Ukraine; "
            + "New York for New York City, USA; Île-de-France for Paris, France.";
}
