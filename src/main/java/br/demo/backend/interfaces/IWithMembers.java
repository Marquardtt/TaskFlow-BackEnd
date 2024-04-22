package br.demo.backend.interfaces;

import br.demo.backend.model.dtos.user.UserGetDTO;

import java.util.Collection;

public interface IWithMembers {

     Collection<UserGetDTO> getMembersDTO();

}
