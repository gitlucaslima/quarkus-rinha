package com.rinha.infra;

import jakarta.enterprise.context.ApplicationScoped;
import org.modelmapper.ModelMapper;

@ApplicationScoped
public class ModelMapperConfig extends ModelMapper {

    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
