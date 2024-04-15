package br.demo.backend.model.interfaces;

import br.demo.backend.model.dtos.user.UserGetDTO;

import java.util.Collection;

public interface WithMembers {

     Collection<UserGetDTO> getMembersDTO();
}
