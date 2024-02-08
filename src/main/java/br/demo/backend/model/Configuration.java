package br.demo.backend.model;

import br.demo.backend.model.enums.Language;
import br.demo.backend.model.enums.Theme;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tb_configuration")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Boolean notifications;

    private String primaryColor;
    private String secondaryColor;
    @Enumerated(value = EnumType.STRING)
    private Theme theme;
    private Integer fontSize;
    @Enumerated(value = EnumType.STRING)
    private Language language;
    private Boolean libras;
    private Boolean textToSound;
    private Boolean notificTasks;
    private Boolean notificAtAddMeInAGroup;
    private Boolean notificWhenChangeMyPermission;
    private Boolean notificMyPointsChange;
    private Boolean notificSchedules;
    private Boolean notificMylateProject;
    private Boolean notificChats;
    private Boolean notificComments;
}
