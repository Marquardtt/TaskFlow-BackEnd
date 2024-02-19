package br.demo.backend.model.ids;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class TaskPageId implements Serializable {
    private Long taskId;
    private Long pageId;
}
