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

	@Test
	void deductUserCash() {
		// given: 사용자와 관련된 객체들을 생성하고 설정
		long userId = 1L;
		int cash = 1000;
		int deductCash = 500;
		Passbook passbook = Passbook.builder().cash(cash).build();
		Users user = Users.builder().userId(userId).passbookId(passbook).build();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(passbookRepository.findById(user.getPassbookId().getPassbookId())).thenReturn(Optional.of(passbook));

		try {
			userService.deductUserCash(userId, deductCash);

			// then: passbok의 cash가 올바르게 차감되었는지 확인
			assertEquals(500, passbook.getCash());

			verify(userRepository).save(eq(user)); // User 객체가 저장되었는지 확인

			verifyNoMoreInteractions(passbookRepository); // 다른 메소드 호출이 없는지 확인

		} catch (BaseException e) {
			fail("예외 발생하지 않아야 한다.");
		}

		try {
			int invalidPrice = 1500; // 잔액보다 큰 금액으로 호출할 경우 예외 발생

			userService.deductUserCash(userId, invalidPrice);

			fail("잔액 부족 예외가 발생해야 한다.");
		} catch (BaseException e) {
			assertEquals(ErrorMessage.NOT_ENOUGH_CASH.getErrorMessage(), e.getErrorMessage().get(0));
		}
	}
}