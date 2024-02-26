package br.demo.backend.model.values;

import br.demo.backend.model.Archive;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_valued_archive")
public class ArchiveValued extends Value{
    @OneToOne(cascade = CascadeType.ALL)
    private Archive archive;

    public ArchiveValued(Long id, Archive archive){
        super(id);
        this.archive = archive;
    }

    @Override
    public void setValue(Object value){this.archive = (Archive) value;}

    @Override
    public Object getValue(){
        return this.archive;
    }
}
