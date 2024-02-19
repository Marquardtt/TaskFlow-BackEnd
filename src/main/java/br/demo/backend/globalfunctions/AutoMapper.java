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
    public void map(Object source, T destinationType, Boolean patch, Boolean mergeCollections) {
        modelMapper.getConfiguration().setSkipNullEnabled(patch);
        modelMapper.getConfiguration().setCollectionsMergeEnabled(mergeCollections);
        modelMapper.map(source, destinationType);
    }
}
