window.loginUserId = null;

$(document).ajaxError(function (event, jqxhr) {
    if (jqxhr.status === 401) {
        showToast("로그인이 필요합니다", "error");
        setTimeout(() => {
            window.location.href = "/users/login";
        }, 1500);
    }
});

function loadLoginUser(callback) {
    $.get("/users/session", function (data) {
        window.loginUser = data; // { id, email, nickname ... }
        window.loginUserId = data.id;

        if (callback) {
            callback();
        }
    }).fail(function () {
        showToast("로그인이 필요합니다.", "error");
        location.href = "/users/login";
    });
}

// 토스트 메시지 요소
function showToast(message, type) {
    const $toast = $("#toast");
    const $toastMessage = $("#toastMessage");

    $toastMessage.text(message);
    $toast.css({
        "background-color": type === "success" ? "#54acf9" : type === "error" ? "#e74c3c" : "#f3128e9f",
        "visibility": "visible",
        "opacity": "1"
    });

    setTimeout(() => {
        $toast.css("opacity", "0");
        setTimeout(() => { $toast.css("visibility", "hidden"); }, 300);
    }, 5000);
}

// 시간 포맷 함수
// 예: 방금 전, n분 전, n시간 전
function formatTimeAgo(timeString) {
    const now = new Date();
    const postTime = new Date(timeString);

    const diff = Math.floor((now - postTime) / 1000); // 초 차이

    if (diff < 60) return '방금 전';
    if (diff < 3600) return `${Math.floor(diff / 60)}분 전`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}시간 전`;

    const date = postTime;
    return `${date.getMonth()+1}월 ${date.getDate()}일 ${date.getHours()}:${String(date.getMinutes()).padStart(2,'0')}`;
}

function updateMessageBadge() {
    $.get("/messages/list", function(messages) {
        // 읽지 않은 메시지 개수
        const unreadCount = messages.filter(m => m.readYn === 'N' && m.sender.id !== loginUserId).length;

        // 기존 뱃지 제거
        $(".message-badge").remove();

        if (unreadCount > 0) {
            // #messageBtn 부모 nav-item에 뱃지 추가
            $("#messageBtn").parent().append(`<div class="message-badge">${unreadCount}</div>`);
        }
    });
}

// 알림 및 메시지 뱃지 업데이트
function updateAlertBadge() {
    $.get("/alerts/list", function(alerts) {
        // 읽지 않은 알림 개수
        const unreadCount = alerts.filter(a => a.readYn === 'N').length;

        // 기존 뱃지 제거
        $(".alert-badge").remove();

        if (unreadCount > 0) {
            // #alertBtn 부모 nav-item에 뱃지 추가
            $("#alertBtn").parent().append(`<div class="alert-badge">${unreadCount}</div>`);
        }
    });
}