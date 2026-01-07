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
                case "writeBtn": $("#content").load("/posts/write"); break;
                case "alertBtn": $("#content").load("/alerts"); break;
                case "profileBtn": $("#content").load("/users/profile"); break;
            }
        });
    });

    loadPosts(); // 초기 로드

    // --------------------------
    // 게시글 불러오기
    // --------------------------
    function loadPosts() {
        $.ajax({
            type: "GET",
            url: "/posts/list",
            success: function(posts) {
                const postList = $("#postList");
                postList.empty();

                posts.forEach(post => {
                    let imagesHtml = "";
                    if(post.imagePaths && post.imagePaths.length > 0) {
                        imagesHtml = post.imagePaths.map(path => `<img src="${path}" class="post-image">`).join("");
                    }

                    const postHtml = `
                        <div class="post">
                            <div class="post-header">
                                <span class="author">${post.authorName}</span>
                                <span class="createdAt">${post.createdAt}</span>
                            </div>
                            <div class="post-body">
                                <p>${post.content}</p>
                                ${imagesHtml}
                            </div>
                        </div>
                    `;
                    postList.append(postHtml);
                });
            },
            error: function() {
                alert("게시글 불러오기 실패");
            }
        });
    }

    // --------------------------
    // 새로고침 버튼
    // --------------------------
    $("#refreshBtn").click(function() {
        loadPosts();
    });

    // --------------------------
    // 하단 네비게이션 버튼
    // --------------------------
    function setActiveNav(targetId) {
        $(".nav-icon").removeClass("active");
        $(`#${targetId}`).addClass("active");
    }

    $("#homeBtn").click(function() {
        setActiveNav("homeBtn");
        loadPosts();
    });

    $("#writeBtn").click(function() {
        setActiveNav("writeBtn");
        $("#content").load("/posts/write"); // 글쓰기 화면 불러오기
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
