package gift.controller;

import gift.model.AuthInfo;
import gift.model.WishListDTO;
import gift.service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    public ResponseEntity<?> getWishList(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "4") int size, AuthInfo authInfo) {
        long memberId = authInfo.id();
        Page<WishListDTO> wishListsPage = wishListService.getWishListByMemberId(memberId, page, size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(wishListsPage.getNumber()));
        headers.add("X-Page-Size", String.valueOf(wishListsPage.getSize()));
        return ResponseEntity.ok().headers(headers).body(wishListsPage);
    }

    @PostMapping
    public ResponseEntity<?> createWishList(
        @RequestBody WishListDTO wishListDTO, AuthInfo authInfo) {
        long memberId = authInfo.id();
        wishListDTO = wishListDTO.withMemberId(memberId);

        WishListDTO createdWishList = wishListService.createWishList(wishListDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWishList);
    }

    @PutMapping
    public ResponseEntity<?> updateWishListQuantity(@RequestBody WishListDTO wishListDTO,
        AuthInfo authInfo) {
        long memberId = authInfo.id();
        wishListDTO = wishListDTO.withMemberId(memberId);
        WishListDTO updatedWishList = wishListService.updateWishListQuantity(wishListDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedWishList);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteWishList(@RequestParam long productId,
        AuthInfo authInfo) {
        long memberId = authInfo.id();
        wishListService.deleteWishList(memberId, productId);
        return ResponseEntity.noContent().build();
    }
}
