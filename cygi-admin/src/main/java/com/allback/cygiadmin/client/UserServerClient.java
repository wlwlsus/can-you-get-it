package com.allback.cygiadmin.client;

import com.allback.cygiadmin.dto.response.UserResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-server", url = "${user.server.url}", path = "${user.server.prefix}")
@Component
public interface UserServerClient {
    @GetMapping("/users")
    ResponseEntity<List<UserResDto>> getUsers();
}
