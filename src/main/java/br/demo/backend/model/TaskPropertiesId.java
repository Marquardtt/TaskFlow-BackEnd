package br.demo.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TaskPropertiesId implements Serializable {
    private Long taskId;
    private Long propertyId;


}
