$(document).ready(function() {
    loadLoginUser(function () {
        loadPosts(); // post.js
    });

    // 초기 뱃지 업데이트
    updateAlertBadge();
    updateMessageBadge();

    // 하단 네비 버튼 이벤트
    const navIcons = ["homeBtn","messageBtn","writeBtn","alertBtn","profileBtn"];
    
    navIcons.forEach(id => {
        $("#" + id).click(function() {
            // 활성화 클래스 제거
            navIcons.forEach(i => $("#" + i).removeClass("active"));
            // 클릭한 아이콘만 활성화
            $(this).addClass("active");
            
            updateAlertBadge();
            updateMessageBadge();

            // 실제 화면 로딩
            switch(id) {
                case "homeBtn": loadPosts(); break;
                case "messageBtn": $("#content").load("/messages/view", function () { loadFriends();}); break;
                case "writeBtn": $("#content").load("/posts/write"); break;
                case "alertBtn": $("#content").load("/alerts/view", function () { loadAlerts(); }); break;
                case "profileBtn": $("#content").load("/users/profile"); break;
            }
        });
    });

    // 하단 네비게이션 버튼
    function setActiveNav(targetId) {
        $(".nav-icon").removeClass("active");
        $(`#${targetId}`).addClass("active");
    }

    // 로그아웃 버튼
    $("#logoutBtn").click(function() {
        window.location.href = "/users/login";
    });

    // 새로고침 버튼
    $("#refreshBtn").click(function() {
        // content 초기화
        $("#content").html('<div id="postList"></div>');

        // 게시글 목록 로드
        loadPosts();
    });

    // 홈 버튼
    $("#homeBtn").click(function() {
        setActiveNav("homeBtn");
        // content 초기화
        $("#content").html('<div id="postList"></div>');

        // 게시글 목록 로드
        loadPosts();
    });
    
    // 메시지 화면
    $("#messageBtn").click(function() {
        setActiveNav("messageBtn");
        loadFriends();
    });

    // 게시글 클릭 → 상세 페이지 이동
    $(document).on("click", ".post", function(e){
        if ($(e.target).closest(".like-btn").length > 0) return;
        if ($(e.target).is("img")) return; // 이미지 클릭 시 상세 페이지 이동 막기

        const postId = $(this).data("post-id");

        $("#content").load("/posts/detailView", function() {
            initPostDetail(postId);
        });
    });

    // 글쓰기 화면 불러오기
    $("#writeBtn").click(function() {
        setActiveNav("writeBtn");
        $("#content").load("/posts/write"); 
    });

    // 알림 화면
    $("#alertBtn").click(function() {
        setActiveNav("alertBtn");
        loadAlerts();
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
                    showToast("검색 실패", "error");
                }
            });
        }
    });
});