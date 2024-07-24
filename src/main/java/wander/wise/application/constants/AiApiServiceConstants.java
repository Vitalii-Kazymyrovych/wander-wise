package wander.wise.application.constants;

public class AiApiServiceConstants {
    public static final String SPECIFIC_LOCATION_EXAMPLES = "Examples: square, museum, market, "
            + "mall, park, certain mountain, bridge, theater, lake, "
            + "embankment, castle etc.";
    public static final String NON_EXISTING_RESTRICT = "It is important that"
            + " the locations exist. "
            + "I will be in danger if travel to non-existing location.";
    public static final String LIST_FORMATING_RULES = "Give me the list of "
            + "location. Return information as "
            + "json object. Use this format: ";
    public static final String LOCATION_NAMES_FIELD_FORMAT = "\"locationNames\": "
            + "[Location name, Location name, Location name, ect.]";
    public static final String FULL_NAME_FORMAT = """
                {\
                    "name": "Location name, that I already provided",\
                    "populatedLocality": "Village, town or city in which 
                    or near which the location is situated. If location 
                    is very big for specific populated locality 
                    (mountains, river, desert), set 
                    here the most famous populated locality, 
                    that situated nearby this location.",\
                    "region": "Part of the country, in which populated 
                    locality from previous point is situated.",\
                    "country": "Country in which region from 
                    previous point is situated.",\
                    "continent": "Continent in which country 
                    from previous point is situated."\
                }
                """;
    public static final String CHECK_LOCATION_FIRST_RULE
            = "1. Return result with empty field if the location "
            + "is present in this list: %s";
    public static final String CHECK_LOCATION_SECOND_RULE
            = "2. Return result with empty field if the location doesn't exist.";
    public static final String CHECK_LOCATION_THIRD_RULE
            = "3. Check if the location %s exists in populated "
            + "locality %s. If it exists in another populated "
            + "locality, fix the mistake. Populated locality "
            + "means city, town or village. If location is "
            + "situated near several localities, choose one.";
    public static final String CHECK_LOCATION_FOURTH_RULE
            = "4. Check if populated locality %s exists in "
            + "region %s. If it exists in another region, fix "
            + "the mistake. By region I mean local regions: "
            + "Kharkiv oblast, Île-de-France, etc. If "
            + "location situated in several regions, just choose one";
    public static final String CHECK_LOCATION_FIFTH_RULE
            = "5. Check if region %s exists in country %s. "
            + "If it exists in another country, fix the mistake.";
    public static final String CHECK_LOCATION_SIXTH_RULE
            = "6. Check if country %s exists in continent %s. "
            + "If it exists in another continent, fix the mistake.";
    public static final String CHECK_LOCATION_SEVENTH_RULE
            = "7. Check if the result location exists and "
            + "make sure, that it formatted this way: "
            + "Location name|Populated locality|Region|Country|Continent. "
            + "Each point should be present. Use \"|\" between points.";
    public static final String CHECK_LOCATION_EIGHTH_RULE
            = "Return it as json with string field \"locationName\"";
    public static final String CLIMATE_LIST = "Tropical|Polar|Temperate";
    public static final String SPECIAL_REQUIREMENTS_LIST = "With pets|With kids|"
            + "LGBTQ friendly|Disability.";
    public static final String TRIP_TYPES_LIST = "Active|Chill|Native culture|"
            + "Family|Culture|Spiritual|Extreme|Corporate|Nature|Shopping|Romantic|Party";
    public static final String REGION_EXAMPLES
            = "Examples: Kharkiv oblast for Kharkiv, Ukraine; "
            + "New York for New York City, USA; Île-de-France for Paris, France.";
    public static final String REGION_AND_CONTINENT_DTO_EXAMPLE = """
            {
                "region": "Region Name", 
                "continent": "Continent Name"
            }
            """;
}
