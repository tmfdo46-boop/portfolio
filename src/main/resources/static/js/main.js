$(document).ready(function() {
    // 하단 네비 버튼 이벤트
    const navIcons = ["homeBtn","messageBtn","writeBtn","alertBtn","profileBtn"];
    
    navIcons.forEach(id => {
        $("#" + id).click(function() {
            // 활성화 클래스 제거
            navIcons.forEach(i => $("#" + i).removeClass("active"));
            // 클릭한 아이콘만 활성화
            $(this).addClass("active");

            // 실제 화면 로딩
            switch(id) {
                case "homeBtn": loadPosts(); break;
                case "messageBtn": $("#content").load("/messages"); break;
                case "writeBtn": $("#content").load("/posts/postWrite.html"); break;
                case "alertBtn": $("#content").load("/alerts"); break;
                case "profileBtn": $("#content").load("/users/profile"); break;
            }
        });
    });

    let loginUserId = null;
    $.get("/users/session", function(data){
        loginUserId = data.id;
        loadPosts(); // 로그인 유저 정보 받아오고 게시글 로드
    });

    // --------------------------
    // 새로고침 버튼
    // --------------------------
    $("#refreshBtn").click(function() {
        // content 초기화
        $("#content").html('<div id="postList"></div>');

        // 게시글 목록 로드
        loadPosts();
    });

    $("#homeBtn").click(function() {
        setActiveNav("homeBtn");
        // content 초기화
        $("#content").html('<div id="postList"></div>');

        // 게시글 목록 로드
        loadPosts();
    });

    // 게시글 클릭
    $(document).on("click", ".post", function(e){
        if ($(e.target).closest(".like-btn").length > 0) return;

        const postId = $(this).data("post-id");

        $("#content").load("/posts/postDetail.html", function() {
            // postId를 여기서 직접 사용
            initPostDetail(postId);
        });
    });

    // 글쓰기 화면 불러오기
    $("#writeBtn").click(function() {
        setActiveNav("writeBtn");
        $("#content").load("/posts/postWrite.html"); 
    });

    // 메시지 화면
    $("#messageBtn").click(function() {
        setActiveNav("messageBtn");
        $("#content").load("/messages/view", function () {
            loadFriends();
        });
    });

    // 알림 화면
    $("#alertBtn").click(function() {
        setActiveNav("alertBtn");
        $("#content").load("/alerts/list"); 
    });
    
    $("#writeBtn").click(function () {
        setActiveNav("writeBtn");
        $("#content").load("/posts/write");
    });
    
    // 프로필 화면 불러오기
    $("#profileBtn").click(function() {
        setActiveNav("profileBtn");
        $("#content").load("/users/profile"); 
    });

    // 검색 버튼 (기본 예시)
    $("#searchBtn").click(function() {
        const keyword = prompt("검색어를 입력하세요");
        if(keyword) {
            $.ajax({
                type: "GET",
                url: `/posts/search?keyword=${keyword}`,
                success: function(posts) {
                    const postList = $("#postList");
                    postList.empty();
                    posts.forEach(post => {
                        postList.append(`
                            <div class="post">
                                <div class="post-header">
                                    <span class="author">${post.authorName}</span>
                                    <span class="created-at">${post.createdAt}</span>
                                </div>
                                <div class="post-body">
                                    <p>${post.content}</p>
                                </div>
                            </div>
                        `);
                    });
                },
                error: function() {
                    alert("검색 실패");
                }
            });
        }
    });

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

    // 하단 네비게이션 버튼
    function setActiveNav(targetId) {
        $(".nav-icon").removeClass("active");
        $(`#${targetId}`).addClass("active");
    }

    // 좋아요 버튼 클릭 이벤트
    $(document).on("click", ".like-btn", function(e) {
        e.stopPropagation();

        const postDiv = $(this).closest(".post");
        const postId = postDiv.data("post-id");
        const likeCountSpan = $(this).find(".like-count");
        const likeImg = $(this).find("img");

        // 현재 토글 상태 확인 (클래스로 구분)
        const liked = $(this).hasClass("liked");

        $.ajax({
            type: "POST",
            url: `/posts/like/${postId}`,
            data: JSON.stringify({ like: !liked }), // true: 좋아요, false: 취소
            contentType: "application/json",
            success: function(updatedPost) {
                likeCountSpan.text(updatedPost.likeCount); // 숫자 갱신
                if (!liked) {
                    $(postDiv).find(".like-btn").addClass("liked");
                    likeImg.attr("src", "/icons/like-filled.png"); // 하트 색상 변경
                } else {
                    $(postDiv).find(".like-btn").removeClass("liked");
                    likeImg.attr("src", "/icons/like.png"); // 기본 하트
                }
            },
            error: function() {
                alert("좋아요 처리 실패");
            }
        });
    });

    // 게시글 불러오기
    function loadPosts() {
        $.get("/posts/list", function(posts){
            const postList = $("#postList");
            postList.empty();
            posts.forEach(post=>{
                // 게시글 이미지가 있으면 <img> 추가
                let postImagesHtml = '';
                if (post.imageUrls && post.imageUrls.length > 0) {
                    postImagesHtml = '<div class="post-images-container">';
                    post.imageUrls.forEach(url => {
                        postImagesHtml += `<img src="${url}" alt="게시글 이미지">`;
                    });
                    postImagesHtml += '</div>';
                }
                const followBtnHtml = !post.following && post.userId !== loginUserId
                    ? `<button class="follow-btn" data-user-id="${post.userId}">+</button>`
                    : '';

                const postHtml = `
                    <div class="post" data-post-id="${post.id}">
                        <div class="post-header">
                            <img class="profile-img" src="${post.profileImage}">
                            ${followBtnHtml}
                            <span class="nickname">${post.nickname}</span>
                            <span class="created-at">${formatTimeAgo(post.createdAt)}</span>
                        </div>

                        <div class="post-content">
                            <p>${post.content}</p>
                            ${postImagesHtml}
                        </div>

                        <div class="post-footer">
                            <div class="post-action like-btn">
                                <img src="/icons/like.png">
                                <span class="like-count">${post.likeCount}</span>
                            </div>
                            <div class="post-action">
                                <img src="/icons/comment.png">
                                <span>${post.commentCount}</span>
                            </div>
                        </div>
                    </div>
                `;

                postList.append(postHtml);
            });
        });
    }

    // 게시글 상세 페이지
    function initPostDetail(postId){
        // 게시글 단건
        $.get("/posts/detail/" + postId, function(post){
            let imagesHtml = '';
            if (post.imageUrls && post.imageUrls.length > 0) {
                imagesHtml = '<div class="post-images-container">';
                post.imageUrls.forEach(url => {
                    imagesHtml += `<img src="${url}" class="detail-image">`;
                });
                imagesHtml += '</div>';
            }
            const followBtnHtml = !post.following && post.userId !== loginUserId
                ? `<button class="follow-btn" data-user-id="${post.userId}">+</button>`
                : '';

            $("#postContent").html(`
                <div class="post" data-post-id="${post.id}">
                    <div class="post-header">
                        <img class="profile-img" src="${post.profileImage}">
                        ${followBtnHtml}
                        <span class="nickname">${post.nickname}</span>
                        <span class="created-at">${formatTimeAgo(post.createdAt)}</span>
                    </div>

                    <div class="post-content">
                        <p>${post.content}</p>
                        ${imagesHtml}
                    </div>

                    <div class="post-footer">
                        <div class="post-action like-btn">
                            <img src="/icons/like.png">
                            <span class="like-count">${post.likeCount}</span>
                        </div>
                        <div class="post-action">
                            <img src="/icons/comment.png">
                            <span>${post.commentCount}</span>
                        </div>
                    </div>
                </div>
            `);
        });

        // 댓글 로드
        function loadComments(){
            $.get(`/comments/${postId}`, function(comments){
                const list = $("#commentList");
                list.empty();
                comments.forEach(c=>{
                    list.append(`<div class="comment"><b>${c.authorName}</b>: ${c.content}</div>`);
                });
            });
        }
        loadComments();

        // 댓글 작성
        $("#commentBtn").off("click").on("click", function(){
            const content = $("#commentContent").val();
            if(!content) return;

            $.ajax({
                type: "POST",
                url: "/comments/saveComment",
                contentType: "application/json",
                data: JSON.stringify({ postId: postId, content: content }),
                success: function(){
                    $("#commentContent").val("");
                    loadComments();
                }
            });
        });
    }

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
                        <div class="chat-status">온라인</div>
                    </div>
                </div>
            </div>
        `);

        $("#chatInput").prop("disabled", false);
        $("#sendBtn").prop("disabled", false);
        $("#chatInput").focus();

        loadChatMessages(friendId);
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
            alert("대화를 선택하세요.");
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
                alert("메시지 전송 실패");
            }
        });
    });


    // 팔로우 버튼 클릭
    $(document).on("click", ".follow-btn", function (e) {
        e.stopPropagation();
        
        const userId = $(this).data("user-id");
        const btn = $(this);

        $.post(`/follows/status/${userId}`, function () {
            btn.text("✔");
            btn.prop("disabled", true);
        });
    });

});