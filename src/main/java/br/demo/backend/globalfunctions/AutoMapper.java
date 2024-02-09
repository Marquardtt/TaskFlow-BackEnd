package br.demo.backend.globalfunctions;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class AutoMapper <T> {

    ModelMapper modelMapper;

    public void map(Object source, T destinationType, Boolean patch) {
        modelMapper.getConfiguration().setSkipNullEnabled(patch);
        modelMapper.getConfiguration().setCollectionsMergeEnabled(patch);
        modelMapper.map(source, destinationType);
    }

}
