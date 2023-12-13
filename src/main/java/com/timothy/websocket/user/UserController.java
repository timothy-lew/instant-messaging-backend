package com.timothy.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user) {

        System.out.println("ADDING USER");
        System.out.println(user);
        System.out.println(user.getFullName());
        System.out.println(user.getNickName());
        System.out.println(user.getStatus());
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.test")
    public void test(@Payload String requestBody) {
        System.out.println("Received request body: " + requestBody);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user) {
        userService.disconnect(user);
        System.out.println(user.getStatus());
        return user;
    }

    @GetMapping("/users/online")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findUsers() {
        return ResponseEntity.ok(userService.findUsers());
    }

    @GetMapping("/user/{nickname}")
    public ResponseEntity<User> findUserByNickname(@PathVariable String nickname) {
        User user = userService.findUserByNickname(nickname);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
