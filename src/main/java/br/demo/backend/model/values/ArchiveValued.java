package br.demo.backend.model.values;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_archive_valued")
public class ArchiveValued extends Value{
    private String archive;

    public ArchiveValued(Long id, String archive){
        super(id);
        this.archive = archive;
    }

    @Override
    public void setValue(Object value){this.archive = (String)value;}

    @Override
    public Object getValue(){
        return this.archive;
    }
}
