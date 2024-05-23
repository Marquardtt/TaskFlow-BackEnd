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

    private Boolean notifications = true;

    private String primaryColor = "#F04A94";
    private String secondaryColor = "#F76858";

    @Enumerated(value = EnumType.STRING)
    private Theme theme = Theme.LIGHT;
    private Integer fontSize = 16;
    @Enumerated(value = EnumType.STRING)
    private Language language = Language.PORTUGUESE;
    private Boolean libras = false;
    private Boolean textToSound = false;

    //add, remove or change
    private Boolean notificTasks = true;
    private Boolean notificAtAddMeInAGroup = true;
    private Boolean notificWhenChangeMyPermission = true;
    //when pass a specific number of points (1000, 5000, etc.)
    private Boolean notificMyPointsChange = true;
    //when i schedule a task or project
    private Boolean notificSchedules = true;
    //when a project or task deadline is near
    private Boolean notificDeadlines = true;
    //when i receive a message
    private Boolean notificChats = true;
    //when ia task than i am responsible receive a comment
    private Boolean notificComments = true;

    private Boolean initialPageTasksPerDeadline = true;
    private Boolean googleCalendar = false;
    private Boolean isTutorialMade = false;
    private Boolean showPropertiesName = true;
    private String font = "Montserrat";

}
