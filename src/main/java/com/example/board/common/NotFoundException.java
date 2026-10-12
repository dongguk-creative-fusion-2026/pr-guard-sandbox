package com.example.board.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    /** "게시글이 없습니다: 1" 형식 */
    public static NotFoundException of(String what, Object id) {
        return new NotFoundException(what + "이 없습니다: " + id);
    }
}
