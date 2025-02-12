package com.example.jwtTokenSpring.repository;

import com.example.jwtTokenSpring.entity.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {


}
