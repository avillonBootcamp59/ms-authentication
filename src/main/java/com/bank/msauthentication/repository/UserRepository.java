package com.bank.msauthentication.repository;

import com.bank.msauthentication.entity.UserEntity;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, String> {
    Maybe<UserEntity> findByUsername(String username);

}
