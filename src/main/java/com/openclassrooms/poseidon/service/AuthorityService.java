package com.openclassrooms.poseidon.service;

import com.openclassrooms.poseidon.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

  @Autowired
  private AuthorityRepository authorityRepository;



}
