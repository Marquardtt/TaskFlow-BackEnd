package br.demo.backend.model.tasks;

import br.demo.backend.model.chat.Message;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.relations.Multivalued;
import br.demo.backend.model.properties.relations.Univalued;
import br.demo.backend.model.properties.relations.UserValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPostDTO {
    private Page page;

}
