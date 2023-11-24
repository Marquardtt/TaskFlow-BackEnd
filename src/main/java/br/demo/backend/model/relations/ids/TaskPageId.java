package br.demo.backend.model.relations.ids;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPageId implements Serializable {
    private Long taskId;
    private Long pageId;
}
