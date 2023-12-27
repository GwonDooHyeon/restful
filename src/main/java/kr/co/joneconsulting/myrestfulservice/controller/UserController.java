package kr.co.joneconsulting.myrestfulservice.controller;

import jakarta.websocket.server.PathParam;
import kr.co.joneconsulting.myrestfulservice.bean.User;
import kr.co.joneconsulting.myrestfulservice.dao.UserDaoService;
import kr.co.joneconsulting.myrestfulservice.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable(name = "id") int id) {
        User user = service.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saveduser = service.save(user);

        // postman 결과 창 Headers에 Location을 알려준다. ( 상세 보기 )
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveduser.getId())
                .toUri();

        // 201로 created 요청값 변경
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable(name = "id") int id) {
        User deletedUser = service.deleteById(id);

        if (deletedUser == null) {
            throw new UserNotFoundException("ID[%s] not found".formatted(id));
        }

        return ResponseEntity.noContent().build();
    }
}
