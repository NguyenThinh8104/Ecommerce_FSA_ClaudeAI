package training.g2.constant;

public class Constants {
    public static final class UserExceptionInformation {
        public static final String USER_NOT_FOUND_MESSAGE = "Không tìm thấy người dùng";
        public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Tên đăng nhập đã tồn tại";
        public static final String EMAIL_ALREADY_EXISTS_MESSAGE = "Email đã được sử dụng";
        public static final String PHONE_ALREADY_EXISTS_MESSAGE = "Số điện thoại đã được sử dụng";
        public static final String FAIL_TO_SAVE_USER_MESSAGE = "Lưu thông tin người dùng thất bại";
        public static final String GENDER_INVALID = "Giới tính không hợp lệ";
        public static final String USER_INFORMATION_NULL_OR_EMPTY = "Thông tin này là bắt buộc";
        public static final String USERNAME_INVALID = "Tên đăng nhập chỉ bao gồm chữ cái, số, dấu gạch ngang (-), gạch dưới (_) và có độ dài từ 8 đến 30 ký tự";
        public static final String PASSWORD_INVALID = "Mật khẩu phải từ 8 ký tự trở lên, bao gồm ít nhất 1 chữ hoa, 1 chữ thường và 1 ký tự đặc biệt";
        public static final String FULL_NAME_INVALID = "Họ tên phải bắt đầu bằng chữ cái, chỉ chứa chữ cái và khoảng trắng";
        public static final String PHONE_INVALID = "Số điện thoại phải gồm đúng 10-15 chữ số";
        public static final String EMAIL_INVALID = "Email không hợp lệ";
        public static final String ROLES_NAME_INVALID = "Tên vai trò không hợp lệ";
        public static final String USER_NOT_FOUND = "Không tìm thấy người dùng, vui lòng đăng nhập bằng tài khoản hợp lệ để xem thông tin cá nhân";
    }

    public static final class Message {
        public static final String CREATE_USER_SUCCESS_MESSAGE = "Tạo người dùng thành công";
        public static final String CREATE_USER_FAIL_MESSAGE = "Tạo người dùng thất bại";
        public static final String GET_USER_SUCCESS_MESSAGE = "Lấy thông tin người dùng thành công";
        public static final String GET_USER_FAIL_MESSAGE = "Lấy thông tin người dùng thất bại";
        public static final String UPDATE_USER_FAIL_MESSAGE = "Cập nhật người dùng thất bại";
        public static final String UPDATE_USER_SUCCESS_MESSAGE = "Cập nhật người dùng thành công";

        // User information
        public static final String EMPTY_FULL_NAME = "Họ và tên không được để trống";
        public static final String EMPTY_USERNAME = "Tên đăng nhập không được để trống";
        public static final String EMPTY_PASSWORD = "Mật khẩu không được để trống";
        public static final String INVALID_MATCH_PASSWORD = "Mật khẩu cũ không chính xác vui lòng nhập lại";

        // Contact information
        public static final String EMPTY_PHONE_NUMBER = "Số điện thoại không được để trống";
        public static final String INVALID_PHONE_NUMBER = "Định dạng số điện thoại không hợp lệ. Phải từ 10 đến 15 chữ số";
        public static final String EMPTY_EMAIL = "Email không được để trống";
        public static final String INVALID_EMAIL = "Định dạng email không hợp lệ";

        // User management
        public static final String DUPLICATE_USERNAME_MESSAGE = "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác";
        public static final String GET_ALL_USER_SUCCESS_MESSAGE = "Lấy danh sách người dùng thành công";
        public static final String GET_ALL_USER_FAIL_MESSAGE = "Lấy danh sách người dùng thất bại";
        public static final String SEND_EMAIL_ACCOUNT_FAIL = "Gửi email thất bại";
        public static final String GET_USERS_WITH_ROLE_SUCCESS = "Lấy tất cả người dùng với vai trò ";
        public static final String GET_ALL_STAFF_USERS_SUCCESS = "Lấy tất cả người dùng nhân viên thành công";

        // General Status Messages
        // ===================================================
        public static final String GENERAL_SUCCESS_MESSAGE = "Thành công";

        public static final String SUCCESS = "Thành công";
        public static final String FAILED = "Thất bại";
        public static final String SUCCESSFULLY = " thành công";
        public static final String STATUS_INVALID = " trạng thái không hợp lệ";
        public static final String ROLE_NOT_FOUND_MESSAGE = "Không tìm thấy role";
        public static final String GET_ALL_CATEGORY_SUCCESS = "lấy danh sách danh mục thành công";

        public static final String ADD_CATEGORY_SUCCESS = "Thêm danh mục thành công";
        public static final String ADD_CATEGORY_FAIL = "Không thể thêm danh mục";
        public static final String UPDATE_CATEGORY_SUCCESS = "Cập nhật danh mục thành công";
        public static final String UPDATE_CATEGORY_FAIL = "Không thể cập nhật danh mục";
        public static final String DELETE_CATEGORY_SUCCESS = "Xóa danh mục thành công";
        public static final String DELETE_CATEGORY_FAIL = "Không thể xóa danh mục";
        public static final String GET_CATEGORY_SUCCESS = "Lấy thông tin danh mục thành công";
        public static final String GET_CATEGORY_FAIL = "Không thể lấy thông tin danh mục";
        public static final String CATEGORY_NOT_FOUND = "Không tìm thấy danh mục";
        public static final String CATEGORY_PARENT_NOT_FOUND = "Không tìm thấy danh mục cha";

        // Thông báo lỗi
        public static final String PRODUCT_NOT_FOUND = "Sản phẩm không tồn tại";
        public static final String PRODUCT_ALREADY_EXISTS = "Sản phẩm đã tồn tại";
        public static final String CODE_ALREADY_EXISTS = "Code  đã tồn tại";
        public static final String ADD_PRODUCT_FAIL = "Thêm sản phẩm thất bại";
        public static final String UPDATE_PRODUCT_FAIL = "Cập nhật sản phẩm thất bại";
        public static final String DELETE_PRODUCT_FAIL = "Xóa sản phẩm thất bại";
        public static final String GET_PRODUCT_SUCCESS = "Lấy thông tin sản phẩm thành công";
        public static final String GET_PRODUCT_FAIL = "Lấy thông tin sản phẩm thất bại";

        public static final String PRODUCT_CREATED_SUCCESS = "Tạo sản phẩm thành công";
        public static final String PRODUCT_UPDATED_SUCCESS = "Cập nhật sản phẩm thành công";
        public static final String PRODUCT_DELETED_SUCCESS = "Xóa sản phẩm thành công";

        public static final String VARIANT_CREATE_SUCCESS = "Tạo biến thể sản phẩm thành công";
        public static final String ATTRIBUTE_VALUE_REQUIRED = "AttributeValue là bắt buộc";
        public static final String ATTRIBUTE_VALUE_CONFLICT = "Một hoặc nhiều AttributeValue không tồn tại";
        public static final String VARIANT_DUPLICATED = "Biến thể với tổ hợp thuộc tính này đã tồn tại";
        public static final String VARIANT_NOT_FOUND = "Không tìm thấy biến thể";
        public static final String VARIANT_UPDATE_SUCCESS = "Cập nhật biến thể thành công";
        public static final String VARIANT_DELETE_SUCCESS = "Xóa biến thể thành công";
        public static final String VARIANT_GET_SUCCESS = "Lấy danh chi tiết biến thể thành công";
        public static final String VARIANT_GET_LIST_SUCCESS = "Lấy danh sách biến thể thành công";
        public static final String DUPLICATE_VARIANT_EXISTS = "Đã tồn tại ít nhất 1 biến thể";

        public static final String ATTRIBUTES_NOT_FOUND = "Thuộc tính không tồn tại";
        public static final String ATTRIBUTES_ALREADY_EXISTS = "Thuộc tính đã tồn tại";
        public static final String ATTRIBUTES_ADD_FAIL = "Thêm thuộc tính thất bại";
        public static final String ATTRIBUTES_UPDATE_FAIL = "Cập nhật thuộc tính thất bại";
        public static final String ATTRIBUTES_DELETE_FAIL = "Xóa thuộc tính thất bại";
        public static final String ATTRIBUTES_GET_FAIL = "Lấy thông tin thuộc tính thất bại";
        public static final String ATTRIBUTES_CREATED_SUCCESS = "Tạo thuộc tính thành công";
        public static final String ATTRIBUTES_UPDATED_SUCCESS = "Cập nhật thuộc tính thành công";
        public static final String ATTRIBUTES_DELETED_SUCCESS = "Xóa thuộc tính thành công";
        public static final String ATTRIBUTES_CODE_OR_NAME_IS_EXISTED = "Code hoặc tên đã tồn tại";
        public static final String ATTRIBUTES_GET_SUCCESS = "Lấy thuộc tính thành công";

        public static final String ATTRIBUTE_VALUE_EXISTS = "Giá trị thuộc tính đã tồn tại";
        public static final String ATTRIBUTE_VALUE_NOT_FOUND = "Không tìm thấy giá trị thuộc tính";
        public static final String ATTRIBUTE_VALUE_CREATE_FAIL = "Thêm giá trị thuộc tính thất bại";
        public static final String ATTRIBUTE_VALUE_UPDATE_FAIL = "Cập nhật giá trị thuộc tính thất bại";
        public static final String ATTRIBUTE_VALUE_DELETE_FAIL = "Xóa giá trị thuộc tính thất bại";
        public static final String ATTRIBUTE_VALUE_CREATED_SUCCESS = "Thêm giá trị thuộc tính thành công";
        public static final String ATTRIBUTE_VALUE_UPDATED_SUCCESS = "Cập nhật giá trị thuộc tính thành công";
        public static final String ATTRIBUTE_VALUE_DELETED_SUCCESS = "Xóa giá trị thuộc tính thành công";
        public static final String ATTRIBUTE_VALUE_GET_FAIL = "Lấy giá trị thuộc tính thất bại";

        public static final String ROLE_NOT_FOUND = "Vai trò không tồn tại";
        public static final String ROLE_ALREADY_EXISTS = "Vai trò đã tồn tại";
        public static final String ROLE_ADD_FAIL = "Thêm vai trò thất bại";
        public static final String ROLE_UPDATE_FAIL = "Cập nhật vai trò thất bại";
        public static final String ROLE_DELETE_FAIL = "Xóa vai trò thất bại";
        public static final String ROLE_GET_FAIL = "Lấy thông tin vai trò thất bại";
        public static final String ROLE_CREATED_SUCCESS = "Tạo vai trò thành công";
        public static final String ROLE_UPDATED_SUCCESS = "Cập nhật vai trò thành công";
        public static final String ROLE_DELETED_SUCCESS = "Xóa vai trò thành công";


        public static final String INVENTORY_NOT_FOUND = "Không tìm thấy thông tin kho.";
        public static final String INVENTORY_ADJUST_QUANTITY_INVALID = "Số lượng điều chỉnh phải lớn hơn 0";
        public static final String INVENTORY_NOT_ENOUGH_STOCK = "Không đủ hàng tồn kho để thực hiện thao tác";
        public static final String INVENTORY_ADJUST_SUCCESS = "Điều chỉnh kho thành công";
        public static final String INVENTORY_ADJUST_FAIL = "Điều chỉnh kho không thành công";
        public static final String INVENTORY_OPTIMISTIC_LOCK_FAIL = "Dữ liệu kho vừa bị thay đổi bởi một tác nhân khác.";
        public static final String INVENTORY_MIN_MAX_FAIL = "Min không được lớn hơn max.";

        public static final String CONTACT_MESSAGE_NOT_FOUND = "Liên hệ không tồn tại";

        // Voucher management

        public static final String VOUCHER_NOT_FOUND = "Voucher không tồn tại";
        public static final String VOUCHER_CODE_EXISTS = "Mã voucher đã tồn tại";
        public static final String INVALID_DATE_RANGE = "Ngày bắt đầu phải trước ngày kết thúc";
        public static final String INVALID_DISCOUNT_VALUE = "Giá trị giảm giá không hợp lệ";
        public static final String INVALID_USAGE_LIMIT = "Giới hạn lượt dùng phải >= 0";
        public static final String INVALID_USER_LIMIT = "Giới hạn mỗi người dùng phải >= 0";
        public static final String CANNOT_UPDATE_STARTED_VOUCHER = "Chỉ được sửa voucher chưa bắt đầu";
    }

    public static final class Regex {
        // public static final String REGEX_PASSWORD = "$d{8}^";
        public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        public static final String REGEX_FULLNAME = "^[a-zA-Z][a-zA-Z\s]*$";
        public static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        public static final String REGEX_PHONE = "^[0-9]{10,15}$";

    }

}
