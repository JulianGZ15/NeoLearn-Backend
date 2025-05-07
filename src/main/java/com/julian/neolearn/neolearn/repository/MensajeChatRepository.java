package com.julian.neolearn.neolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.julian.neolearn.neolearn.entity.MensajeChat;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
}

