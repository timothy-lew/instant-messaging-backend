package com.timothy.websocket.user;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status status);

    List<User> findAll();

    User findByNickName(String nickName);
}
