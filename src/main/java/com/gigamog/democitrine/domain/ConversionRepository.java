package com.gigamog.democitrine.domain;

import com.gigamog.democitrine.domain.model.Conversion;
import com.gigamog.democitrine.domain.model.ConversionsTable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Repository
public class ConversionRepository {

    public Stream<Conversion> getAllConversions(){
        return Arrays.stream(ConversionsTable.values())
                .map(this::mapConversion);
    }

    private Conversion mapConversion(ConversionsTable conversionsTable){
        return Conversion.builder()
                .name(conversionsTable.name())
                .si(conversionsTable.getSi())
                .symbol(conversionsTable.getSymbol())
                .type(conversionsTable.getType())
                .unitConverstions(conversionsTable.getUnitConversions())
                .workingValue(conversionsTable.getWorkingValue())
                .build();
    }


}
