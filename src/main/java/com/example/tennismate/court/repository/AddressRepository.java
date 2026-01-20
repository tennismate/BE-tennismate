package com.example.tennismate.court.repository;

import com.example.tennismate.court.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //이 인터페이스는 디비랑 연결되는 저장소 객체야!!, spring container에 등록
public interface AddressRepository extends JpaRepository<Address, Long> {
}