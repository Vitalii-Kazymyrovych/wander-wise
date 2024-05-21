package wander.wise.application.service.api.maps;

import wander.wise.application.dto.maps.LocationDto;

public interface MapsApiService {
    LocationDto getLocationDtoByName(String locationName);

    LocationDto getMapsResponseByUsersUrl(String usersUrl);
}
