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

$("#writeBtn").click(function () {
    setActiveNav("writeBtn");
    $("#content").load("/posts/write");
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
    $.ajax({
        type: "GET",
        url: "/posts/list",
        success: function(posts) {
            renderPosts(posts);
        },
        error: function() {
            alert("게시글 불러오기 실패");
        }
    });
}

// --------------------------
// 게시글 렌더링
// --------------------------
function renderPosts(posts) {
    const postList = $("#postList");
    postList.empty(); // 기존 게시글 초기화

    posts.forEach(post => {
        // createdAt 포맷 처리 (방금 전 / n분 전 / HH:mm)
        const createdAt = formatTimeAgo(post.createdAt);
        
        // 게시글 이미지가 있으면 <img> 추가
        let postImagesHtml = '';
        if (post.imageUrls && post.imageUrls.length > 0) {
            postImagesHtml = '<div class="post-images-container">';
            post.imageUrls.forEach(url => {
                postImagesHtml += `<img src="${url}" alt="게시글 이미지">`;
            });
            postImagesHtml += '</div>';
        }

        const postHtml = `
            <div class="post" data-post-id="${post.id}">
                <div class="post-header">
                    <div class="post-user">
                        <span class="nickname">${post.nickname}</span>
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

function renderSinglePost(post) {
    let postImagesHtml = '';
    if (post.imageUrls && post.imageUrls.length > 0) {
        post.imageUrls.forEach(url => {
            postImagesHtml += `<div class="post-image"><img src="${url}" alt="게시글 이미지"></div>`;
        });
    }

    return `
        <div class="post" data-post-id="${post.id}">
            <div class="post-header">
                <div class="post-user">
                    <span class="nickname">${post.nickname}</span>
                    <span class="created-at">방금 전</span>
                </div>
            </div>
            <div class="post-content">${post.content}</div>
            ${postImagesHtml}
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
}
