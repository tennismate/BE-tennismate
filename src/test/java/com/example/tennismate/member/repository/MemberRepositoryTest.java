package com.example.tennismate.member.repository;

import com.example.tennismate.global.config.TestQuerydslConfig;
import com.example.tennismate.global.enums.MemberRole;
import com.example.tennismate.member.entity.Member;
import com.example.tennismate.member.repository.adapter.MemberRepositoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {MemberRepositoryAdapter.class, TestQuerydslConfig.class})
public class MemberRepositoryTest {

    @Autowired
    private MemberRepositoryAdapter memberRepositoryAdapter;

    @Autowired
    private MemberRepository memberRepository;
    
    @Test
    @DisplayName(value = "회원 정보 DB 저장 테스트")
    void givenMember_whenRegister_thenSaveMember() {
        // given
        Member member = Member.of(
                "test@email.com",
                "testPassword123!",
                "testNickname",
                "010-1111-1111",
                25,
                null,
                null,
                MemberRole.ROLE_USER
        );

        // when
        memberRepositoryAdapter.register(member);

        // then
        boolean isExists = memberRepository.existsByEmail("test@email.com");
        assertThat(isExists).isTrue();

    }

}
