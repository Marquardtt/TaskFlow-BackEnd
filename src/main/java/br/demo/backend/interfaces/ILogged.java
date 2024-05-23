package br.demo.backend.interfaces;

import br.demo.backend.model.relations.PropertyValue;
import br.demo.backend.model.tasks.Log;

import java.util.Collection;

public interface ILogged {

    Collection<Log> getLogs();
    void setLogs(Collection<Log> logs);
    Collection<PropertyValue> getPropertiesValues();
    String getName();
    Long getId();
}
