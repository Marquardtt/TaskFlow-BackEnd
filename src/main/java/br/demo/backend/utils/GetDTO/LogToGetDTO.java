package br.demo.backend.utils.GetDTO;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.tasks.LogGetDTO;
import br.demo.backend.model.dtos.user.OtherUsersDTO;
import br.demo.backend.model.tasks.Log;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.BeanUtils;

public class LogToGetDTO implements ModelToGetDTO<Log, LogGetDTO> {
    @Override
    public LogGetDTO tranform(Log log) {
        if(log == null) return null;
        LogGetDTO logGet = new LogGetDTO();
        BeanUtils.copyProperties(log, logGet);
        logGet.setUser(tranformSimple(log.getUser()));
        return logGet;
    }

    private static OtherUsersDTO tranformSimple(User obj){
        if(obj == null) return null;
        OtherUsersDTO simpleUser = new OtherUsersDTO();
        BeanUtils.copyProperties(obj, simpleUser);
        System.out.println(simpleUser);
        simpleUser.setUsername(obj.getUserDetailsEntity().getUsername());
        return simpleUser;
    }
}
