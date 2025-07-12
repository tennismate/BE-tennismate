package com.example.tennismate.global.config;

import com.example.tennismate.global.enums.MemberRole;
import com.example.tennismate.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.example.tennismate.member.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DisplayName(value = "Querydsl 테스트")
public class QuerydslTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName(value = "Querydsl 동작 확인을 위한 테스트")
    void querydslBasicTest() {
        // given
        Member testMember = new Member(
                "test@email.com",
                "testPassword",
                "testName",
                "010-1111-2222",
                23,
                "testurl",
                "testurl",
                MemberRole.ROLE_USER
        );
        em.persist(testMember);

        // when
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.nickname.eq("testName"))
                .fetchOne();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@email.com");

    }
}
