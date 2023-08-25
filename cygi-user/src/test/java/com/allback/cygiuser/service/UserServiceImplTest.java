package com.allback.cygiuser.service;

import com.allback.cygiuser.config.oauth.entity.ProviderType;
import com.allback.cygiuser.dto.request.AmountRequest;
import com.allback.cygiuser.dto.response.UserResDto;
import com.allback.cygiuser.entity.Passbook;
import com.allback.cygiuser.entity.Users;
import com.allback.cygiuser.repository.PassbookRepository;
import com.allback.cygiuser.repository.UserRepository;
import com.allback.cygiuser.util.exception.BaseException;
import com.allback.cygiuser.util.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * author : cadqe13@gmail.com
 * date : 2023-08-25
 * description :
 */

@SpringBootTest
class UserServiceImplTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PassbookRepository passbookRepository;

	//	@Autowired
	@Autowired
	private UserServiceImpl userService;


	@Test
	void amount() {
		// given: 사용자와 관련된 객체들을 생성하고 설정
		long userId = 1L;
		int cash = 1000;
		Passbook passbook = Passbook.builder().cash(cash).build();
		Users user = Users.builder().userId(userId).passbookId(passbook).build();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(passbookRepository.findById(user.getPassbookId().getPassbookId())).thenReturn(Optional.of(passbook));

		// when: amount 메소드 실행
		AmountRequest request = new AmountRequest();
		request.setUserId(userId);
		request.setAmount(cash);

		userService.amount(request);

		// then: passbok의 cash가 올바르게 업데이트되었는지 확인
		assertEquals(cash, passbook.getCash());

		verify(passbookRepository).save(any(Passbook.class)); // save() 호출 확인
	}
}