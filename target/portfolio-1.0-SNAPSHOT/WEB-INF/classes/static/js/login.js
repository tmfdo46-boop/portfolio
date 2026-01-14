$(document).ready(function() {
    const $toast = $("#toast");
    const $toastMessage = $("#toast-message");

    // ---------------------------
    // 토스트 메시지 표시
    // ---------------------------
    function showToast(message, type) {
        $toastMessage.text(message);
        $toast.css({
            "background-color": type === "success" ? "#54acf9" : "#e74c3c",
            "visibility": "visible",
            "opacity": "1"
        });

        setTimeout(() => {
            $toast.css("opacity", "0");
            setTimeout(() => { $toast.css("visibility", "hidden"); }, 300);
        }, 5000);
    }

    $("#loginBtn").click(function(e) {
        e.preventDefault(); // form 기본 submit 방지
        const data = {
            email: $("#email").val(),
            password: $("#password").val()
        };

        $.ajax({
            type: "POST",
            url: "/users/login",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function(response) {
                if(response === "success") {
                    window.location.href = "/main";
                } else {
                    showToast("아이디 또는 비밀번호가 올바르지 않습니다.", "error");
                    return;
                }
            },
            error: function() {
                showToast("로그인 중 오류가 발생했습니다.", "error");
            }
        });
    });
});
