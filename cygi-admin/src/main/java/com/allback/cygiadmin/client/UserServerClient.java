package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.UserResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user.server.url}",path = "${user.server.prefix}")
public interface UserServerClient {
    @GetMapping("/users")
    ResponseEntity<Page<UserResDto>> getUsers(@RequestParam("page") int page);
}
