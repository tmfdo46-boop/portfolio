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

    loadPosts(); // 초기 로드

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
    $("#writeBtn").click(function() {
        setActiveNav("writeBtn");
        $("#content").load("/posts/postWrite.html"); // 글쓰기 화면 불러오기
    });

    $("#profileBtn").click(function() {
        setActiveNav("profileBtn");
        $("#content").load("/users/profile"); // 프로필 화면 불러오기
    });

    $("#messageBtn").click(function() {
        setActiveNav("messageBtn");
        $("#content").load("/messages/list"); // 메시지 화면
    });

    $("#alertBtn").click(function() {
        setActiveNav("alertBtn");
        $("#content").load("/alerts/list"); // 알림 화면
    });
    
    $("#writeBtn").click(function () {
        setActiveNav("writeBtn");
        $("#content").load("/posts/write");
    });

    // --------------------------
    // 검색 버튼 (기본 예시)
    // --------------------------
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

});

// 이벤트 위임
$(document).on("click", ".like-btn", function() {
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

// --------------------------
// 하단 네비게이션 버튼
// --------------------------
function setActiveNav(targetId) {
    $(".nav-icon").removeClass("active");
    $(`#${targetId}`).addClass("active");
}

// --------------------------
// 게시글 불러오기
// --------------------------
function loadPosts() {
    $.get("/posts/list", function(posts){
        const postList = $("#postList");
        postList.empty();
        posts.forEach(post=>{
            // createdAt 포맷 처리 (방금 전 / n분 전 / HH:mm)
            const createdAt = formatTimeAgo(post.createdAt);
            
            // 게시글 이미지가 있으면 <img> 추가
            let postImagesHtml = '';
            if (post.imagePaths && post.imagePaths.length > 0) {
                postImagesHtml = '<div class="post-images-container">';
                post.imagePaths.forEach(url => {
                    postImagesHtml += `<img src="${url}" alt="게시글 이미지">`;
                });
                postImagesHtml += '</div>';
            }

            const postHtml = `
                <div class="post" data-post-id="${post.id}">
                    <div class="post-header">
                        <div class="post-user">
                            <span class="nickname">${post.authorName}</span>
                            <span class="created-at">${createdAt}</span>
                        </div>
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
                            <span>0</span>
                        </div>
                    </div>
                </div>
            `;

            postList.append(postHtml);
        });
    });
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

        $("#postContent").html(`
            <div class="post" data-post-id="${post.id}">
                <div class="post-header">
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