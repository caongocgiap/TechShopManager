package com.techshop.server.infrastructure.constant;

import lombok.Getter;

@Getter
public enum ResponseMessage {

    INTERNAL_SERVER_ERROR("Có lỗi xảy ra, vui lòng thử lại sau"),
    NOT_FOUND("Không tìm thấy dữ liệu"),
    SUCCESS("Thành công"),
    INVALID_REQUEST("Yêu cầu không hợp lệ"),
    UNAUTHORIZED("Không có quyền truy cập"),
    FORBIDDEN("Không được phép truy cập"),
    BAD_REQUEST("Yêu cầu không hợp lệ"),
    CREATED("Tạo mới thành công"),
    UPDATED("Cập nhật thành công"),
    DELETED("Xóa thành công"),
    DUPLICATE("Dữ liệu đã tồn tại"),
    NOT_FOUND_USER("Không tìm thấy người dùng"),
    NOT_FOUND_ROLE("Không tìm thấy quyền"),
    STRING_TOO_LONG("Dữ liệu quá dài"),
    CODE_EXIST("Mã đã tồn tại"),
    INVALID_NUMBER("Dữ liệu phải là số!"),
    INVALID_DATA("Dữ liệu không hợp lệ!");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

}
