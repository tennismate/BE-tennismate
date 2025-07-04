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

    @Test
    @DisplayName(value = "중복 이메일 검증 테스트")
    void givenMember_whenExistingEmail_thenReturnTrue() {
        // given
        Member member = Member.of(
                "exist@email.com",
                "testPassword123!",
                "testNickname",
                "010-1111-2222",
                25,
                null,
                null,
                MemberRole.ROLE_USER
        );

        // when
        memberRepository.save(member);
        boolean isExistingEmail = memberRepositoryAdapter.existsByEmail("exist@email.com");

        // then
        assertThat(isExistingEmail).isTrue();

    }

    @Test
    @DisplayName(value = "중복되지 않는 이메일 검증 테스트")
    void givenMember_whenNonExistingEmail_thenReturnFalse() {
        // given
        // 이메일 정보가 존재하지 않는 상황이기 때문에, Member 객체 저장 x

        // when
        boolean isExistingEmail = memberRepositoryAdapter.existsByEmail("nonexist@email.com");

        // then
        assertThat(isExistingEmail).isFalse();

    }

}
