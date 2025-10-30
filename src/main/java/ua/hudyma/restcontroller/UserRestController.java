package ua.hudyma.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.hudyma.domain.orders.BuyerReqDto;
import ua.hudyma.domain.users.BuyerRespDto;
import ua.hudyma.domain.users.VendorReqDto;
import ua.hudyma.domain.users.VendorRespDto;
import ua.hudyma.service.UserService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserRestController {
    private final UserService userService;

    @PostMapping("/vendors/add")
    public ResponseEntity<VendorRespDto> createVendor (@RequestBody VendorReqDto dto){
        return ResponseEntity.ok(userService.createVendor(dto));
    }
    @PostMapping("/buyers/add")
    public ResponseEntity<BuyerRespDto> createBuyer (@RequestBody BuyerReqDto dto){
        return ResponseEntity.ok(userService.createBuyer(dto));
    }
}
