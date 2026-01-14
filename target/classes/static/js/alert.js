// 알림 불러오기
function loadAlerts() {
    $.get("/alerts/list", function (alerts) {
        const list = $("#alertList");
        if(list.length === 0) {
            $("#content").html('<div id="alertList" class="alerts-container"></div>');
            list = $("#alertList");
        }

        list.empty();

        alerts.forEach(alert => {
            const unreadClass = alert.readYn === 'N' ? 'unread' : '';
            const unreadStyle = alert.readYn === 'N' ? 'font-weight: bold;' : '';
            const alertHtml = `
                <div class="alert-item ${unreadClass}" data-id="${alert.id}">
                    <div class="alert-icon">💬</div>
                    <div class="alert-content">
                        <div class="alert-post" style="${unreadStyle}">
                            ${alert.content}
                        </div>
                        <div class="alert-time">
                            ${formatTimeAgo(alert.createdAt)}
                        </div>
                    </div>
                </div>
            `;
            list.append(alertHtml);
        });

        // 읽음 처리
        $.ajax({
            type: "PUT",
            url: "/alerts/read/" + loginUserId,
            success: () => {
                $(this).removeClass("unread");
                updateAlertBadge()
            },
            error: () => { showToast("읽음 처리 실패", "error"); }
        });
    });
}