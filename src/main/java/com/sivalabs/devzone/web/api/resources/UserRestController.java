package com.sivalabs.devzone.web.api.resources;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.CreateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.services.SecurityService;
import com.sivalabs.devzone.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.info("process=get_user, user_id=" + id);
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public UserDTO createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        log.info("process=create_user, user_email=" + createUserRequest.getEmail());
        UserDTO userDTO = new UserDTO(
            null,
            createUserRequest.getName(),
            createUserRequest.getEmail(),
            createUserRequest.getPassword(),
            null,
            null
        );
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    @AnyAuthenticatedUser
    public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO user) {
        log.info("process=update_user, user_id=" + id);
        if (!id.equals(securityService.loginUserId())) {
            throw new ResourceNotFoundException("User not found with id=" + id);
        }
        user.setId(id);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    @AnyAuthenticatedUser
    public void deleteUser(@PathVariable Long id) {
        log.info("process=delete_user, user_id=" + id);
        userService.getUserById(id).map(u -> {
            if (!id.equals(securityService.loginUserId()) && !securityService.isCurrentUserAdmin()) {
                throw new ResourceNotFoundException("User not found with id=" + id);
            }
            userService.deleteUser(id);
            return u;
        });
    }


}
