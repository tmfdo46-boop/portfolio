
// 최초 진입 시 홈 로딩
$(document).ready(function () {
    loadFriends();
});

let selectedFriendId = null;
// 친구 리스트 로드
function loadFriends() {
    $.get("/follows/friends", function (friends) {
        const list = $("#friendList");
        list.empty();

        friends.forEach(friend => {
            const imgSrc = friend.profileImg ? friend.profileImg : '/images/default_profile.png';
            list.append(`
                <li class="friend-item" data-id="${friend.id}">
                    <img src="${imgSrc}" class="friend-profile" />
                    <span class="friend-nickname">${friend.nickname}</span>
                </li>
            `);
        });
    });
}

// 친구 클릭 시 채팅 열기
$(document).on("click", ".friend-item", function () {
    const friendId = $(this).data("id");
    const nickname = $(this).find(".friend-nickname").text();
    const profileImg = $(this).find(".friend-profile").attr("src");

    selectedFriendId = friendId;

    $("#chatHeader").removeClass("chat-header-placeholder").html(`
        <div class="chat-header">
            <div class="chat-header-left">
                <img src="${profileImg}" class="chat-profile">
                <div class="chat-user-info">
                    <div class="chat-nickname">${nickname}</div>
                </div>
            </div>
        </div>
    `);

    $("#chatInput").prop("disabled", false);
    $("#sendBtn").prop("disabled", false);
    $("#chatInput").focus();

    loadChatMessages(friendId);
    
    // 메시지 읽음 처리
    $.ajax({
        type: "PUT",
        url: "/messages/read/" + friendId,
        success: () => {
            $(this).removeClass("unread");
            updateMessageBadge();
        },
        error: () => { showToast("읽음 처리 실패", "error"); }
    });
});

// 채팅 메시지 불러오기
function loadChatMessages(friendId) {
    $.get("/messages/chat", { friendId: friendId }, function (messages) {
        const chat = $("#chatMessages");
        chat.empty();

        messages.forEach(msg => {
            if (msg.sender.id === loginUserId) {
                // 내가 보낸 메시지
                chat.append(`
                    <div class="message-row my">
                        <div class="message-bubble my-bubble">
                            ${msg.content}
                        </div>
                    </div>
                `);
            } else {
                // 상대가 보낸 메시지
                chat.append(`
                    <div class="message-row other">
                        <div class="message-bubble other-bubble">
                            ${msg.content}
                        </div>
                    </div>
                `);
            }
        });

        chat.scrollTop(chat[0].scrollHeight);
    });
}

// 엔터키로 메시지 전송
$(document).on("keypress", "#chatInput", function (e) {
    if (e.which === 13) {
        $("#sendBtn").click();
    }
});

// 메시지 전송
$(document).on("click", "#sendBtn", function () {
    const message = $("#chatInput").val().trim();

    if (!selectedFriendId) {
        showToast("대화를 선택하세요.", "select");
        return;
    }

    if (message === "") return;

    $.ajax({
        url: "/messages/send",
        type: "POST",
        data: {
            receiverId: selectedFriendId,
            content: message
        },
        success: function () {
            $("#chatInput").val("");
            loadChatMessages(selectedFriendId);
        },
        error: function () {
            showToast("메시지 전송 실패", "error");
        }
    });
});