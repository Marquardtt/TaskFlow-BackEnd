package br.demo.backend.model.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Data
public class DestinationId implements Serializable {
    private Long userId;
    private Long messageId;
}
