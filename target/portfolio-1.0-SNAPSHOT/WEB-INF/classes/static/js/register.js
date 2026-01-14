$(document).ready(function () {
    const $form = $("#registerForm");
    const $toast = $("#toast");
    const $toastMessage = $("#toast-message");
    let emailValid = false;

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

    // 도메인 선택 시 input 자동 채움
    document.getElementById('domain_select').addEventListener('change', function() {
        if (this.value) {
            document.getElementById('email_domain').value = this.value;
        } else {
            document.getElementById('email_domain').value = '';
        }
    });

    // ---------------------------
    // 이메일 처리
    // ---------------------------
    function updateRealEmail() {
        const id = $("#email_id").val().trim();
        const domain = $("#email_domain").val().trim();
        $("#email").val(id && domain ? id + "@" + domain : "");
    }

    $("#email_id, #email_domain").on("input", updateRealEmail);
    $("#domain_select").on("change", function () {
        const val = $(this).val();
        if (val === "") {
            $("#email_domain").val("").prop("readonly", false).focus();
        } else {
            $("#email_domain").val(val).prop("readonly", true);
        }
        updateRealEmail();
    });

    // ---------------------------
    // 이메일 중복 확인
    // ---------------------------
    $('#check-email-btn').click(function () {
        const email = $('#email').val();
        if (!email) {
            $('#email-check-msg').text('이메일을 입력해주세요.');
            emailValid = false;
            return;
        }

        $.get('/users/check-email', { email: email }, function (data) {
            if (data.exists) {
                $('#email-check-msg')
                    .text('이미 사용 중인 이메일입니다.')
                    .css('color', '#e74c3c');
                emailValid = false;
            } else {
                $('#email-check-msg')
                    .text('사용 가능한 이메일입니다.')
                    .css('color', '#3498db');
                emailValid = true;
            }
        });
    });

    // ---------------------------
    // 비밀번호 검증
    // ---------------------------
    function validatePassword() {
        const pwd = $("#password").val();
        const confirm = $("#confirm-password").val();
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

        let isFormatValid = regex.test(pwd);
        let isMatchValid = pwd && pwd === confirm;

        $("#strength-message").text(pwd === "" ? "" : (isFormatValid ? "안전한 비밀번호입니다." : "대/소문자, 숫자, 특수문자를 모두 포함해야 합니다. (8자 이상)"))
            .css("color", isFormatValid ? "green" : "red");

        $("#password-message").text(confirm === "" ? "" : (isMatchValid ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다."))
            .css("color", isMatchValid ? "green" : "red");

        const $btn = $(".submit-btn");
        if (isFormatValid && isMatchValid) $btn.prop("disabled", false).css({"opacity":"1", "cursor":"pointer"});
        else $btn.prop("disabled", true).css({"opacity":"0.5", "cursor":"not-allowed"});
    }

    $("#password, #confirm-password").on("input", validatePassword);

    // ---------------------------
    // 휴대폰 자동 하이픈
    // ---------------------------
    $("#hp").on("input", function () {
        let val = $(this).val().replace(/[^0-9]/g, '');
        if (val.length < 4) $(this).val(val);
        else if (val.length < 7) $(this).val(val.substr(0,3)+'-'+val.substr(3));
        else if (val.length < 11) $(this).val(val.substr(0,3)+'-'+val.substr(3,3)+'-'+val.substr(6));
        else $(this).val(val.substr(0,3)+'-'+val.substr(3,4)+'-'+val.substr(7));
    });

    // ---------------------------
    // 주소 검색
    // ---------------------------
    function updateRealAddress() {
        $("#address").val($("#address1").val().trim() + " " + $("#address2").val().trim());
    }
    $("#address2").on("input", updateRealAddress);

    $("#address-btn").on("click", function () {
        const $layer = $("#address-layer");
        if ($layer.is(":visible")) $layer.hide();
        else {
            $layer.show();
            new daum.Postcode({
                oncomplete: function(data) {
                    let roadAddr = data.roadAddress;
                    let extra = '';
                    if(data.bname && /[동|로|가]$/g.test(data.bname)) extra += data.bname;
                    if(data.buildingName && data.apartment === 'Y') extra += extra ? ', '+data.buildingName : data.buildingName;
                    if(extra) extra = ' ('+extra+')';
                    $("#address1").val(roadAddr+extra);
                    updateRealAddress();
                    $layer.hide();
                },
                width:"100%", height:"100%", maxSuggestItems:5
            }).embed($layer[0]);
        }
    });

    // ---------------------------
    // 폼 제출
    // ---------------------------
    $form.on("submit", function(e){
        e.preventDefault();
        updateRealEmail();
        updateRealAddress();

        if (!emailValid) { showToast("이메일 중복 확인을 해주세요.", "error"); return; }

        const pwd = $("#password").val();
        const confirm = $("#confirm-password").val();
        if(pwd !== confirm) { showToast("비밀번호가 일치하지 않습니다.", "error"); return; }

        const formData = {};
        $form.serializeArray().forEach(item => formData[item.name] = item.value);

        $.ajax({
            url: "/users/register",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: function(res){
                if(res.status === "success") {
                    showToast(res.message, "success");
                    $form[0].reset();
                    
                    emailValid = false;
                    $("#email-check-msg").text("");

                    // 1.5초 후 로그인 화면으로 이동
                    setTimeout(function () {
                        window.location.href = "/users/login";
                    }, 1500);
                } else showToast(res.message, "error");
            },
            error: function(){
                showToast("회원가입 중 오류가 발생했습니다.", "error");
            }
        });
    });
});
