package br.demo.backend.controller;

import br.demo.backend.model.dtos.permission.PermissionGetDTO;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.model.dtos.user.UserPutDTO;
import br.demo.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    //TODO: Verificar com os outros quais informações que o
    // usuario pode ver de outro usuario (mudar na getDTO) e
    // tambem tem que ver as informações que seram encriptadas

    //   FEITO
    @PostMapping
    public UserGetDTO insert(@RequestBody UserPostDTO user){
        return userService.save(user);
    }

  //TODO: verificar com a heloisa se eu posso tirar isso
    @GetMapping("/{username}/project/{projectId}")
    //  FEITO => PORÉM PROVÁVEL QUE TERÁ ALTERAÇÕE
    public PermissionGetDTO getPermisisonInAProject(@PathVariable String username, @PathVariable Long projectId){
        return userService.getPermissionOfAUserInAProject(username, projectId);
    }

    //   FEITO
    @PutMapping
    public UserGetDTO upDate(@RequestBody UserPutDTO user){
        return userService.update(user, false);
    }
    //   FEITO
    @PatchMapping
    public UserGetDTO patch(@RequestBody UserPutDTO user) {
        return userService.update(user, true);
    }

    //    precisa estar num mesmo projeto ou group que o outro user   IMPLEMENTAR O ACCESS => BECKER
    @GetMapping("/{username}")
    public UserGetDTO findOne(@PathVariable String username){
        return userService.findOne(username);
    }

    //precisa estar logado
    @GetMapping("/logged")
    public UserGetDTO findLogged(){
        return userService.findLogged();
    }

    @PatchMapping("/picture/{username}")
    public UserGetDTO upDatePicture(@RequestParam MultipartFile picture, @PathVariable String username) {
        return userService.updatePicture(picture, username);
    }

    //FEITO
    @PatchMapping("/password/{username}")
    public UserGetDTO upDatePassword(@PathVariable String username, @RequestBody String password) {
        return userService.updatePassword(username, password);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id)
        //Ao deletar um usuario ele tem que setar o novo owner de seus projetos
    }

    //FEITO
    @GetMapping
    public Collection<UserGetDTO> findAll() {
        return userService.findAll();
    }
//TODO: gera a partir de completar tarefas
    //Dono de algum projeto em que o usuario esteja
    @PatchMapping("/add-points/{username}")
    public UserGetDTO updatePoints(@PathVariable String username, @RequestBody Integer points) {
        return userService.addPoints(username, points);

    }

  //TODO: verificar o porque dessa requisição
    //precisa ser o dono do grupo em que o usuario esta nesse projeto
    @PatchMapping("{username}/update-permission")
    public PermissionGetDTO updatePermission(@PathVariable String username, @RequestBody Permission permission){
        return userService.updatePermission(username, permission);
    }

}
