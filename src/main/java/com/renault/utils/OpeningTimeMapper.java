package com.renault.utils;

import com.renault.dto.OpeningTimeDTO;
import com.renault.model.OpeningTime;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OpeningTimeMapper {
    OpeningTimeDTO openingTimeToOpeningTimeDTO(OpeningTime openingTime);
    OpeningTime openingTimeDTOtoOpeningTime(OpeningTimeDTO openingTimeDTO);
}
