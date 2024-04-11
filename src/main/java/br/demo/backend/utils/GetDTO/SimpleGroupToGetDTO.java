package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.Group;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class SimpleGroupToGetDTO implements ModelToGetDTO<Group, SimpleGroupGetDTO> {
    @Override
    public SimpleGroupGetDTO tranform(Group group) {
        if(group == null) return null;
        SimpleGroupGetDTO groupGet = new SimpleGroupGetDTO();
        BeanUtils.copyProperties(group, groupGet);
        return groupGet;
    }
}
